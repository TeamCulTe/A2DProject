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
    public const FIELDS = ["id_user", "id_book", "review", "shared", "last_update", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    public const PLACEHOLDERS = [":idU", ":idB", ":review", ":shared", ":update"];

    /**
     * Stores the associated table name.
     */
    public const TABLE = "Review";

    /**
     * Creates a review into the database.
     * @param int $idUser The id of the user who owns the review list.
     * @param int $idBook The id of the book.
     * @param string $review The review to set.
     * @param bool $shared The review's shared status to set.
     * @return true|false True if success else false.
     */
    public function create(int $idUser, int $idBook, string $review, bool $shared = false)
    {
        $statement = sprintf("INSERT INTO %s(%s, %s, %s, %s) VALUES(%s, %s, %s, %s)",
            static::TABLE, static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3],
            static::PLACEHOLDERS[0], static::PLACEHOLDERS[1], static::PLACEHOLDERS[2], static::PLACEHOLDERS[3]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $review, PDO::PARAM_STR);
        $req->bindValue(static::PLACEHOLDERS[3], $shared, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Gets a review written by a specific user for a specific book.
     * @param int $idUser The id of the user.
     * @param int $idBook The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getReview(int $idUser, int $idBook)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = %s AND %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4],
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Gets a shared review written by a specific user for a specific book.
     * @param int $idUser The id of the user.
     * @param int $idBook The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getSharedReview(int $idUser, int $idBook)
    {
        $statement = sprintf("SELECT %s, %s FROM %s WHERE %s = 1 AND %s = %s AND %s = %s AND deleted = 0",
            static::FIELDS[2], static::FIELDS[4], static::TABLE, static::FIELDS[3], static::FIELDS[0],
            static::PLACEHOLDERS[0], static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Gets the reviews written for a specific book.
     * @param int $idBook The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getBookReviews(int $idBook)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::TABLE, static::FIELDS[1],
            static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Gets the reviews written for a specific book.
     * @param int $idBook The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getBookSharedReviews(int $idBook)
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s = 1 AND %s = %s AND deleted = 0",
            static::FIELDS[0], static::FIELDS[2], static::FIELDS[4], static::TABLE, static::FIELDS[3], static::FIELDS[1],
            static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Gets the reviews written by a specific user.
     * @param int $idUser The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getUserReviews(int $idUser)
    {
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE %s = %s AND deleted = 0",
            static::FIELDS[1], static::FIELDS[2], static::FIELDS[3], static::FIELDS[4], static::TABLE, static::FIELDS[0],
            static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Gets the shared reviews written by a specific user.
     * @param int $idUser The id of the book.
     * @return null|string The json response if exists else null.
     */
    public function getUserSharedReviews(int $idUser)
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s = 1 AND %s = %s AND deleted = 0",
            static::FIELDS[1], static::FIELDS[2], static::FIELDS[4], static::TABLE, static::FIELDS[3],
            static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Updates a review text by the id of the book and the id of the user who wrote it.
     * @param int $idUser The id of user.
     * @param int $idBook The id of the book.
     * @param string $review The new text to set.
     * @return true|false True if success else false.
     */
    public function updateReview(int $idUser, int $idBook, string $review)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s AND %s = %s", static::TABLE,
            static::FIELDS[2], static::PLACEHOLDERS[2], static::FIELDS[4], static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1],
            static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[2], $review, PDO::PARAM_STR);

        return $req->execute();
    }

    /**
     * Updates a review sharing status by the id of the book and the id of the user who wrote it.
     * @param int $idUser The id of user.
     * @param int $idBook The id of the book.
     * @param bool $shared The new shared  status to set
     * @return true|false True if success else false.
     */
    public function updateReviewSharing(int $idUser, int $idBook, bool $shared)
    {
        $statement = sprintf("UPDATE %s SET %s = %s, %s = CURRENT_TIMESTAMP WHERE %s = %s AND %s = %s", static::TABLE,
            static::FIELDS[3], static::PLACEHOLDERS[3], static::FIELDS[4], static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1],
            static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[3], $shared, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a couple of id given in parameter, soft delete the associated review.
     * @param int $idUser The id of user.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function softDelete(int $idUser, $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1, %s = CURRENT_TIMESTAMP WHERE %s = %s AND %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a user id and a book id given in parameter, restore the associated soft deleted review.
     * @param int $idUser The id of the user.
     * @param int $idBook The id of the user.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeleted(int $idUser, $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0, %s = CURRENT_TIMESTAMP WHERE %s = %s AND %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a user id given in parameter, soft delete the associated reviews.
     * @param int $idUser The id of user.
     * @return true|false True if success else false.
     */
    public function softDeleteUserReviews(int $idUser)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a user id given in parameter, restore the associated soft deleted reviews.
     * @param int $idUser The id of the user.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedUserReviews(int $idUser)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a book id given in parameter, soft delete the associated reviews.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function softDeleteBooksReviews(int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 1, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * From a book id given in parameter, restore the associated soft deleted reviews.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function restoreSoftDeletedBookReviews(int $idBook)
    {
        $statement = sprintf("UPDATE %s SET deleted = 0, %s = CURRENT_TIMESTAMP WHERE %s = %s",
            static::TABLE, static::FIELDS[4], static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Deletes a review from the database.
     * @param int $idUser The id of user.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function delete(int $idUser, $idBook)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s AND %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0], static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);
        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Deletes all reviews associated to a specific user from the database.
     * @param int $idUser The id of user.
     * @return true|false True if success else false.
     */
    public function deleteUserReviews(int $idUser)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[0], static::PLACEHOLDERS[0]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[0], $idUser, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Deletes all reviews associated to a specific book from the database.
     * @param int $idBook The id of the book.
     * @return true|false True if success else false.
     */
    public function deleteBookReviews(int $idBook)
    {
        $statement = sprintf("DELETE FROM %s WHERE %s = %s",
            static::TABLE, static::FIELDS[1], static::PLACEHOLDERS[1]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[1], $idBook, PDO::PARAM_INT);

        return $req->execute();
    }

    /**
     * Query all reviews from database.
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
     * Query all shared reviews from database.
     * @return null|string The json response if found else null.
     */
    public function queryAllShared()
    {
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE %s = 1 AND deleted = 0",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[4], static::TABLE, static::FIELDS[3]);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all reviews from database with pagination (number parameters included).
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

        $req->bindValue($offsetPlaceholder, ($start - 1), PDO::PARAM_INT);
        $req->bindValue($limitPlaceholder, ($end - $start + 1), PDO::PARAM_INT);

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all shared reviews from database with pagination (number parameters included).
     * @param int $start The start index of the results to get.
     * @param int $end The last index of the results to get.
     * @return null|string The json response if found else null.
     */
    public function queryAllSharedPaginated(int $start, int $end)
    {
        $offsetPlaceholder = ":startResult";
        $limitPlaceholder = ":endResult";
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE %s = 1 AND deleted = 0 LIMIT %s OFFSET %s",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[4], static::TABLE, static::FIELDS[3],
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
     * Counts the number of shared reviews in the database.
     * @return null|string The json response if found else null.
     */
    public function countShared() {
        $statement = sprintf("SELECT COUNT(*) as %s FROM %s WHERE deleted = 0 AND %s = 1", static::COUNT,
            static::TABLE, static::FIELDS[3]);
        $req = $this->db->query($statement);
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
     * Get the shared entries that has been updated after than the date value given in parameter.
     * @param string $date The date to query entities.
     * @return null|string The json response if found else null.
     */
    public function querySharedNewer(string $date) {
        $statement = sprintf("SELECT %s, %s, %s, %s FROM %s WHERE %s > %s AND deleted = 0 AND %s = 1",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[2], static::FIELDS[4], static::TABLE,
            static::FIELDS[4], static::PLACEHOLDERS[4], static::FIELDS[3]);
        $req = $this->db->prepare($statement);

        $req->bindValue(static::PLACEHOLDERS[4], $date, PDO::PARAM_STR);
        $req->execute();

        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all review ids and last_update fields from database in order to use it to determine which entities to update.
     * @return null|string The json response if found else null.
     */
    public function queryUpdateFields()
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE deleted = 0 ORDER BY %s",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[4], static::TABLE, static::FIELDS[0]);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Query all shared review ids and last_update fields from database in order to use it to determine which entities to update.
     * @return null|string The json response if found else null.
     */
    public function querySharedUpdateFields()
    {
        $statement = sprintf("SELECT %s, %s, %s FROM %s WHERE %s = 1 AND deleted = 0 ORDER BY %s",
            static::FIELDS[0], static::FIELDS[1], static::FIELDS[4], static::TABLE, static::FIELDS[3], static::FIELDS[0]);
        $req = $this->db->query($statement);
        $response = $req->fetchAll(PDO::FETCH_ASSOC);

        return json_encode($response);
    }

    /**
     * Deletes all the test entities (id inferior to 0).
     */
    public function deleteTestEntities() {
        $statement = sprintf("DELETE FROM %s WHERE %s < 0 AND %s < 0", static::TABLE, static::FIELDS[0], static::FIELDS[1]);

        $this->db->exec($statement);
    }
}