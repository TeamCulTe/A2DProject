package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Review;
import org.json.JSONException;
import org.json.JSONObject;

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
     * Defines the review's text field.
     */
    public static final String REVIEW = "review";

    /**
     * Defines the review's shared field.
     */
    public static final String SHARED = "shared";

    /**
     * Stores the base of the reviews API url.
     */
    private final String baseUrl = APIManager.API_URL + APIManager.REVIEWS;

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
    public boolean createSQLite(@NonNull Review entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(USER, this.userDBManager.SQLiteGetId(entity.getAuthor()));
            data.put(BOOK, entity.getId());
            DBManager.database.insertOrThrow(TABLE, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

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
    public String getFieldSQLite(String field, int idUser, int idBook) {
        try {
            String[] selectArgs = {String.valueOf(idUser), String.valueOf(idBook)};
            String query = String.format(DOUBLE_QUERY_FIELD, field, TABLE, USER, BOOK);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

            result.moveToNext();

            String queriedField = result.getString(result.getColumnIndexOrThrow(field));

            result.close();

            return queriedField;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull Review entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ? AND %s = ?", BOOK, USER);
            String[] whereArgs = new String[]{String.valueOf(entity.getId()),
                    String.valueOf(this.userDBManager.SQLiteGetId(entity.getAuthor()))};

            data.put(REVIEW, entity.getReview());

            return DBManager.database.update(TABLE, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From a couple of ids, returns the associated java entity.
     * @param idUser The id of the user.
     * @param idBook The id of the book.
     * @return The loaded entity if exists else null.
     */
    public Review loadSQLite(int idUser, int idBook) {
        try {
            String[] selectArgs = {String.valueOf(idUser), String.valueOf(idBook)};
            String query = String.format(DOUBLE_QUERY_ALL, TABLE, USER, BOOK);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

            return new Review(result);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a book id, returns the associated list of reviews.
     * @param idBook The id of the book.
     * @return The list of entities if exists else else an empty ArrayList.
     */
    public List<Review> loadBookSQLite(int idBook) {
        return this.loadSQLite(idBook, BOOK);
    }

    /**
     * From a user id, returns the associated list of reviews.
     * @param idUser The id of the user.
     * @return The list of entities if exists else an empty ArrayList.
     */
    public List<Review> loadUserSQLite(int idUser) {
        return this.loadSQLite(idUser, USER);
    }

    /**
     * Deletes a review entry from the database.
     * @param idUser The user affected to the review.
     * @param idBook The book affected to the review.
     * @return True if success else false.
     */
    public boolean deleteSQLite(int idUser, int idBook) {
        try {
            String whereClause = String.format("%s = ? AND %s = ?", USER, BOOK);
            String[] whereArgs = new String[]{String.valueOf(idUser), String.valueOf(idBook)};

            return DBManager.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all reviews entries from the SQLite database for a specific user.
     * @param id The id of the user.
     * @return True if success else false.
     */
    public boolean deleteUserSQLite(int id) {
        try {
            String whereClause = String.format("%s = ?", USER);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return DBManager.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all reviews entries from the SQLite database for a specific book.
     * @param id The id of the book.
     * @return True if success else false.
     */
    public boolean deleteBookSQLite(int id) {
        try {
            String whereClause = String.format("%s = ?", BOOK);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return DBManager.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all reviews from the SQLite database for a specific id and filter (book or user).
     * @param id The id from which delete the entries.
     * @return True if success else false.
     */
    private boolean deleteSQLite(int id, String filter) {
        try {
            String whereClause = String.format("%s = ?", filter);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return DBManager.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From an id and a filter type (column), returns the associated list of reviews.
     * @param id The id to filter on.
     * @param column The id column to filter on.
     * @return The list of entities if exists else an empty ArrayList.
     */
    private List<Review> loadSQLite(int id, String column) {
        try {
            ArrayList<Review> reviews = new ArrayList<>();
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, column);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                reviews.add(new Review(result, false));
            }

            result.close();

            return reviews;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Query all the reviews from the database.
     * @return The list of reviews.
     */
    public List<Review> queryAllSQLite() {
        List<Review> reviews = new ArrayList<>();

        try {
            Cursor result = DBManager.database.rawQuery(String.format(QUERY_ALL, TABLE), null);

            if (result.getCount() > 0) {
                do {
                    reviews.add(new Review(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }

        return reviews;
    }

    /**
     * From the API, query the list of all the shared reviews from the MySQL database in order to stores it into the
     * SQLite database.
     */
    public void importAllFromMySQL() {
        String url = String.format(baseUrl + APIManager.READ + "?%s=true", SHARED);

        super.importAllFromMySQL(url);
    }

    /**
     * Creates a review entity in MySQL database.
     * @param review The review to create.
     */
    public void createMySQL(Review review) {
        String url = String.format(baseUrl + APIManager.CREATE + USER + "=%s&" + BOOK + "=%s&" + REVIEW +
                "=%s&" + SHARED + "=%s",
                this.userDBManager.SQLiteGetId(review.getAuthor()),
                review.getId(),
                review.getReview(),
                review.isShared());

        super.requestString(Request.Method.POST, url, null);
    }

    /**
     * Updates a review entity in MySQL database.
     * @param review The review to update.
     */
    public void updateMySQL(Review review) {
        String url = String.format(baseUrl + APIManager.UPDATE + USER + "=%s&" + BOOK + "=%s&" + REVIEW + "=%s&" +
                        SHARED + "=%s",
                this.userDBManager.SQLiteGetId(review.getAuthor()),
                review.getId(),
                review.getReview(),
                review.isShared());

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Loads a review from MySQL database.
     * @param idUser The id of the user.
     * @param idBook The id of the book.
     * @return The loaded review.
     */
    public Review loadMySQL(int idUser, int idBook) {
        final Review review = new Review();

        String url = String.format(baseUrl + APIManager.READ + USER + "=%s&" + BOOK + "=%s", idUser, idBook);

        super.requestJsonObject(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                review.init(response);
            }
        });

        return review;
    }

    /**
     * Loads all reviews written by a specific user from MySQL database.
     * @param idUser The id of the user.
     * @return The loaded review.
     */
    public List<Review> loadUserMySQL(int idUser) {
        final List<Review> reviews = new ArrayList<>();

        String url = String.format(baseUrl + APIManager.READ + USER + "=%s", idUser);

        super.requestJsonObject(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                reviews.add(new Review(response));
            }
        });

        return reviews;
    }

    /**
     * Loads all reviews written by a specific user from MySQL database.
     * @param idBook The id of the book.
     * @return The loaded review.
     */
    public List<Review> loadBookMySQL(int idBook) {
        final List<Review> reviews = new ArrayList<>();
        String url = String.format(baseUrl + APIManager.READ + BOOK + "=%s", idBook);

        super.requestJsonObject(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                reviews.add(new Review(response));
            }
        });

        return reviews;
    }

    /**
     * Deletes a review entity in MySQL database.
     * @param idUser The id of the user.
     * @param idBook The id of the book.
     */
    public void deleteMySQL(int idUser, int idBook) {
        String url = String.format(baseUrl + APIManager.DELETE + USER + "=%s&" + BOOK + "=%s", idUser, idBook);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Deletes all reviews entities in MySQL database from a specific user.
     * @param idUser The id of the user.
     */
    public void deleteUserMySQL(int idUser) {
        String url = String.format(baseUrl + APIManager.DELETE + USER + "=%s", idUser);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Restores a quote entity in MySQL database.
     * @param idUser The id of the user.
     * @param idBook The id of the book.
     */
    public void restoreMySQL(int idUser, int idBook) {
        String url = String.format(baseUrl + APIManager.RESTORE + USER + "=%s&" + BOOK + "=%s", idUser, idBook);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Restores all quote entities in MySQL database from a specific user.
     * @param idUser The id of the user.
     */
    public void restoreUserMySQL(int idUser) {
        String url = String.format(baseUrl + APIManager.RESTORE + USER + "=%s", idUser);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Deletes a review entity in MySQL database.
     * @param review The review to delete.
     */
    public void deleteMySQL(Review review) {
        this.deleteMySQL(this.userDBManager.SQLiteGetId(review.getAuthor()), review.getId());
    }

    @Override
    protected void createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(USER, entity.getInt(USER));
            data.put(BOOK, entity.getInt(BOOK));
            data.put(REVIEW, entity.getString(REVIEW));
            DBManager.database.insertOrThrow(TABLE, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
