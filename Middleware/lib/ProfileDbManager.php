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
    public const FIELDS = ["id_profile", "avatar", "description", "last_update", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    public const PLACEHOLDERS = [":idP", ":avatar", ":description", "update"];

    /**
     * Stores the associated table name.
     */
    public const TABLE = "Profile";

    /**
     * Creates a profile in the database.
     * @param string $avatar The string representing the user's avatar.
     * @param string $description The profile's description.
     * @return true|false True if success else false.
     */
    public function create(string $avatar, string $description)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s) VALUES(%s, %s)",
            static::TABLE, static::FIELDS[1], static::FIELDS[2], static::PLACEHOLDERS[1], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $avatar, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[2], $description, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * Gets the profile associated to the id given in parameter.
     * @param int $id The id of the profile to get.
     * @return null|string The json response if exists else null.
     */
    public function getProfile(int $id)
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
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
     * @return true|false True if success else false.
     */
    public function update(int $id, string $avatar, string $description)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[2], static::PLACEHOLDERS[2],
            static::FIELDS[3], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $avatar, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[2], $description, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * From an id, updates the associated profile's avatar.
     * @param int $id The id of the profile to update.
     * @param string $avatar The new avatar to set.
     * @return true|false True if success else false.
     */
    public function updateAvatar(int $id, string $avatar)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s", static::TABLE,
            static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[3], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $avatar, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * From an id, updates the associated profile's description.
     * @param int $id The id of the profile to update.
     * @param string $description The new description to set.
     * @return true|false True if success else false.
     */
    public function updateDescription(int $id, string $description)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s", static::TABLE,
            static::FIELDS[2], static::PLACEHOLDERS[2], static::FIELDS[3], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $description, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * From an id given in parameter, soft delete the associated profile.
     * @param int $id The id of the profile to delete.
     * @return true|false True if success else false.
     */
    public function softDelete(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[3], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From an id given in parameter, restore the associated soft deleted profile.
     * @param int $id The id of the profile to restore.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeleted(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[3], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Deletes a profile from the database.
     * @param int $id The id of the profile to delete.
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
     * Query all profiles from database.
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
     * Query all profiles from database with pagination (number parameters included).
     * @param int $start The start index of the results to get.
     * @param int $end The last index of the results to get.
     * @return null|string The json response if found else null.
     */
    public function queryAllPaginated(int $start, int $end)
    {
        $offsetPlaceholder = ":startResult";
        $limitPlaceholder = ":endResult";
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE deleted = 0 LIMIT %s OFFSET %s",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::TABLE, $limitPlaceholder,
            $offsetPlaceholder);
        $req = $this->db->prepare($statement);

        $req->bindValue($offsetPlaceholder, $start, PDO::PARAM_INT);
        $req->bindValue($limitPlaceholder, $end, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Counts the number of entities in the database.
     * @return null|string The json response if found else null.
     */
    public function count() {
        $statement = sprintf("SELECT COUNT(*) as %s FROM %s WHERE deleted = 0", static::COUNT, static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Get the entries greater than the id value given in parameter.
     * @param int $id The id which query the entries above.
     * @return null|string The json response if found else null.
     */
    public function queryAbove(int $id) {
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE %s > %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::TABLE, static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Get the entries that has been updated after than the date value given in parameter.
     * @param string $date The date to query entities.
     * @return null|string The json response if found else null.
     */
    public function queryNewer(string $date) {
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE %s > %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::TABLE, static::FIELDS[3],
            static::PLACEHOLDERS[3]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[3], $date, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Query all profile id and last_update fields from database in order to use it to determine which entities to update.
     * @return null|string The json response if found else null.
     */
    public function queryUpdateFields()
    {
        $statement = sprintf("SELECT %s, %s FROM %s",
            static::FIELDS[0], static::FIELDS[3], static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }
}