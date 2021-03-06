<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:06
 */

require_once "DbManager.php";

/**
 * DAO Class CountryDbManager, used to manage categories in database (CRUD).
 */
class CountryDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    public const FIELDS = ["id_country", "name_country", "last_update", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    public const PLACEHOLDERS = [":idCo", ":nameCo", ":update"];

    /**
     * Stores the associated table name.
     */
    public const TABLE = "Country";

    /**
     * Creates a country in the database.
     * @param string $name The name of the country.
     * @return string the id of the created entity.
     */
    public function create(string $name)
    {
        $statement = sprintf("INSERT INTO %s(%s) VALUE(%s)",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $name, PDO::PARAM_STR);

        if ($req->execute()) {
            return $this->db->lastInsertId();
        } else {
            return "";
        }
    }

    /**
     * Creates a country in the database from a name given in parameter.
     * @param int $id The id of the country to create.
     * @param string $name The name of the country to create.
     * @return True if success else false.
     */
    public function fullCreate(int $id, string $name)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s) VALUE(%s, %s)",
            static::TABLE, static::FIELDS[0], static::FIELDS[1], static::PLACEHOLDERS[0], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $name, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * Gets the country associated to the id given in parameter.
     * @param int $id The id of the country to get.
     * @return null|string The json response if exists else null.
     */
    public function getCountry(int $id)
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::TABLE, static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Gets the country id associated to the name given in parameter.
     * @param string $name The name of the country to get.
     * @return null|string The json response if exists else null.
     */
    public function getCountryId(string $name)
    {
        $statement = sprintf("SELECT %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $name, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * From an id, updates the name of the associated country.
     * @param int $id The id of the country to update.
     * @param string $name The new name to set.
     * @return true|false True if success else false.
     */
    public function update(int $id, string $name)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s", static::TABLE,
            static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[2], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $name, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * From an id given in parameter, soft delete the associated country.
     * @param int $id The id of the country to delete.
     * @return true|false True if success else false.
     */
    public function softDelete(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From an id given in parameter, restore the associated soft deleted country.
     * @param int $id The id of the country to restore.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeleted(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[2], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Deletes a country from the database.
     * @param int $id The id of the country to delete.
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
     * Query all countries from database.
     * @return null|string The json response if found else null.
     */
    public function queryAll()
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all countries from database with pagination (number parameters included).
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
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s > %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
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
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s > %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::TABLE, static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[2], $date, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all country id and last_update fields from database in order to use it to determine which entities to update.
     * @return null|string The json response if found else null.
     */
    public function queryUpdateFields()
    {
        $statement = sprintf("SELECT %s, %s FROM %s WHERE deleted = 0 ORDER BY %s",
            static::FIELDS[0], static::FIELDS[2], static::TABLE, static::FIELDS[0]);
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