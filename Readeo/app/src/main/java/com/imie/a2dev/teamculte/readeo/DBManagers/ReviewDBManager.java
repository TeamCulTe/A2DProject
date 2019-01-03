package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Review;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class used to manage the review entities from databases.
 */
public final class ReviewDBManager extends DBManager {
    /**
     * Defines the review's table name.
     */
    public static final String TABLE = "Review";

    /**
     * Defines the review's user id field.
     */
    public static final String USER = UserDBManager.ID;

    /**
     * Defines the review's book id field.
     */
    public static final String BOOK = BookDBManager.ID;

    /**
     * Defines the review's user id field.
     */
    public static final String REVIEW = "review";

    /**
     * Stores the user db manager used to correspond between the user pseudo and the associated id.
     */
    private final UserDBManager userDBManager;

    /**
     * ReviewDBManager's constructor.
     * @param context The associated context.
     */
    public ReviewDBManager(Context context) {
        super(context);

        this.userDBManager = new UserDBManager(context);
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean SQLiteCreate(@NonNull Review entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(USER, this.userDBManager.SQLiteGetId(entity.getAuthor()));
            data.put(BOOK, entity.getId());
            this.database.insertOrThrow(TABLE, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param idUser The id of the user.
     * @param idBook The id of the book.
     * @return The value of the field.
     */
    public String SQLiteGetField(String field, int idUser, int idBook) {
        try {
            String[] selectArgs = {String.valueOf(idUser), String.valueOf(idBook)};
            String query = String.format(DOUBLE_QUERY_FIELD, field, TABLE, USER, BOOK);
            Cursor result = this.database.rawQuery(query, selectArgs);

            result.moveToNext();

            String queriedField = result.getString(result.getColumnIndexOrThrow(field));

            result.close();

            return queriedField;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean SQLiteUpdate(@NonNull Review entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ? AND %s = ?", BOOK, USER);
            String[] whereArgs = new String[]{String.valueOf(entity.getId()),
                    String.valueOf(this.userDBManager.SQLiteGetId(entity.getAuthor()))};

            data.put(REVIEW, entity.getReview());

            return this.database.update(TABLE, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From a couple of ids, returns the associated java entity.
     * @param idUser The id of the user.
     * @param idBook The id of the book.
     * @return The loaded entity if exists else null.
     */
    public Review SQLiteLoad(int idUser, int idBook) {
        try {
            String[] selectArgs = {String.valueOf(idUser), String.valueOf(idBook)};
            String query = String.format(DOUBLE_QUERY_ALL, TABLE, USER, BOOK);
            Cursor result = this.database.rawQuery(query, selectArgs);

            return new Review(result);
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a book id, returns the associated list of reviews.
     * @param idBook The id of the book.
     * @return The list of entities if exists else else an empty ArrayList.
     */
    public List<Review> SQLiteLoadBook(int idBook) {
        return this.SQLiteLoad(idBook, BOOK);
    }

    /**
     * From a user id, returns the associated list of reviews.
     * @param idUser The id of the user.
     * @return The list of entities if exists else an empty ArrayList.
     */
    public List<Review> SQLiteLoadUser(int idUser) {
        return this.SQLiteLoad(idUser, USER);
    }

    /**
     * Deletes a review entry from the database.
     * @param idUser The user affected to the review.
     * @param idBook The book affected to the review.
     * @return True if success else false.
     */
    public boolean SQLiteDelete(int idUser, int idBook) {
        try {
            String whereClause = String.format("%s = ? AND %s = ?", USER, BOOK);
            String[] whereArgs = new String[]{String.valueOf(idUser), String.valueOf(idBook)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all reviews entries from the SQLite database for a specific user.
     * @param id The id of the user.
     * @return True if success else false.
     */
    public boolean SQLiteDeleteAuthor(int id) {
        try {
            String whereClause = String.format("%s = ?", USER);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all reviews entries from the SQLite database for a specific book.
     * @param id The id of the book.
     * @return True if success else false.
     */
    public boolean SQLiteDeleteBook(int id) {
        try {
            String whereClause = String.format("%s = ?", BOOK);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all reviews from the SQLite database for a specific id and filter (book or user).
     * @param id The id from which delete the entries.
     * @return True if success else false.
     */
    private boolean SQLiteDelete(int id, String filter) {
        try {
            String whereClause = String.format("%s = ?", filter);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From an id and a filter type (column), returns the associated list of reviews.
     * @param id The id to filter on.
     * @param column The id column to filter on.
     * @return The list of entities if exists else an empty ArrayList.
     */
    private List<Review> SQLiteLoad(int id, String column) {
        try {
            ArrayList<Review> reviews = new ArrayList<>();
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, column);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                reviews.add(new Review(result, false));
            }

            result.close();

            return reviews;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Query all the reviews from the database.
     * @return The list of reviews.
     */
    public List<Review> queryAll() {
        List<Review> reviews = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(QUERY_ALL, TABLE), null);

            while (result.moveToNext()) {
                reviews.add(new Review(result));
            }

        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }

        return reviews;
    }
}
