package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Review;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.DEFAULT_FORMAT;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema.BOOK;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema.REVIEW;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema.SHARED;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema.USER;

/**
 * Manager class used to manage the review entities from databases.
 */
public final class ReviewDBManager extends RelationDBManager {
    /**
     * ReviewDBManager's constructor.
     * @param context The associated context.
     */
    public ReviewDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{USER, BOOK};
        this.baseUrl = APIManager.API_URL + APIManager.REVIEWS;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull Review entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();

            data.put(USER, entity.getUserId());
            data.put(BOOK, entity.getId());
            data.put(REVIEW, entity.getReview());

            this.database.insertOrThrow(this.table, null, data);
            this.database.setTransactionSuccessful();

            return true;
        } catch (SQLiteException e) {
            this.logError("createSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull Review entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ? AND %s = ?", BOOK, USER);
            String[] whereArgs = new String[]{String.valueOf(entity.getId()), String.valueOf(entity.getUserId())};

            data.put(REVIEW, entity.getReview());
            data.put(UPDATE, new DateTime().toString(DEFAULT_FORMAT));

            boolean success = this.database.update(this.table, data, whereClause, whereArgs) != 0;

            this.database.setTransactionSuccessful();

            return success;
        } catch (SQLiteException e) {
            this.logError("updateSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
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
            String query = String.format(this.DOUBLE_QUERY_FIELD, field, this.table, USER, BOOK);
            Cursor result = this.database.rawQuery(query, selectArgs);

            result.moveToNext();

            String queriedField = result.getString(result.getColumnIndexOrThrow(field));

            result.close();

            return queriedField;
        } catch (SQLiteException e) {
            this.logError("getFieldSQLite", e);

            return null;
        }
    }

    /**
     * From a couple of ids, returns the associated java entity.
     * @param idUser The id of the user.
     * @param idBook The id of the book.
     * @return The loaded entity if exists else null.
     */
    public Review loadSQLite(int idUser, int idBook) {
        Cursor result = this.loadCursorSQLite(idUser, idBook);

        if (result == null || result.getCount() == 0) {
            return null;
        }

        return new Review(result);
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
     * Deletes all reviews entries from the SQLite database for a specific user.
     * @param id The id of the user.
     * @return True if success else false.
     */
    public boolean deleteUserSQLite(int id) {
        this.database.beginTransaction();
        
        try {
            String whereClause = String.format("%s = ?", USER);
            String[] whereArgs = new String[]{String.valueOf(id)};

            boolean success = this.database.delete(this.table, whereClause, whereArgs) != 0;

            this.database.setTransactionSuccessful();

            return success;
        } catch (SQLiteException e) {
            this.logError("deleteUserSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }

    /**
     * Deletes all reviews entries from the SQLite database for a specific book.
     * @param id The id of the book.
     * @return True if success else false.
     */
    public boolean deleteBookSQLite(int id) {
        this.database.beginTransaction();
        
        try {
            String whereClause = String.format("%s = ?", BOOK);
            String[] whereArgs = new String[]{String.valueOf(id)};

            boolean success = this.database.delete(this.table, whereClause, whereArgs) != 0;

            this.database.setTransactionSuccessful();

            return success;
        } catch (SQLiteException e) {
            this.logError("deleteBookSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }

    /**
     * Query all the reviews from the database.
     * @return The list of reviews.
     */
    public List<Review> queryAllSQLite() {
        List<Review> reviews = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    reviews.add(new Review(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("queryAllSQLite", e);
        }

        return reviews;
    }

    /**
     * From the API, query the list of all the shared reviews from the MySQL database in order to stores it into the
     * SQLite database.
     */
    public void importFromMySQL() {
        String url = String.format(baseUrl + APIManager.READ + "?%s=true", SHARED);

        super.importFromMySQL(url);
    }

    /**
     * Creates a review entity in MySQL database.
     * @param review The review to create.
     */
    public void createMySQL(Review review) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(review.getUserId()));
        param.put(BOOK, String.valueOf(review.getId()));
        param.put(REVIEW, review.getReview());
        param.put(SHARED, String.valueOf(review.isShared()));

        super.requestString(Request.Method.POST, url, null, param);
    }

    /**
     * Loads a review from MySQL database.
     * @param idUser The id of the author.
     * @param idBook The id of the book.
     * @return The loaded review.
     */
    public Review loadMySQL(int idUser, int idBook) {
        final Review review = new Review();
        String url = this.baseUrl + APIManager.READ + USER + "=" + idUser + "&" + BOOK + "=" + idBook;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null, new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                   HttpHeaderParser.parseCharset(response.headers)));
                    JSONObject object = jsonArray.getJSONObject(0);

                    review.init(object);
                } catch (Exception e) {
                    ReviewDBManager.this.logError("loadMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(ReviewDBManager.this.getContext())
                                             .finishRequest(ReviewDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(ReviewDBManager.this.getContext())
                                         .finishRequest(ReviewDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();

        return (review.isEmpty()) ? null : review;
    }

    /**
     * Loads the reviews associated to a user from MySQL database.
     * @param idUser The id of the author.
     * @return The loaded reviews.
     */
    public List<Review> loadUserMySQL(int idUser) {
        final List<Review> reviews = new ArrayList<>();
        String url = this.baseUrl + APIManager.READ + USER + "=" + idUser;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null, new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                   HttpHeaderParser.parseCharset(response.headers)));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        reviews.add(new Review(jsonArray.getJSONObject(i)));
                    }
                } catch (Exception e) {
                    ReviewDBManager.this.logError("loadUserMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(ReviewDBManager.this.getContext())
                                             .finishRequest(ReviewDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(ReviewDBManager.this.getContext())
                                         .finishRequest(ReviewDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();

        return reviews;
    }

    /**
     * Updates a review entity in MySQL database.
     * @param review The review to update.
     */
    public void updateMySQL(Review review) {
        String url = this.baseUrl + APIManager.UPDATE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(review.getUserId()));
        param.put(BOOK, String.valueOf(review.getId()));
        param.put(REVIEW, review.getReview());
        param.put(SHARED, String.valueOf(review.isShared()));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Deletes a review entity in MySQL database.
     * @param idUser The id of the user.
     * @param idBook The id of the book.
     */
    public void deleteMySQL(int idUser, int idBook) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(idUser));
        param.put(BOOK, String.valueOf(idBook));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Deletes all reviews entities in MySQL database from a specific user.
     * @param idUser The id of the user.
     */
    public void deleteUserMySQL(int idUser) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(idUser));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Restores a quote entity in MySQL database.
     * @param idUser The id of the user.
     * @param idBook The id of the book.
     */
    public void restoreMySQL(int idUser, int idBook) {
        String url = this.baseUrl + APIManager.RESTORE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(idUser));
        param.put(BOOK, String.valueOf(idBook));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Restores all quote entities in MySQL database from a specific user.
     * @param idUser The id of the user.
     */
    public void restoreUserMySQL(int idUser) {
        String url = this.baseUrl + APIManager.RESTORE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(idUser));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Deletes a review entity in MySQL database.
     * @param review The review to delete.
     */
    public void deleteMySQL(Review review) {
        this.deleteMySQL(review.getUserId(), review.getId());
    }

    /**
     * Deletes the test entity in MySQL database.
     * @param idUser The id of the user who wrote the test review to delete.
     * @param idBook The id of the book.
     */
    public void deleteTestEntityMySQL(int idUser, int idBook) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(this.ids[0], String.valueOf(idUser));
        param.put(this.ids[1], String.valueOf(idBook));

        param.put(APIManager.TEST, "1");

        super.requestString(Request.Method.DELETE, url, null, param);
    }

    @Override
    public boolean createSQLite(@NonNull JSONObject entity) {
        this.database.beginTransaction();

        try {
            ContentValues data = new ContentValues();

            data.put(USER, entity.getInt(USER));
            data.put(BOOK, entity.getInt(BOOK));
            data.put(REVIEW, entity.getString(REVIEW));

            this.database.insertOrThrow(this.table, null, data);
            this.database.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            this.logError("createSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }

    @Override
    public boolean updateSQLite(@NonNull JSONObject entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ? AND %s = ?", USER, BOOK);
            String[] whereArgs = new String[]{entity.getString(USER), entity.getString(BOOK)};

            data.put(REVIEW, entity.getString(REVIEW));
            data.put(UPDATE, new DateTime().toString(DEFAULT_FORMAT));

            boolean success = this.database.update(this.table, data, whereClause, whereArgs) != 0;

            this.database.setTransactionSuccessful();

            return success;
        } catch (Exception e) {
            this.logError("updateSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }

    /**
     * Deletes all reviews from the SQLite database for a specific id and filter (book or user).
     * @param id The id from which delete the entries.
     * @return True if success else false.
     */
    private boolean deleteSQLite(int id, String filter) {
        this.database.beginTransaction();
        
        try {
            String whereClause = String.format("%s = ?", filter);
            String[] whereArgs = new String[]{String.valueOf(id)};

            boolean success = this.database.delete(this.table, whereClause, whereArgs) != 0;

            this.database.setTransactionSuccessful();

            return success;
        } catch (SQLiteException e) {
            this.logError("deleteSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
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
            String query = String.format(this.SIMPLE_QUERY_ALL, this.table, column);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                reviews.add(new Review(result, false));
            }

            result.close();

            return reviews;
        } catch (SQLiteException e) {
            this.logError("loadSQLite", e);

            return null;
        }
    }
}
