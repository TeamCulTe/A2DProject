<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:08
 */

/**
 * DAO Class BookDbManager, used to manage books in database (CRUD).
 */
class BookDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    const FIELDS = ["id_book", "id_category", "title", "cover", "summary", "date_published", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    const PLACE_HOLDERS = [":idB", ":idC", ":title", ":cover", ":summary", ":date"];

    /**
     * Stores the associated table name.
     */
    const TABLE = "Book";

    /**
     * Creates a book into the database.
     * @param int $idCategory The associated category id.
     * @param string $title The title of the book.
     * @param string $cover The url of the cover.
     * @param string $summary The book's summary text.
     * @param string $date The book's written date.
     */
    public function create(int $idCategory, string $title, string $cover, string $summary, string $date)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s, %s, %s, %s) VALUES (%s, %s, %s, %s, %s)",
            static::TABLE, static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::PLACE_HOLDERS[1], static::PLACE_HOLDERS[2], static::PLACE_HOLDERS[3],
            static::PLACE_HOLDERS[4],  static::PLACE_HOLDERS[5]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACE_HOLDERS[1], $idCategory, PDO::PARAM_INT);
        $req->bindValue(static::PLACE_HOLDERS[2], $title, PDO::PARAM_STR);
        $req->bindValue(static::PLACE_HOLDERS[3], $cover, PDO::PARAM_STR);
        $req->bindValue(static::PLACE_HOLDERS[4], $summary, PDO::PARAM_STR);
        $req->bindValue(static::PLACE_HOLDERS[5], $date, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * From an id, gets the associated book info.
     * @param int $id The id of the wished book's info.
     * @return null|string The json response if the id match else null.
     */
    public function getBook(int $id)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::FIELDS[5],
            static::TABLE, static::FIELDS[0], static::PLACE_HOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACE_HOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
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
            static::FIELDS[0],static::TABLE, static::FIELDS[1], static::PLACE_HOLDERS[1], static::FIELDS[5],
            static::PLACE_HOLDERS[5]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACE_HOLDERS[1], $title, PDO::PARAM_STR);
        $req->bindValue(static::PLACE_HOLDERS[5], $date, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Updates a book's info by its id.
     * @param int $id The id of the book to update.
     * @param int $idCategory The id of the category to set.
     * @param string $title The title to set.
     * @param string $cover The url of the cover to set.
     * @param string $summary The summary to set.
     * @param string $date The date to set.
     */
    public function update(int $id, int $idCategory, string $title, string $cover, string $summary, string $date)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = %s, %s = %s, %s = %s, %s = %s WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACE_HOLDERS[1], static::FIELDS[2],
            static::PLACE_HOLDERS[2], static::FIELDS[3], static::PLACE_HOLDERS[3], static::FIELDS[4],
            static::PLACE_HOLDERS[4], static::FIELDS[5], static::PLACE_HOLDERS[5], static::FIELDS[0],
            static::PLACE_HOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACE_HOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACE_HOLDERS[1], $idCategory, PDO::PARAM_STR);
        $req->bindValue(static::PLACE_HOLDERS[2], $title, PDO::PARAM_STR);
        $req->bindValue(static::PLACE_HOLDERS[3], $cover, PDO::PARAM_STR);
        $req->bindValue(static::PLACE_HOLDERS[4], $summary, PDO::PARAM_STR);
        $req->bindValue(static::PLACE_HOLDERS[5], $date, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * Soft deletes a book from its id.
     * @param int $id The id of the book to soft delete.
     */
    public function softDelete(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACE_HOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACE_HOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();
    }

    /**
     * Deletes a book by the associated id given in parameter.
     * @param int $id The id of the book to delete.
     */
    public function delete(int $id)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACE_HOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACE_HOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();
    }
}