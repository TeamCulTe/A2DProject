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
    const FIELDS = ["id_user", "pseudo", "password", "email", "id_profile", "id_city", "id_country", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    const PLACEHOLDERS = [":idU", ":pseudo", ":password", ":email", ":idP", ":idCi", ":idCo"];

    /**
     * Stores the associated table name.
     */
    const TABLE = "User";

    /**
     * Creates a user into the database.
     * @param string $pseudo The pseudo of the user.
     * @param string $password The password of the user.
     * @param string $email The user's email address.
     * @param int $idProfile The id of user's profile.
     * @param int $idCity The id of user's city.
     * @param int $idCountry The id of user's country.
     */
    public function create(string $pseudo, string $password, string $email, int $idProfile, int $idCity, int $idCountry)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s, %s, %s, %s, %s) VALUES (%s, %s, %s, %s, %s, %s)",
            static::TABLE, static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::FIELDS[5], static::FIELDS[6], static::PLACEHOLDERS[1], static::PLACEHOLDERS[2],
            static::PLACEHOLDERS[3], static::PLACEHOLDERS[4], static::PLACEHOLDERS[5], static::PLACEHOLDERS[6]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $password, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[4], $idProfile, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[5], $idCity, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[6], $idCountry, PDO::PARAM_INT);
        $req->execute();
    }

    /**
     * From an id, gets the associated user info.
     * @param int $id The id of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getUser(int $id)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::FIELDS[5],
            static::FIELDS[6], static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * From a pseudo, gets the associated user info.
     * @param string $pseudo The pseudo of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getUserFromPseudo(string $pseudo)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::FIELDS[5],
            static::FIELDS[6], static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * From an email address, gets the associated user info.
     * @param string $email The email of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getUserFromEmail(string $email)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[4], static::FIELDS[5],
            static::FIELDS[6], static::TABLE, static::FIELDS[3], static::PLACEHOLDERS[3]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * From a pseudo, gets the associated user info except for the password.
     * @param string $pseudo The pseudo of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getPublicUserFromPseudo(string $pseudo)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[3], static::FIELDS[4], static::FIELDS[5], static::FIELDS[6],
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * From an email address, gets the associated user info except for the password.
     * @param string $email The email of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getPublicUserFromEmail(string $email)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[4], static::FIELDS[5], static::FIELDS[6],
            static::TABLE, static::FIELDS[3], static::PLACEHOLDERS[3]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * From an email address, gets the associated user info.
     * @param string $pseudo The pseudo of the wished user's info.
     * @param string $password The password of the wished user's info.
     * @return null|string The json response if the id match else null.
     */
    public function getUserFromAuth(string $pseudo, string $password)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND %s =%s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[3], static::FIELDS[4], static::FIELDS[5], static::FIELDS[6],
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[2], static::PLACEHOLDERS[2]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[2], $password, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Queries a user id from a pseudo.
     * @param string $pseudo The pseudo of the user.
     * @return null|string The json response if the parameters match else null.
     */
    public function getUserId(string $pseudo)
    {
        $statement = sprintf("SELECT %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0],static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
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
     */
    public function update(int $id, string $pseudo, string $password, string $email, int $idProfile, int $idCity,
                           int $idCountry)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = %s, %s = %s, %s = %s, %s = %s, %s = %s WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[2],
            static::PLACEHOLDERS[2], static::FIELDS[3], static::PLACEHOLDERS[3], static::FIELDS[4],
            static::PLACEHOLDERS[4], static::FIELDS[5], static::PLACEHOLDERS[5], static::FIELDS[6],
            static::PLACEHOLDERS[6], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[2], $password, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[4], $idProfile, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[5], $idCity, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[6], $idCountry, PDO::PARAM_INT);
        $req->execute();
    }

    /**
     * Updates a user's pseudo by its id.
     * @param int $id The id of the user to update.
     * @param string $pseudo The pseudo to set.
     */
    public function updatePseudo(int $id, string $pseudo)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $pseudo, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * Updates a user's password by its id.
     * @param int $id The id of the user to update.
     * @param string $password The password to set.
     */
    public function updatePassword(int $id, string $password)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s",
            static::TABLE, static::FIELDS[2], static::PLACEHOLDERS[2], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $password, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * Updates a user's email by its id.
     * @param int $id The id of the user to update.
     * @param string $email The email to set.
     */
    public function updateEmail(int $id, string $email)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s",
            static::TABLE, static::FIELDS[3], static::PLACEHOLDERS[3], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[3], $email, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * Updates a user's city by its id.
     * @param int $id The id of the user to update.
     * @param int $idCity The id of the city to set.
     */
    public function updateCity(int $id, int $idCity)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s",
            static::TABLE, static::FIELDS[5], static::PLACEHOLDERS[5], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[5], $idCity, PDO::PARAM_INT);
        $req->execute();
    }

    /**
     * Updates a user's country by its id.
     * @param int $id The id of the user to update.
     * @param int $idCountry The id of the city to set.
     */
    public function updateCountry(int $id, int $idCountry)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s",
            static::TABLE, static::FIELDS[6], static::PLACEHOLDERS[6], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $id, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[6], $idCountry, PDO::PARAM_INT);
        $req->execute();
    }

    /**
     * Soft deletes a user from its id.
     * @param int $id The id of the user to soft delete.
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
     * Deletes a user by the associated id given in parameter.
     * @param int $id The id of the user to delete.
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