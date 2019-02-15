package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CityDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CountryDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema;
import com.imie.a2dev.teamculte.readeo.HTTPRequestQueueSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;

/**
 * Abstract class extended by all DBManager classes (used to manage entities into databases).
 */
public abstract class DBManager {
    /**
     * Defines the database version.
     */
    private final static int VERSION = 1;

    /**
     * Defines the database file name.
     */
    private final static String DB_FILE_NAME = "readeo.db";

    // Log tags.

    /**
     * Defines the SQLite log tag.
     */
    public final static String SQLITE_TAG = "SQLiteError";

    /**
     * Defines the JSON log tag.
     */
    public final static String JSON_TAG = "JSONError";

    /**
     * Defines the server log tag.
     */
    protected final static String SERVER_TAG = "ServerError";

    // Predefined queries.

    /**
     * Defines the default all fields database query.
     */
    protected final static String QUERY_ALL = "SELECT * FROM %s";

    /**
     * Defines the default all fields database query with paginated results.
     */
    protected final static String QUERY_ALL_PAGINATED = "SELECT * FROM %s LIMIT %s OFFSET %s";

    /**
     * Defines the default all fields database query with a simple where clause.
     */
    protected final static String SIMPLE_QUERY_ALL = "SELECT * FROM %s WHERE %s = ?";

    /**
     * Defines the default all fields database query with a simple where - like (from start) clause.
     */
    protected final static String SIMPLE_QUERY_ALL_LIKE_START = "SELECT * FROM %s WHERE %s LIKE '?%%'";

    /**
     * Defines the default field database query with a simple where clause.
     */
    protected final static String SIMPLE_QUERY_FIELD = "SELECT %s FROM %s WHERE %s = ?";

    /**
     * Defines the default field database query with a simple where clause.
     */
    protected final static String DOUBLE_QUERY_FIELD = "SELECT %s FROM %s WHERE %s = ? AND %s = ?";

    /**
     * Defines the default all fields database query with a double where clause.
     */
    protected final static String DOUBLE_QUERY_ALL = "SELECT * FROM %s WHERE %s = ? AND %s = ?";

    /**
     * Defines the default simple update query (getting the id and the last_update fields) from SQLite db.
     */
    protected final static String SIMPLE_QUERY_UPDATE = "SELECT %s, " + UPDATE + " FROM %s";

    /**
     * Defines the default double update query (getting the id and the last_update fields) from SQLite db.
     */
    protected final static String DOUBLE_QUERY_UPDATE = "SELECT %s, %s, " + UPDATE + " FROM %s";

    // Other attributes

    /**
     * Defines the count param and json value from MySQL query alias.
     */
    protected static final String COUNT = "count";

    /**
     * Defines the default pagination results.
     */
    protected static final int PAGINATION = 2000;

    /**
     * Stores the managers database in order to manage.
     */
    protected static SQLiteDatabase database;

    /**
     * Stores the DBManager's managed table.
     */
    protected String table;

    /**
     * Stores the DBManager's managed entities id labels.
     */
    protected String[] ids;

    /**
     * Stores the associated context in order to use other managers from managers classes.
     */
    private Context context;

    /**
     * Stores the associated DBHandler to get the database object.
     */
    private DBHandler handler;

    /**
     * DBManager's full filled constructor initializing the handler attribute and opening the database.
     * @param context The associated context.
     */
    protected DBManager(Context context) {
        this.handler = new DBHandler(context, DB_FILE_NAME, null, VERSION);
        this.context = context;

        this.open();
    }

    /**
     * Gets the database attribute.
     * @return The SQLiteDatabase value of database attribute.
     */
    public static SQLiteDatabase getDatabase() {
        return DBManager.database;
    }

    /**
     * Gets the handler attribute.
     * @return The DBHandler value of handler attribute.
     */
    public final DBHandler getHandler() {
        return this.handler;
    }

    /**
     * Gets the context attribute.
     * @return The Context value of context attribute.
     */
    public final Context getContext() {
        return this.context;
    }

    /**
     * Sets the handler attribute.
     * @param newHandler The new DBHandler value to set.
     */
    public final void setHandler(DBHandler newHandler) {
        this.handler = newHandler;
    }

