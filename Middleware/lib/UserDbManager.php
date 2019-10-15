<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:06
 */

require_once "DbManager.php";

/**
 * DAO Class UserDbManager, used to manage categories in database (CRUD).
 */
class UserDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    public const FIELDS = ["id_user", "pseudo", "password", "email", "id_profile", "id_city", "id_country", "last_update", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    public const PLACEHOLDERS = [":idU", ":pseudo", ":password", ":email", ":idP", ":idCi", ":idCo", ":update"];

    /**
     * Stores the associated table name.
     */
    public const TABLE = "User";

    /**
     * Creates a user into the database.
     * @param string $pseudo The pseudo of the user.
     * @param string $password The password of the user.
     * @param string $email The user's email address.
     * @param int $idProfile The id of user's profile.
     * @param int $idCity The id of user's city.
     * @param int $idCountry The id of user's country.
     * @return string the id of the created entity.
     */
    public function create(string $pseudo, string $password, string $email, int $idProfile, int $idCity, int $idCountry)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s, %s, %s, %s, %s) VALUES (%s, %s, %s, %s, %s, %s)",
            static::TABLE, static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::FIELDS[6], static::PLACEHOLDERS[1], static::PLACEHOLDERS[2],
            static::PLACEHOLDERS[3], static::PLACEHOLDERS[4], static::PLACEHOLDERS[5], static::PLACEHOLDERS[6]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[2], password_hash($password, PASSWORD_BCRYPT), PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[4], $idProfile, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[5], $idCity, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[6], $idCountry, PDO::PARAM_INT);

        if ($req->execute()) {
            return $this->db->lastInsertId();
        } else {
            return "";
        }
    }

    /**
     * Creates a user into the database.
     * @param int $id The id of the user to create.
     * @param string $pseudo The pseudo of the user.
     * @param string $password The password of the user.
     * @param string $email The user's email address.
     * @param int $idProfile The id of user's profile.
     * @param int $idCity The id of user's city.
     * @param int $idCountry The id of user's country.
     * @return string the id of the created entity.
     */
    public function fullCreate(int $id, string $pseudo, string $password, string $email, int $idProfile, int $idCity, int $idCountry)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s, %s, %s, %s, %s, %s) VALUES (%s, %s, %s, %s, %s, %s, %s)",
            static::TABLE, static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3],
            static::FIELDS[4], static::FIELDS[5], static::FIELDS[6], static::PLACEHOLDERS[0], static::PLACEHOLDERS[1],
            static::PLACEHOLDERS[2], static::PLACEHOLDERS[3], static::PLACEHOLDERS[4], static::PLACEHOLDERS[5],
            static::PLACEHOLDERS[6]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[2], password_hash($password, PASSWORD_BCRYPT), PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[4], $idProfile, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[5], $idCity, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[6], $idCountry, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From an id, gets the associated user info.
     * @param int $id The id of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getUser(int $id)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::FIELDS[6], static::FIELDS[7], static::TABLE, static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * From a pseudo, gets the associated user info.
     * @param string $pseudo The pseudo of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getUserFromPseudo(string $pseudo)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::FIELDS[5],
            static::FIELDS[6], static::FIELDS[7], static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * From an email address, gets the associated user info.
     * @param string $email The email of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getUserFromEmail(string $email)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[4], static::FIELDS[5],
            static::FIELDS[6], static::FIELDS[7], static::TABLE, static::FIELDS[3], static::PLACEHOLDERS[3]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * From a pseudo, gets the associated user info except for the password.
     * @param string $pseudo The pseudo of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getPublicUserFromPseudo(string $pseudo)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[4], static::FIELDS[5], static::FIELDS[6],
            static::FIELDS[7], static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * From an email address, gets the associated user info except for the password.
     * @param string $email The email of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getPublicUserFromEmail(string $email)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[4], static::FIELDS[5], static::FIELDS[6],
            static::FIELDS[7], static::TABLE, static::FIELDS[3], static::PLACEHOLDERS[3]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * From an email address, gets the associated user info.
     * @param string $email The email of the wished user's info.
     * @param string $password The password of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getUserFromAuth(string $email, string $password)
    {
        if (password_verify($password, $this->getUserPassword($email)))
        {
            $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
                static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
                static::FIELDS[5], static::FIELDS[6], static::FIELDS[7], static::TABLE, static::FIELDS[3],
                static::PLACEHOLDERS[3]);
            $req = $this->db->prepare($statement);

            $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
            $req->execute();

            $response = $req->fetchAll(PDO::FETCH_ASSOC);

            return json_encode($response);
        }

        return null;
    }

    /**
     * Queries a user id from a pseudo.
     * @param string $pseudo The pseudo of the user.
     * @return null|string The json response if the parameters match else null.
     */
    public function getUserId(string $pseudo)
    {
        $statement = sprintf("SELECT %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Updates a user's info by its id.
     * @param int $id The id of the user to update.
     * @param string $pseudo The pseudo to set.
     * @param string $password The password to set.
     * @param string $email The email address to set.
     * @param int $idProfile The id of the profile to set.
     * @param int $idCity The id of the city to set.
     * @param int $idCountry The id of the country to set.
     * @return true|false True if success else false.
     */
    public function update(int $id, string $pseudo, string $password, string $email, int $idProfile, int $idCity,
                           int $idCountry)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = %s, %s = %s, %s = %s, %s = %s, %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[2],
            static::PLACEHOLDERS[2], static::FIELDS[3], static::PLACEHOLDERS[3], static::FIELDS[4],
            static::PLACEHOLDERS[4], static::FIELDS[5], static::PLACEHOLDERS[5], static::FIELDS[6],
            static::PLACEHOLDERS[6], static::FIELDS[7], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[2], password_hash($password, PASSWORD_BCRYPT), PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[4], $idProfile, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[5], $idCity, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[6], $idCountry, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Updates a user's pseudo by its id.
     * @param int $id The id of the user to update.
     * @param string $pseudo The pseudo to set.
     * @return true|false True if success else false.
     */
    public function updatePseudo(int $id, string $pseudo)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[7], static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * Updates a user's password by its id.
     * @param int $id The id of the user to update.
     * @param string $password The password to set.
     * @return true|false True if success else false.
     */
    public function updatePassword(int $id, string $password)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[2], static::PLACEHOLDERS[2], static::FIELDS[7], static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], password_hash($password, PASSWORD_BCRYPT), PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * Updates a user's email by its id.
     * @param int $id The id of the user to update.
     * @param string $email The email to set.
     * @return true|false True if success else false.
     */
    public function updateEmail(int $id, string $email)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[3], static::PLACEHOLDERS[3], static::FIELDS[7], static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * Updates a user's city by its id.
     * @param int $id The id of the user to update.
     * @param int $idCity The id of the city to set.
     * @return true|false True if success else false.
     */
    public function updateCity(int $id, int $idCity)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[5], static::PLACEHOLDERS[5], static::FIELDS[7], static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[5], $idCity, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Updates a user's country by its id.
     * @param int $id The id of the user to update.
     * @param int $idCountry The id of the city to set.
     * @return true|false True if success else false.
     */
    public function updateCountry(int $id, int $idCountry)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[6], static::PLACEHOLDERS[6], static::FIELDS[7], static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[6], $idCountry, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Soft deletes a user from its id.
     * @param int $id The id of the user to soft delete.
     * @return true|false True if success else false.
     */
    public function softDelete(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[7], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Soft deletes a user from its id.
     * @param string $email The email of the user to soft delete.
     * @param string $password The password of the user to soft delete.
     * @return true|false True if success else false.
     */
    public function softDeleteFromAuth(string $email, string $password)
    {
        if (password_verify($password, $this->getUserPassword($email)))
        {
            $statement = sprintf("UPDATE %s SET deleted = 1, %s = CURRENT_TIMESTAMP WHERE %s = %s",
                static::TABLE, static::FIELDS[7], static::FIELDS[3], static::PLACEHOLDERS[3]);
            $req = $this->db->prepare($statement);

            $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);

            return $req->execute();
        }

        return false;
    }

    /**
     * From an id given in parameter, restore the associated soft deleted user.
     * @param int $id The id of the user to restore.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeleted(int $id)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[7], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From an email and a password given in parameter, restore the associated soft deleted user.
     * @param string $email The email of the user to restore.
     * @param string $password The password of the user to restore.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedFromAuth(string $email, string $password)
    {
        if (password_verify($password, $this->getUserPassword($email))) {
            $statement = sprintf("UPDATE %s SET deleted = 0, %s = CURRENT_TIMESTAMP WHERE %s = %s",
                static::TABLE, static::FIELDS[7], static::FIELDS[3], static::PLACEHOLDERS[3]);
            $req = $this->db->prepare($statement);

            $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);

            return $req->execute();
        }

        return false;
    }

    /**
     * Deletes a user by the associated id given in parameter.
     * @param int $id The id of the user to delete.
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
     * Deletes a user by the associated id given in parameter.
     * @param string $email The email of the user to delete.
     * @param string $password The password of the user to delete.
     * @return true|false True if success else false.
     */
    public function deleteFromAuth(string $email, string $password)
    {
        if (password_verify($password, $this->getUserPassword($email))) {
            $statement = sprintf("DELETE FROM %s WHERE %s = %s AND %s = %s",
                static::TABLE, static::FIELDS[3], static::PLACEHOLDERS[3]);
            $req = $this->db->prepare($statement);

            $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);

            return $req->execute();
        }

        return false;
    }

    /**
     * Query all users from database.
     * @return null|string The json response if found else null.
     */
    public function queryAll()
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::FIELDS[6], static::FIELDS[7], static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all public users from database (without sensible information).
     * @return null|string The json response if found else null.
     */
    public function queryAllPublic()
    {
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[4], static::FIELDS[7], static::TABLE);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all users from database with pagination (numbers parameters included).
     * @param int $start The start index of the results to get.
     * @param int $end The last index of the results to get.
     * @return null|string The json response if found else null.
     */
    public function queryAllPaginated(int $start, int $end)
    {
        $offsetPlaceholder = ":startResult";
        $limitPlaceholder = ":endResult";
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE deleted = 0 LIMIT %s OFFSET %s",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::FIELDS[6], static::FIELDS[7], static::TABLE, $limitPlaceholder, $offsetPlaceholder);
        $req = $this->db->prepare($statement);

        $req->bindValue($offsetPlaceholder, $start, PDO::PARAM_INT);
        $req->bindValue($limitPlaceholder, $end, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all public users from database with pagination (numbers parameters included).
     * @param int $start The start index of the results to get.
     * @param int $end The last index of the results to get.
     * @return null|string The json response if found else null.
     */
    public function queryAllPublicPaginated(int $start, int $end)
    {
        $offsetPlaceholder = ":startResult";
        $limitPlaceholder = ":endResult";
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE deleted = 0 LIMIT %s OFFSET %s",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[4], static::FIELDS[7], static::TABLE, $limitPlaceholder,
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
     * Get the public entries greater than the id value given in parameter.
     * @param int $id The id which query the entries above.
     * @return null|string The json response if found else null.
     */
    public function queryAbove(int $id) {
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE %s > %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[4], static::FIELDS[7], static::TABLE, static::FIELDS[0],
            static::PLACEHOLDERS[0]);
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
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE %s > %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[4], static::FIELDS[7], static::TABLE, static::FIELDS[7],
            static::PLACEHOLDERS[7]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[7], $date, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all user id and last_update fields from database in order to use it to determine which entities to update.
     * @return null|string The json response if found else null.
     */
    public function queryUpdateFields()
    {
        $statement = sprintf("SELECT %s, %s FROM %s WHERE deleted = 0 ORDER BY %s",
            static::FIELDS[0], static::FIELDS[7], static::TABLE, static::FIELDS[0]);
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

    /**
     * From an email, gets the associated hashed password in the db.
     * @param string $email The user's email.
     * @return null|string if not match returns null else, the password.
     */
    public function getUserPassword(string $email) {
        $statement = sprintf("SELECT %s FROM %s WHERE %s = %s", static::FIELDS[2], static::TABLE, static::FIELDS[3],
            static::PLACEHOLDERS[3]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? $response[0][static::FIELDS[2]] : null;
    }
}