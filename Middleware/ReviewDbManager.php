<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:07
 */

require_once "DbManager.php";

/**
 * DAO Class ReviewDbManager, used to manage categories in database (CRUD).
 */
class ReviewDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    const FIELDS = ["id_user", "id_book", "review", "shared", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    const PLACEHOLDERS = [":idU", ":idB", ":review", ":shared"];

    /**
     * Stores the associated table name.
     */
    const TABLE = "Review";

    /**
     * Creates a review into the database.
     * @param int $idUser The id of the user who owns the review list.
     * @param int $idBook The id of the book.
     * @param string $review The review to set.
     * @param bool $shared The review to set.
     */
    public function create(int $idUser, int $idBook, string $review, bool $shared)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s, %s, %s) VALUES(%s, %s, %s, %s)",
            static::TABLE, static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3],
            static::PLACEHOLDERS[0], static::PLACEHOLDERS[1], static::PLACEHOLDERS[2], static::PLACEHOLDERS[3]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $shared, PDO::PARAM_BOOL);
        $req->bindValue(static::PLACEHOLDERS[3], $review, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * Gets a review written by a specific user for a specific book.
     * @param int $idUser The id of the user.
     * @param int $idBook The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getReview(int $idUser, int $idBook)
    {
        $statement = sprintf("SELECT %s, %s FROM %s WHERE %s = %s AND %s = %s AND deleted = 0",
            static::FIELDS[2], static::FIELDS[3], static::TABLE, static::FIELDS[0],
            static::PLACEHOLDERS[0], static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Gets the reviews written for a specific book.
     * @param int $idBook The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getBookReviews(int $idBook)
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s = %s AND %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[2], static::FIELDS[3], static::TABLE, static::FIELDS[1],
            static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Gets the reviews written by a specific user.
     * @param int $idUser The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getUserReviews(int $idUser)
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s = %s AND %s = %s AND deleted = 0",
            static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::TABLE, static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return (!empty($response)) ? json_encode($response) : null;
    }

    /**
     * Updates a review text by the id of the book and the id of the user who wrote it.
     * @param int $idUser The id of user.
     * @param int $idBook The id of the book.
     * @param string $review The new text to set.
     */
    public function updateReview(int $idUser, int $idBook, string $review)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s AND %s = %s", static::TABLE,
            static::FIELDS[2], static::PLACEHOLDERS[2], static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1],
            static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $review, PDO::PARAM_STR);
        $req->execute();
    }

    /**
     * Updates a review sharing status by the id of the book and the id of the user who wrote it.
     * @param int $idUser The id of user.
     * @param int $idBook The id of the book.
     * @param bool $shared The new shared  status to set.
     */
    public function updateReviewSharing(int $idUser, int $idBook, bool $shared)
    {
        $statement = sprintf("UPDATE %s SET %s = %s WHERE %s = %s AND %s = %s", static::TABLE,
            static::FIELDS[3], static::PLACEHOLDERS[3], static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1],
            static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[3], $shared, PDO::PARAM_BOOL);
        $req->execute();
    }

    /**
     * From an id given in parameter, soft delete the associated review.
     * @param int $id The id of the review to delete.
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
     * Deletes a review from the database.
     * @param int $id The id of the review to delete.
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