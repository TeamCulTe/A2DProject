<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:08
 */

require_once "DbManager.php";

/**
 * DAO Class BookDbManager, used to manage books in database (CRUD).
 */
class BookDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    public const FIELDS = ["id_book", "id_category", "title", "cover", "summary", "date_published", "last_update", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    public const PLACEHOLDERS = [":idB", ":idC", ":title", ":cover", ":summary", ":date", ":update"];

    /**
     * Stores the associated table name.
     */
    public const TABLE = "Book";

    /**
     * Creates a book into the database.
     * @param int $idCategory The associated category id.
     * @param string $title The title of the book.
     * @param string $cover The url of the cover.
     * @param string $summary The book's summary text.
     * @param string $date The book's written date.
     * @return string the id of the created entity.
     */
    public function create(int $idCategory, string $title, string $cover, string $summary, string $date)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s, %s, %s, %s) VALUES (%s, %s, %s, %s, %s)",
            static::TABLE, static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::PLACEHOLDERS[1], static::PLACEHOLDERS[2], static::PLACEHOLDERS[3],
            static::PLACEHOLDERS[4], static::PLACEHOLDERS[5]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idCategory, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $title, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[3], $cover, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[4], $summary, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[5], $date, PDO::PARAM_STR);

        if ($req->execute()) {
            return $this->db->lastInsertId();
        } else {
            return "";
        }
    }

    /**
     * Creates a book into the database.
     * @param int $id The id of the book.
     * @param int $idCategory The associated category id.
     * @param string $title The title of the book.
     * @param string $cover The url of the cover.
     * @param string $summary The book's summary text.
     * @param string $date The book's written date.
     * @return string the id of the created entity.
     */
    public function fullCreate(int $id, int $idCategory, string $title, string $cover, string $summary, string $date)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s, %s, %s, %s, %s) VALUES (%s, %s, %s, %s, %s, %s)",
            static::TABLE, static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3],
            static::FIELDS[4], static::FIELDS[5], static::PLACEHOLDERS[0], static::PLACEHOLDERS[1],
            static::PLACEHOLDERS[2], static::PLACEHOLDERS[3], static::PLACEHOLDERS[4], static::PLACEHOLDERS[5]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idCategory, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $title, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[3], $cover, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[4], $summary, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[5], $date, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * From an id, gets the associated book info.
     * @param int $id The id of the wished book's info.
     * @return null|string The json response if the id match else null.
     */
    public function getBook(int $id)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::FIELDS[6], static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * From a category id, gets the associated books info.
     * @param int $idCategory The id of the wished book's info.
     * @return null|string The json response if the id match else null.
     */
    public function getCategoryBooks(int $idCategory)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::FIELDS[5],
            static::FIELDS[6], static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idCategory, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Queries a book id from a book title and a book date.
     * @param string $title The title of the book.
     * @param string $date The written date of the book.
     * @return null|string The json response if the parameters match else null.
     */
    public function getBookId(string $title, string $date)
    {
        $statement = sprintf("SELECT %s FROM %s WHERE %s = %s AND %s = %s AND deleted = 0",
            static::FIELDS[0], static::TABLE, static::FIELDS[2], static::PLACEHOLDERS[2], static::FIELDS[5],
            static::PLACEHOLDERS[5]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[2], $title, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[5], $date, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Updates a book's info by its id.
     * @param int $id The id of the book to update.
     * @param int $idCategory The id of the category to set.
     * @param string $title The title to set.
     * @param string $cover The url of the cover to set.
     * @param string $summary The summary to set.
     * @param string $date The date to set.
     * @return true|false True if success else false.
     */
    public function update(int $id, int $idCategory, string $title, string $cover, string $summary, string $date)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = %s, %s = %s, %s = %s, %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[2],
            static::PLACEHOLDERS[2], static::FIELDS[3], static::PLACEHOLDERS[3], static::FIELDS[4],
            static::PLACEHOLDERS[4], static::FIELDS[5], static::PLACEHOLDERS[5], static::FIELDS[6],
            static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idCategory, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $title, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[3], $cover, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[4], $summary, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[5], $date, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * Soft deletes a book from its id.
     * @param int $id The id of the book to soft delete.
     * @return true|false True if success else false.
     */
    public function softDelete(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[6], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From an id given in parameter, restore the associated soft deleted book.
     * @param int $id The id of the book to restore.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeleted(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Deletes a book by the associated id given in parameter.
     * @param int $id The id of the book to delete.
     * @return true|false True if success else false.
     */
    public function delete(int $id)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Query all books from database.
     * @return null|string The json response if found else null.
     */
    public function queryAll()
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::FIELDS[6], static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all books from database with pagination (numbers parameters included).
     * @param int $start The start index of the results to get.
     * @param int $end The last index of the results to get.
     * @return null|string The json response if found else null.
     */
    public function queryAllPaginated(int $start, int $end) {
        $offsetPlaceholder = ":startResult";
        $limitPlaceholder = ":endResult";
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE deleted = 0 LIMIT %s OFFSET %s",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::FIELDS[6], static::TABLE, $limitPlaceholder, $offsetPlaceholder);
        $req = $this->db->prepare($statement);

        $req->bindValue($offsetPlaceholder, $start, PDO::PARAM_INT);
        $req->bindValue($limitPlaceholder, $end, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Counts the number of entities in the database.
     * @return null|string The json response if found else null.
     */
    public function count() {
        $statement = sprintf("SELECT COUNT(*) as %s FROM %s WHERE deleted = 0", static::COUNT, static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Get the entries greater than the id value given in parameter.
     * @param int $id The id which query the entries above.
     * @return null|string The json response if found else null.
     */
    public function queryAbove(int $id) {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s > %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::FIELDS[6], static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Get the entries that has been updated after than the date value given in parameter.
     * @param string $date The date to query entities.
     * @return null|string The json response if found else null.
     */
    public function queryNewer(string $date) {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s > %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::FIELDS[6], static::TABLE, static::FIELDS[6], static::PLACEHOLDERS[6]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[6], $date, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all book id and last_update fields from database in order to use it to determine which entities to update.
     * @return null|string The json response if found else null.
     */
    public function queryUpdateFields()
    {
        $statement = sprintf("SELECT %s, %s FROM %s WHERE deleted = 0 ORDER BY %s",
            static::FIELDS[0], static::FIELDS[6], static::TABLE, static::FIELDS[0]);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Deletes all the test entities (id inferior to 0).
     */
    public function deleteTestEntities() {
        $statement = sprintf("DELETE FROM %s WHERE %s < 0", static::TABLE, static::FIELDS[0]);

        $this->db->exec($statement);
    }
}