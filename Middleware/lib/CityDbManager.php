<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:06
 */

require_once "DbManager.php";

/**
 * DAO Class CityDbManager, used to manage categories in database (CRUD).
 */
class CityDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    public const FIELDS = ["id_city", "name_city", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    public const PLACEHOLDERS = [":idCi", ":nameCi"];

    /**
     * Stores the associated table name.
     */
    public const TABLE = "City";

    /**
     * Creates a city in the database.
     * @param string $name The name of the city.
     * @return true|false True if success else false.
     */
    public function create(string $name)
    {
        $statement = sprintf("INSERT INTO %s(%s) VALUE(%s)",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $name, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * Gets the city associated to the id given in parameter.
     * @param int $id The id of the city to get.
     * @return null|string The json response if exists else null.
     */
    public function getCity(int $id)
    {
        $statement = sprintf("SELECT %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[1], static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Gets the city id associated to the name given in parameter.
     * @param string $name The name of the city to get.
     * @return null|string The json response if exists else null.
     */
    public function getCityId(string $name)
    {
        $statement = sprintf("SELECT %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $name, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * From an id, updates the name of the associated city.
     * @param int $id The id of the city to update.
     * @param string $name The new name to set.
     * @return true|false True if success else false.
     */
    public function update(int $id, string $name)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s", static::TABLE,
            static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $name, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * From an id given in parameter, soft delete the associated city.
     * @param int $id The id of the city to delete.
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
     * From an id given in parameter, restore the associated soft deleted city.
     * @param int $id The id of the city to restore.
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
     * Deletes a city from the database.
     * @param int $id The id of the city to delete.
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
     * Query all cities from database.
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
     * Query all cities from database with pagination (number parameters included).
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
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Counts the number of entities in the database.
     */
    public function count() {
        $statement = sprintf("SELECT COUNT(*) as %s FROM %s WHERE deleted = 0", static::COUNT, static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }
}