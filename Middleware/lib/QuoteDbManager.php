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
    public const FIELDS = ["id_quote", "id_user", "id_book", "quote", "last_update", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    public const PLACEHOLDERS = [":idQ", ":idU", ":idB", ":quote", ":update"];

    /**
     * Stores the associated table name.
     */
    public const TABLE = "Quote";

    /**
     * Creates a quote into the database.
     * @param int $idUser The id of the user who owns the quote list.
     * @param int $idBook The id of the book.
     * @param string $quote The quote to set.
     * @return string the id of the created entity.
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

        if ($req->execute()) {
            return $this->db->lastInsertId();
        } else {
            return "";
        }
    }

    /**
     * Creates a quote into the database.
     * @param int $id The id of the quote to create.
     * @param int $idUser The id of the user who owns the quote list.
     * @param int $idBook The id of the book.
     * @param string $quote The quote to set.
     * @return string the id of the created entity.
     */
    public function fullCreate(int $id, int $idUser, int $idBook, string $quote)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s, %s, %s) VALUES(%s, %s, %s, %s)",
            static::TABLE, static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3],
            static::PLACEHOLDERS[0], static::PLACEHOLDERS[1], static::PLACEHOLDERS[2], static::PLACEHOLDERS[3]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
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
        $statement = sprintf("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Gets the list of quotes written by a specific user.
     * @param int $idUser The id of the user.
     * @return null|string The json response if exists else null.
     */
    public function getUserQuotes(int $idUser)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::TABLE, static::FIELDS[1],
            static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idUser, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Gets the list of quotes from a specific book.
     * @param int $idBook The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getBookQuotes(int $idBook)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[3], static::FIELDS[4], static::TABLE, static::FIELDS[2],
            static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Gets the list of quotes written by a specific user for a specific book.
     * @param int $idUser The id of the user.
     * @param int $idBook The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getUserBookQuotes(int $idUser, int $idBook)
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s = %s AND %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[3], static::FIELDS[4], static::TABLE, static::FIELDS[1],
            static::PLACEHOLDERS[1], static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * From an id, updates the quote's text.
     * @param int $id The id of the quote to update.
     * @param string $quote The new text to set.
     * @return true|false True if success else false.
     */
    public function update(int $id, string $quote)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s", static::TABLE,
            static::FIELDS[3], static::PLACEHOLDERS[3], static::FIELDS[4], static::FIELDS[0], static::PLACEHOLDERS[0]);
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
        $statement = sprintf("UPDATE %s SET deleted = 1, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[0], static::PLACEHOLDERS[0]);
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
        $statement = sprintf("UPDATE %s SET deleted = 0, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[0], static::PLACEHOLDERS[0]);
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
        $statement = sprintf("UPDATE %s SET deleted = 1, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[1], static::PLACEHOLDERS[1]);
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
        $statement = sprintf("UPDATE %s SET deleted = 0, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[1], static::PLACEHOLDERS[1]);
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
        $statement = sprintf("UPDATE %s SET deleted = 1, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[2], static::PLACEHOLDERS[2]);
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
        $statement = sprintf("UPDATE %s SET deleted = 0, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[1], static::PLACEHOLDERS[1]);
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
        $statement = sprintf("SELECT %s, %s, %s, %s, %s FROM %s WHERE deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
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
        $statement = sprintf("SELECT %s, %s, %s, %s, %s FROM %s WHERE deleted = 0 LIMIT %s OFFSET %s",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::TABLE,
            $limitPlaceholder, $offsetPlaceholder);
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
        $statement = sprintf("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s > %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::TABLE,
            static::FIELDS[0], static::PLACEHOLDERS[0]);
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
        $statement = sprintf("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s > %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::TABLE,
            static::FIELDS[4], static::PLACEHOLDERS[4]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[4], $date, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all quote id and last_update fields from database in order to use it to determine which entities to update.
     * @return null|string The json response if found else null.
     */
    public function queryUpdateFields()
    {
        $statement = sprintf("SELECT %s, %s FROM %s WHERE deleted = 0 ORDER BY %s",
            static::FIELDS[0], static::FIELDS[4], static::TABLE, static::FIELDS[0]);
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