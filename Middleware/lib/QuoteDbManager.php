<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:07
 */

require_once "DbManager.php";

/**
 * DAO Class QuoteDbManager, used to manage categories in database (CRUD).
 */
class QuoteDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    const FIELDS = ["id_quote", "id_user", "id_book", "quote", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    const PLACEHOLDERS = [":idQ", ":idU", ":idB", ":quote"];

    /**
     * Stores the associated table name.
     */
    const TABLE = "Quote";

    /**
     * Creates a quote into the database.
     * @param int $idUser The id of the user who owns the quote list.
     * @param int $idBook The id of the book.
     * @param string $quote The quote to set.
     * @return true|false True if success else false.
     */
    public function create(int $idUser, int $idBook, string $quote)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s, %s) VALUES(%s, %s, %s)",
            static::TABLE, static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::PLACEHOLDERS[1],
            static::PLACEHOLDERS[2], static::PLACEHOLDERS[3]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[3], $quote, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * Gets a quote from its id.
     * @param int $id The id of the quote.
     * @return null|string The json response if exists else null.
     */
    public function getQuote(int $id)
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::TABLE, static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Gets the list of quotes written by a specific user.
     * @param int $idUser The id of the user.
     * @return null|string The json response if exists else null.
     */
    public function getUserQuotes(int $idUser)
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[2], static::FIELDS[3], static::TABLE, static::FIELDS[1],
            static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idUser, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Gets the list of quotes from a specific book.
     * @param int $idBook The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getBookQuotes(int $idBook)
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[3], static::TABLE, static::FIELDS[2],
            static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Gets the list of quotes written by a specific user for a specific book.
     * @param int $idUser The id of the user.
     * @param int $idBook The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getUserBookQuotes(int $idUser, int $idBook)
    {
        $statement = sprintf("SELECT %s, %s FROM %s WHERE %s = %s AND %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[3], static::TABLE, static::FIELDS[1],
            static::PLACEHOLDERS[1], static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * From an id, updates the quote's text.
     * @param int $id The id of the quote to update.
     * @param string $quote The new text to set.
     * @return true|false True if success else false.
     */
    public function update(int $id, string $quote)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s", static::TABLE,
            static::FIELDS[3], static::PLACEHOLDERS[3], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[3], $quote, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * From an id given in parameter, soft delete the associated quote.
     * @param int $id The id of the quote to delete.
     * @return true|false True if success else false.
     */
    public function softDelete(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From an id given in parameter, restore the associated soft deleted quote.
     * @param int $id The id of the quote to restore.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeleted(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0 WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a user id given in parameter, soft delete the associated quotes.
     * @param int $idUser The id of user.
     * @return true|false True if success else false.
     */
    public function softDeleteUserQuotes(int $idUser)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idUser, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a user id given in parameter, restore the associated soft deleted quotes.
     * @param int $idUser The id of the user.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedUserQuotes(int $idUser)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0 WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idUser, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a book id given in parameter, soft delete the associated quotes.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function softDeleteBooksQuotes(int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s",
            static::TABLE, static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a book id given in parameter, restore the associated soft deleted quotes.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedBookQuotes(int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0 WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Deletes a quote from the database.
     * @param int $id The id of the quote to delete.
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
     * Deletes all quotes associated to a specific user from the database.
     * @param int $idUser The id of user.
     * @return true|false True if success else false.
     */
    public function deleteUserQuotes(int $idUser)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idUser, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Deletes all quotes associated to a specific book from the database.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function deleteBookQuotes(int $idBook)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Query all quotes from database.
     * @return null|string The json response if found else null.
     */
    public function queryAll()
    {
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Query all quotes from database with pagination (number parameters included).
     * @param int $start The start index of the results to get.
     * @param int $end The last index of the results to get.
     * @return null|string The json response if found else null.
     */
    public function queryAllPaginated(int $start, int $end)
    {
        $offsetPlaceholder = ":startResult";
        $limitPlaceholder = ":endResult";
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE deleted = 0 LIMIT %s OFFSET %s",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::TABLE,
            $limitPlaceholder, $offsetPlaceholder);
        $req = $this->db->prepare($statement);

        $req->bindValue($offsetPlaceholder, $start, PDO::PARAM_INT);
        $req->bindValue($limitPlaceholder, $end, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }
}