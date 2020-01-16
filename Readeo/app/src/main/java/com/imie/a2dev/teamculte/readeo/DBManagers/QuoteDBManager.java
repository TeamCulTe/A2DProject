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
import com.android.volley.toolbox.StringRequest;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Quote;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.DEFAULT_FORMAT;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.BOOK;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.QUOTE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.USER;

/**
 * Manager class used to manage the quote entities from databases.
 */
public final class QuoteDBManager extends SimpleDBManager {
    /**
     * QuoteDBManager's constructor.
     * @param context The associated context.
     */
    public QuoteDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
        this.baseUrl = APIManager.API_URL + APIManager.QUOTES;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull Quote entity) {
        try {
            ContentValues data = new ContentValues();
            long id;

            if (entity.getId() != 0) {
                data.put(ID, entity.getId());
            }

            data.put(USER, entity.getUserId());
            data.put(BOOK, entity.getBookId());
            data.put(QUOTE, entity.getQuote());

            id = this.database.insertOrThrow(this.table, null, data);
            if (entity.getId() == 0) {
                entity.setId((int) id);
            }

            return true;
        } catch (SQLiteException e) {
            this.logError("createSQLite", e);

            return false;
        }
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull Quote entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(QUOTE, entity.getQuote());
            data.put(UPDATE, new DateTime().toString(DEFAULT_FORMAT));

            return this.database.update(this.table, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            this.logError("updateSQLite", e);

            return false;
        }
    }

    /**
     * From an id, returns the associated java entity.
     * @param id The id of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public Quote loadSQLite(int id) {
        Cursor result = this.loadCursorSQLite(id);

        if (result == null || result.getCount() == 0) {
            return null;
        }

        return new Quote(result);
    }

    /**
     * From a book id, returns the associated list of quotes.
     * @param idBook The id of the book.
     * @return The list of entities if exists else an empty ArrayList.
     */
    public List<Quote> loadBookSQLite(int idBook) {
        return this.loadSQLite(idBook, BOOK);
    }

    /**
     * From a user id, returns the associated list of quotes.
     * @param idUser The id of the user.
     * @return The list of entities if exists else an empty ArrayList.
     */
    public List<Quote> loadUserSQLite(int idUser) {
        return this.loadSQLite(idUser, USER);
    }

    /**
     * Deletes all quotes entries from the SQLite database for a specific user.
     * @param id The id of the user.
     * @return True if success else false.
     */
    public boolean deleteUserSQLite(int id) {
        try {
            String whereClause = String.format("%s = ?", USER);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(this.table, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            this.logError("deleteUserSQLite", e);

            return false;
        }
    }

    /**
     * Deletes all quotes entries from the SQLite database for a specific book.
     * @param id The id of the book.
     * @return True if success else false.
     */
    public boolean deleteBookSQLite(int id) {
        try {
            String whereClause = String.format("%s = ?", BOOK);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(this.table, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            this.logError("deleteBookSQLite", e);

            return false;
        }
    }

    /**
     * From an id book and id user, returns the associated list of quotes.
     * @param idUser The id to filter on.
     * @param idBook The id to filter on.
     * @return The list of entities if exists else an empty ArrayList.
     */
    public List<Quote> loadUserBookSQLite(int idUser, int idBook) {
        try {
            ArrayList<Quote> quotes = new ArrayList<>();
            String[] selectArgs = {String.valueOf(idUser), String.valueOf(idBook)};
            String query = String.format(this.DOUBLE_QUERY_ALL, this.table, USER, BOOK);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                quotes.add(new Quote(result, false));
            }

            result.close();

            return quotes;
        } catch (SQLiteException e) {
            this.logError("loadSQLite", e);

            return null;
        }
    }

    /**
     * Deletes all quotes from the SQLite database for a specific id and filter (book or user).
     * @param id The id from which delete the entries.
     * @return True if success else false.
     */
    private boolean deleteSQLite(int id, String filter) {
        try {
            String whereClause = String.format("%s = ?", filter);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(this.table, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            this.logError("deleteSQLite", e);

            return false;
        }
    }

    /**
     * From an id and a filter type (column), returns the associated list of quotes.
     * @param id The id to filter on.
     * @param column The id column to filter on.
     * @return The list of entities if exists else an empty ArrayList.
     */
    private List<Quote> loadSQLite(int id, String column) {
        try {
            ArrayList<Quote> quotes = new ArrayList<>();
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(this.SIMPLE_QUERY_ALL, this.table, column);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                quotes.add(new Quote(result, false));
            }

            result.close();

            return quotes;
        } catch (SQLiteException e) {
            this.logError("loadSQLite", e);

            return null;
        }
    }

    /**
     * Query all the quotes from the database.
     * @return The list of quotes.
     */
    public List<Quote> queryAllSQLite() {
        List<Quote> quotes = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    quotes.add(new Quote(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("queryAllSQLite", e);
        }

        return quotes;
    }

    /**
     * From the API, query the list of all quotes from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(baseUrl + APIManager.READ);
    }

    /**
     * Creates a quote entity in MySQL database.
     * @param quote The quote to create.
     */
    public void createMySQL(final Quote quote) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        if (quote.getId() != 0) {
            param.put(ID, String.valueOf(quote.getId()));
        }

        // TODO : Fix here, User id SQLite instead of MySQL.
        param.put(USER, String.valueOf(quote.getUserId()));
        param.put(BOOK, String.valueOf(quote.getBookId()));
        param.put(QUOTE, quote.getQuote());

        StringRequest request = new StringRequest(Request.Method.POST, url, null, new OnRequestError()) {
            @Override
            protected Map<String, String> getParams() {
                return param;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(QuoteDBManager.this.getContext())
                                         .finishRequest(QuoteDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String resp = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Pattern pattern = Pattern.compile("^\\d.$");

                    if (pattern.matcher(resp).find()) {
                        quote.setId(Integer.valueOf(resp));
                        QuoteDBManager.this.createSQLite(quote);
                    }
                } catch (IOException e) {
                    QuoteDBManager.this.logError("createMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(QuoteDBManager.this.getContext())
                                             .finishRequest(QuoteDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
    }

    /**
     * Loads a quote from MySQL database.
     * @param idQuote The id of the quote.
     * @return The loaded quote.
     */
    public Quote loadMySQL(int idQuote) {
        final Quote quote = new Quote();
        String url = this.baseUrl + APIManager.READ + ID + "=" + idQuote;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null, new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                   HttpHeaderParser.parseCharset(response.headers)));
                    JSONObject object = jsonArray.getJSONObject(0);

                    quote.init(object);
                } catch (Exception e) {
                    QuoteDBManager.this.logError("loadMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(QuoteDBManager.this.getContext())
                                             .finishRequest(QuoteDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(QuoteDBManager.this.getContext())
                                         .finishRequest(QuoteDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();

        return (quote.isEmpty()) ? null : quote;
    }

    /**
     * Updates a quote entity in MySQL database.
     * @param quote The quote to update.
     */
    public void updateMySQL(Quote quote) {
        String url = this.baseUrl + APIManager.UPDATE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(quote.getId()));
        param.put(QUOTE, quote.getQuote());

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Deletes a quote entity in MySQL database.
     * @param id The id of the entity to delete.
     */
    public void deleteMySQL(int id) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(id));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Soft deletes a quote entity in MySQL database.
     * @param id The id of the entity to delete.
     */
    public void softDeleteMySQL(int id) {
        String url = this.baseUrl + APIManager.SOFT_DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(id));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Deletes all quote entities in MySQL database from a specific user.
     * @param idUser The id of the user.
     */
    public void deleteUserMySQL(int idUser) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(idUser));
        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Soft deletes all quote entities in MySQL database from a specific user.
     * @param idUser The id of the user.
     */
    public void softDeleteUserMySQL(int idUser) {
        String url = this.baseUrl + APIManager.SOFT_DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(idUser));
        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Restores a quote entity in MySQL database.
     * @param id The id of the entity to delete.
     */
    public void restoreMySQL(int id) {
        String url = this.baseUrl + APIManager.RESTORE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(id));
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
     * Deletes a quote entity in MySQL database.
     * @param quote The quote to delete.
     */
    public void deleteMySQL(Quote quote) {
        this.deleteMySQL(quote.getId());
    }

    /**
     * Soft deletes a quote entity in MySQL database.
     * @param quote The quote to delete.
     */
    public void softDeleteMySQL(Quote quote) {
        this.softDeleteMySQL(quote.getId());
    }

    @Override
    public boolean createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(USER, entity.getInt(USER));
            data.put(BOOK, entity.getInt(BOOK));
            data.put(QUOTE, entity.getString(QUOTE));

            this.database.insertOrThrow(this.table, null, data);

            return true;
        } catch (Exception e) {
            this.logError("createSQLite", e);

            return false;
        }
    }

    @Override
    public boolean updateSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{entity.getString(ID)};

            data.put(QUOTE, entity.getString(QUOTE));
            data.put(UPDATE, new DateTime().toString(DEFAULT_FORMAT));

            return this.database.update(this.table, data, whereClause, whereArgs) != 0;
        } catch (Exception e) {
            this.logError("createSQLite", e);

            return false;
        }
    }
}
