<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:07
 */

require_once "DbManager.php";

/**
 * DAO Class BookListDbManager, used to manage book lists in database (CRUD).
 */
class BookListDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    const FIELDS = ["id_user", "id_book_list_type", "id_book", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    const PLACEHOLDERS = [":idU", ":idBLT", ":idB"];

    /**
     * Stores the associated table name.
     */
    const TABLE = "BookList";

    /**
     * Creates a book list into the database.
     * @param int $idUser The id of the user who owns the book list.
     * @param int $idBookListType The id of the type of book list (to read, reading, red).
     * @param int $idBook The id of the associated book.
     * @return true|false True if success else false.
     */
    public function create(int $idUser, int $idBookListType, int $idBook)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s, %s) VALUES(%s, %s, %s)",
            static::TABLE, static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::PLACEHOLDERS[0],
            static::PLACEHOLDERS[1], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBookListType, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Gets the list of books contained in a specific type of book list from a specific user.
     * @param int $idBookListType The id of the wished book list type.
     * @param int $idUser The id of the user who owns the list.
     * @return null|string The json response if exists else null.
     */
    public function getBookList(int $idUser, int $idBookListType)
    {
        $statement = sprintf("SELECT %s FROM %s WHERE %s = %s AND %s = %s AND deleted = 0",
            static::FIELDS[2], static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0],
            static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBookListType, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * From an user id, gets its associated book lists.
     * @param int $idUser The id of the user.
     * @return null|string The json response if exists else null.
     */
    public function getUserBookLists(int $idUser)
    {
        $statement = sprintf("SELECT %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[1], static::FIELDS[2], static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Soft delete all book lists from a specific user.
     * @param int $idUser The id of the user.
     * @return true|false True if success else false.
     */
    public function softDeleteUserBookLists(int $idUser)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Restore all soft deleted book lists from a specific user.
     * @param int $idUser The id of the user.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedUserBookLists(int $idUser)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0 WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Soft delete book lists containing a specific book.
     * @param int $idBook The id of the user.
     * @return true|false True if success else false.
     */
    public function softDeleteBookBookLists(int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s",
            static::TABLE, static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Restore all soft deleted book lists containing a specific book.
     * @param int $idBook The id of the user.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedBookBookLists(int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0 WHERE %s = %s",
            static::TABLE, static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Soft delete a book lists from a specific type of book list.
     * @param int $idBookListType The id of the user.
     * @return true|false True if success else false.
     */
    public function softDeleteTypedBookLists(int $idBookListType)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBookListType, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Restore all soft deleted book lists from a specific type of book list.
     * @param int $idBookListType The id of the user.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedTypedBookLists(int $idBookListType)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0 WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBookListType, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Soft delete a book from a book lists owned by specific user.
     * @param int $idUser The id of the user.
     * @param int $idBook The id of the book to delete.
     * @return true|false True if success else false.
     */
    public function softDeleteUserBookListBook(int $idUser, int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s AND %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Restore a soft deleted book from a book lists owned by specific user.
     * @param int $idUser The id of the user.
     * @param int $idBook The id of the book to delete.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedUserBookListBook(int $idUser, int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0 WHERE %s = %s AND %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Soft delete a book from a specific type of book lists owned by specific user.
     * @param int $idUser The id of the user.
     * @param int $idBookListType The id of the type of the book list.
     * @param int $idBook The id of the book to delete.
     * @return true|false True if success else false.
     */
    public function softDeleteBookList(int $idUser, int $idBookListType, int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s AND %s = %s AND %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1],
            static::PLACEHOLDERS[1], static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBookListType, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Restore a soft deleted book from a specific type of book lists owned by specific user.
     * @param int $idUser The id of the user.
     * @param int $idBookListType The id of the type of the book list.
     * @param int $idBook The id of the book to delete.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedBookList(int $idUser, int $idBookListType, int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0 WHERE %s = %s AND %s = %s AND %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1],
            static::PLACEHOLDERS[1], static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBookListType, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Delete all book lists from a specific user.
     * @param int $idUser The id of the user.
     * @return true|false True if success else false.
     */
    public function deleteUserBookLists(int $idUser)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Delete all book lists containing a specific book.
     * @param int $idBook The id of the user.
     * @return true|false True if success else false.
     */
    public function deleteBookBookLists(int $idBook)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Delete all book lists from a specific type of book list.
     * @param int $idBookListType The id of the user.
     * @return true|false True if success else false.
     */
    public function deleteTypedBookLists(int $idBookListType)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBookListType, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Delete a book from a book lists owned by specific user.
     * @param int $idUser The id of the user.
     * @param int $idBook The id of the book to delete.
     * @return true|false True if success else false.
     */
    public function deleteUserBookListBook(int $idUser, int $idBook)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s AND %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Delete a book from a specific type odf book lists owned by specific user.
     * @param int $idUser The id of the user.
     * @param int $idBookListType The id of the type of the book list.
     * @param int $idBook The id of the book to delete.
     * @return true|false True if success else false.
     */
    public function deleteBookList(int $idUser, int $idBookListType, int $idBook)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s AND %s = %s AND %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1],
            static::PLACEHOLDERS[1], static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBookListType, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Query all book lists from database.
     * @return null|string The json response if found else null.
     */
    public function queryAll()
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Query all book lists from database with pagination (number parameters included).
     * @param int $start The start index of the results to get.
     * @param int $end The last index of the results to get.
     * @return null|string The json response if found else null.
     */
    public function queryAllPaginated(int $start, int $end)
    {
        $offsetPlaceholder = ":startResult";
        $limitPlaceholder = ":endResult";
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE deleted = 0 LIMIT %s OFFSET %s",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::TABLE, $limitPlaceholder,
            $offsetPlaceholder);
        $req = $this->db->prepare($statement);

        $req->bindValue($offsetPlaceholder, $start, PDO::PARAM_INT);
        $req->bindValue($limitPlaceholder, $end, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }
}