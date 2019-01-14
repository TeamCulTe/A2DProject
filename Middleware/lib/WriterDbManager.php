<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:07
 */

require_once "DbManager.php";

/**
 * DAO Class WriterDbManager, used to manage categories in database (CRUD).
 */
class WriterDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    const FIELDS = ["id_author", "id_book", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    const PLACEHOLDERS = [":idA", ":idB"];

    /**
     * Stores the associated table name.
     */
    const TABLE = "Writer";

    /**
     * Creates a writer into the database.
     * @param int $idAuthor The id of the user who wrote book.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function create(int $idAuthor, int $idBook)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s) VALUES(%s, %s)",
            static::TABLE, static::FIELDS[0], static::FIELDS[1], static::PLACEHOLDERS[0], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idAuthor, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Gets the writers for a specific book.
     * @param int $idBook The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getBookWriters(int $idBook)
    {
        $statement = sprintf("SELECT %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::TABLE, static::FIELDS[1],
            static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Gets the books written by a specific author.
     * @param int $idAuthor The id of the author.
     * @return null|string The json response if exists else null.
     */
    public function getWriterBooks(int $idAuthor)
    {
        $statement = sprintf("SELECT %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[1], static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idAuthor, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * From an author id and a book id given in parameter, soft delete the associated writer.
     * @param int $idAuthor The id of the user who wrote book.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function softDelete(int $idAuthor, int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s AND %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idAuthor, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From an author id and a book id given in parameter, restore the soft deleted writer.
     * @param int $idAuthor The id of the user who wrote book.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeleted(int $idAuthor, int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0 WHERE %s = %s AND %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idAuthor, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From an author id given in parameter, soft delete the associated writers.
     * @param int $idAuthor The id of the user who wrote book.
     * @return true|false True if success else false.
     */
    public function softDeleteAuthor(int $idAuthor)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idAuthor, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From an author id given in parameter, restore the associated soft deleted writers.
     * @param int $idAuthor The id of the author.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedAuthor(int $idAuthor)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0 WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idAuthor, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a book id given in parameter, soft delete the associated writers.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function softDeleteBook(int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a book id given in parameter, restore the associated soft deleted writer.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedBook(int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0 WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Deletes all writers entries from a specific author id.
     * @param int $idAuthor The id of the author.
     * @return true|false True if success else false.
     */
    public function deleteAuthor(int $idAuthor)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idAuthor, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Deletes all writers entries from a specific book id.
     * @param int $idBook The id of the author.
     * @return true|false True if success else false.
     */
    public function deleteBook(int $idBook)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Query all writers from database.
     * @return null|string The json response if found else null.
     */
    public function queryAll()
    {
        $statement = sprintf("SELECT %s, %s FROM %s WHERE deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Query all writers from database with pagination (number parameters included).
     * @param int $start The start index of the results to get.
     * @param int $end The last index of the results to get.
     * @return null|string The json response if found else null.
     */
    public function queryAllPaginated(int $start, int $end)
    {
        $offsetPlaceholder = ":startResult";
        $limitPlaceholder = ":endResult";
        $statement = sprintf("SELECT %s, %s FROM %s WHERE deleted = 0 LIMIT %s OFFSET %s",
            static::FIELDS[0], static::FIELDS[1], static::TABLE, $limitPlaceholder,
            $offsetPlaceholder);
        $req = $this->db->prepare($statement);

        $req->bindValue($offsetPlaceholder, $start, PDO::PARAM_INT);
        $req->bindValue($limitPlaceholder, $end, PDO::PARAM_INT);

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }
}