    /**
     * Sets the context attribute.
     * @param newContext The new Context value to set.
     */
    public final void setContext(Context newContext) {
        this.context = newContext;
    }

    /**
     * Queries the number of entries for a specific table.
     * @return The number of entries or -1 if an error occurred.
     */
    public final int countSQLite() {
        try {
            String[] selectArgs = {};
            String query = String.format("SELECT COUNT(*) as %s FROM %s", COUNT, this.table);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

            result.moveToNext();

            int count = result.getInt(result.getColumnIndexOrThrow(COUNT));

            result.close();

            return count;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return -1;
        }
    }

    /**
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param id The id of the db entity to access.
     * @return The value of the field.
     */
    public String getFieldSQLite(String field, int id) {
        return this.getFieldSQLite(field, this.ids[0], String.valueOf(id));
    }

    /**
     * From an id given in parameter, deletes the associated entity in the database.
     * @param id The id of the entity to delete.
     * @return true if success else false.
     */
    public boolean deleteSQLite(int id) {
        try {
            String whereClause = String.format("%s = ?", this.ids[0]);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return DBManager.database.delete(this.table, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From the API, query the list of entities (depending on the url) from the MySQL database in order to stores it
     * into the SQLite database.
     * @param url The url to request.
     */
    public final void importFromMySQL(String url) {
        this.requestJsonArray(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        DBManager.this.createSQLite(response.getJSONObject(i));
                    } catch (JSONException e) {
                        Log.e(JSON_TAG, e.getMessage());
                    }
                }

                HTTPRequestQueueSingleton.getInstance(DBManager.this.context).finishRequest();
            }
        });
    }

    /**
     * From the API, query the list of all entities from the MySQL database with pagination in order to stores it into
     * the SQLite database.
     * @param url The url to request.
     */
    public final void importPaginatedFromMySQL(final String url) {
        final Integer[] max = new Integer[1];
        final Integer[] indexes = new Integer[]{0, 0};

        this.requestJsonArray(Request.Method.GET, url + COUNT, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    max[0] = response.getJSONObject(0).getInt(COUNT);
                    indexes[1] = (max[0] > PAGINATION) ? PAGINATION : max[0];

                    while (!indexes[0].equals(max[0])) {
                        DBManager.this.importFromMySQL(String.format("%sstart=%s&end=%s", url, indexes[0], indexes[1]));
                        DBManager.this.updateIndexes(indexes, max[0]);
                    }
                } catch (JSONException e) {
                    Log.e(JSON_TAG, e.getMessage());
                }

                HTTPRequestQueueSingleton.getInstance(DBManager.this.context).finishRequest();
            }
        });
    }

    /**
     * Imports all the MySQL database into the SQLite one.
     */
    public static void importMySQLDatabase() {
        Context context = App.getAppContext();

        new AuthorDBManager(context).importFromMySQL();
        new CategoryDBManager(context).importFromMySQL();
        new CityDBManager(context).importFromMySQL();
        new CountryDBManager(context).importFromMySQL();
        new BookListTypeDBManager(context).importFromMySQL();
        new ProfileDBManager(context).importFromMySQL();
        new BookDBManager(context).importFromMySQL();
        new UserDBManager(context).importFromMySQL();
        new QuoteDBManager(context).importFromMySQL();
        new ReviewDBManager(context).importFromMySQL();
        new WriterDBManager(context).importFromMySQL();
    }

    /**
     * Imports all new entries and updated entities from MySQL database into the SQLite one.
     */
    public static void updateDatabase() {

    }

    /**
     * Imports all the MySQL table into the SQLite one.
     */
    public static void importMySQLTable(String table) {
        Context context = App.getAppContext();

        switch (table) {
            case AuthorDBSchema.TABLE:
                new AuthorDBManager(context).importFromMySQL();

                break;
            case CategoryDBSchema.TABLE:
                new CategoryDBManager(context).importFromMySQL();

                break;
            case CityDBSchema.TABLE:
                new CityDBManager(context).importFromMySQL();

                break;
            case CountryDBSchema.TABLE:
                new CountryDBManager(context).importFromMySQL();

                break;
            case BookListTypeDBSchema.TABLE:
                new BookListTypeDBManager(context).importFromMySQL();

                break;
            case ProfileDBSchema.TABLE:
                new ProfileDBManager(context).importFromMySQL();

                break;
            case BookDBSchema.TABLE:
                new BookDBManager(context).importFromMySQL();

                break;
            case UserDBSchema.TABLE:
                new UserDBManager(context).importFromMySQL();

                break;
            case QuoteDBSchema.TABLE:
                new QuoteDBManager(context).importFromMySQL();

                break;
            case ReviewDBSchema.TABLE:
                new ReviewDBManager(context).importFromMySQL();

                break;
            case WriterDBSchema.TABLE:
                new WriterDBManager(context).importFromMySQL();

                break;
            default:
                break;
        }
    }

    /**
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param filter The name of the column to filter on.
     * @param filterValue The value to filter on.
     * @return The value of the field.
     */
    protected final String getFieldSQLite(String field, String filter, String filterValue) {
        try {
            String[] selectArgs = {filterValue};
            String query = String.format(SIMPLE_QUERY_FIELD, field, this.table, filter);
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
     * Adds a JsonArray HTTP request to the queue.
     * @param method The method to use (POST, GET, PUT...).
     * @param url The url to request.
     * @param successListener The instance implementing response listener in order to call the associated callback
     * function.
     */
    protected final void requestJsonArray(int method, String url, Response.Listener<JSONArray> successListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(method,
                url,
                null,
                successListener,
                new OnRequestError());

        HTTPRequestQueueSingleton.getInstance(this.context).addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Adds a JsonObject HTTP request to the queue.
     * @param method The method to use (POST, GET, PUT...).
     * @param url The url to request.
     * @param successListener The instance implementing response listener in order to call the associated callback
     * function.
     */
    protected final void requestJsonObject(int method, String url, Response.Listener<JSONObject> successListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method,
                url,
                null,
                successListener,
                new OnRequestError());

        HTTPRequestQueueSingleton.getInstance(this.context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Adds a String HTTP request to the queue.
     * @param method The method to use (POST, GET, PUT...).
     * @param url The url to request.
     * @param successListener The instance implementing response listener in order to call the associated callback
     * function.
     */
    protected final void requestString(int method, String url, Response.Listener<String> successListener) {
        StringRequest stringRequest = new StringRequest(method, url, successListener, new OnRequestError());

        HTTPRequestQueueSingleton.getInstance(this.context).addToRequestQueue(stringRequest);
    }

    /**
     * From a JSON object creates the associated entity into the database.
     * @param entity The JSON object to store into the database.
     */
    protected abstract void createSQLite(@NonNull JSONObject entity);

    /**
     * Gets the list of id / last_update from the SQLite database.
     * @return A string array of strings containing the id at index 0 and the date at index 1.
     */
    protected String[][] getUpdateFieldsSQLite() {
        try {
            int fieldsNumber = 2;
            String query = String.format(SIMPLE_QUERY_UPDATE, ids[0], this.table);
            Cursor result = DBManager.database.rawQuery(query, null);
            String[][] data = new String[result.getCount()][fieldsNumber];
            int i = 0;

            while (result.moveToNext()) {
                data[i][0] = result.getString(result.getColumnIndex(ids[0]));
                data[i][1] = result.getString(result.getColumnIndex(UPDATE));

                i++;
            }

            result.close();

            return data;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Opens and set the SQLiteDatabase.
     */
    private void open() {
        if (DBManager.database == null) {
            DBManager.database = this.handler.getWritableDatabase();
        }
    }

    /**
     * Updates the indexes depending on the max result number.
     * @param maxIndex The max results number.
     */
    private void updateIndexes(Integer cursorIndexes[], int maxIndex) {
        if (cursorIndexes.length != 2) {
            return;
        }

        cursorIndexes[0] = cursorIndexes[1];
        cursorIndexes[1] = (cursorIndexes[1] + PAGINATION > maxIndex) ? maxIndex : cursorIndexes[1] + PAGINATION;
    }

    /**
     * Closes the database.
     */
    private void close() {
        this.handler.close();
    }

    /**
     * Inner class used to manage HTTP request errors while contacting the API.
     */
    protected final class OnRequestError implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(SERVER_TAG, error.getMessage());
        }
    }
}
