<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:07
 */

require_once "DbManager.php";

/**
 * DAO Class BookListTypeDbManager, used to manage book lists in database (CRUD).
 */
class BookListTypeDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    const FIELDS = ["id_book_list_type", "label", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    const PLACEHOLDERS = [":idBLT", ":label"];

    /**
     * Stores the associated table name.
     */
    const TABLE = "BookListType";

    /**
     * Creates a book list type in the database.
     * @param string $label The label of the book list type.
     */
    public function create(string $label)
    {
        $statement = sprintf("INSERT INTO %s(%s) VALUE(%s)",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $label, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * Gets the book list type associated to the id given in parameter.
     * @param int $id The id of the book list type to get.
     * @return null|string The json response if exists else null.
     */
    public function getBookListType(int $id)
    {
        $statement = sprintf("SELECT %s FROM %s WHERE %s = %s",
            static::FIELDS[1], static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Gets the book list type id associated to the label given in parameter.
     * @param string $label The label of the book list type to get.
     * @return null|string The json response if exists else null.
     */
    public function getBookListTypeId(string $label)
    {
        $statement = sprintf("SELECT %s FROM %s WHERE %s = %s",
            static::FIELDS[0], static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $label, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * From an id, updates the label of the associated book list.
     * @param int $id The id of the book list to update.
     * @param string $label The new label to set.
     */
    public function update(int $id, string $label)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s", static::TABLE,
            static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $label, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * From an id given in parameter, soft delete the associated the book list type.
     * @param int $id The id of the book list type to delete.
     */
    public function softDelete(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1 WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();
    }

    /**
     * Deletes a book list type from the database.
     * @param int $id The id of the book list type to delete.
     */
    public function delete(int $id)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();
    }
}