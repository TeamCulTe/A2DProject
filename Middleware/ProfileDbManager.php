<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:07
 */

require_once "DbManager.php";

/**
 * DAO Class ProfileDbManager, used to manage categories in database (CRUD).
 */
class ProfileDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    const FIELDS = ["id_profile", "avatar", "description", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    const PLACEHOLDERS = [":idP", ":avatar", ":description"];

    /**
     * Stores the associated table name.
     */
    const TABLE = "Profile";

    /**
     * Creates a profile in the database.
     * @param string $avatar The string representing the user's avatar.
     * @param string $description The profile's description.
     */
    public function create(string $avatar, string $description)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s) VALUES(%s, %s)",
            static::TABLE, static::FIELDS[1], static::FIELDS[2], static::PLACEHOLDERS[1], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $avatar, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[2], $description, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * Gets the profile associated to the id given in parameter.
     * @param int $id The id of the profile to get.
     * @return null|string The json response if exists else null.
     */
    public function getProfile(int $id)
    {
        $statement = sprintf("SELECT %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[1], static::FIELDS[2], static::TABLE, static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * From an id, updates the associated profile's values.
     * @param int $id The id of the profile to update.
     * @param string $avatar The new avatar to set.
     * @param string $description The new description to set.
     */
    public function update(int $id, string $avatar, string $description)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = %s WHERE %s = %s", static::TABLE,
            static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[2], static::PLACEHOLDERS[2], static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $avatar, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[2], $description, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * From an id, updates the associated profile's avatar.
     * @param int $id The id of the profile to update.
     * @param string $avatar The new avatar to set.
     */
    public function updateAvatar(int $id, string $avatar)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s", static::TABLE,
            static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $avatar, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * From an id, updates the associated profile's description.
     * @param int $id The id of the profile to update.
     * @param string $description The new description to set.
     */
    public function updateDescription(int $id, string $description)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s", static::TABLE,
            static::FIELDS[2], static::PLACEHOLDERS[2], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $description, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * From an id given in parameter, soft delete the associated profile.
     * @param int $id The id of the profile to delete.
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
     * Deletes a profile from the database.
     * @param int $id The id of the profile to delete.
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