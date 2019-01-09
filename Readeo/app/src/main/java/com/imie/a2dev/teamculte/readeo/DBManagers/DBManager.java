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
import com.imie.a2dev.teamculte.readeo.HTTPRequestQueueSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
     * Defines the default all fields database query with a simple where clause.
     */
    protected final static String SIMPLE_QUERY_ALL = "SELECT * FROM %s WHERE %s = ?";

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
     * Stores the managers database in order to manage.
     */
    protected static SQLiteDatabase database;

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
     * Overriding destructor in order to close the database object.
     */
    public void finalize() {
        this.close();
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
    public DBHandler getHandler() {
        return this.handler;
    }

    /**
     * Gets the context attribute.
     * @return The Context value of context attribute.
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Sets the handler attribute.
     * @param newHandler The new DBHandler value to set.
     */
    public void setHandler(DBHandler newHandler) {
        this.handler = newHandler;
    }

    /**
     * Sets the context attribute.
     * @param newContext The new Context value to set.
     */
    public void setContext(Context newContext) {
        this.context = newContext;
    }

    /**
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param table The concerned database table.
     * @param filter The name of the column to filter on.
     * @param filterValue The value to filter on.
     * @return The value of the field.
     */
    public String getFieldSQLite(String field, String table, String filter, String filterValue) {
        try {
            String[] selectArgs = {filterValue};
            String query = String.format(SIMPLE_QUERY_FIELD, field, table, filter);
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
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param table The concerned database table.
     * @param idColumn The name of the id column to filter on.
     * @param id The id of the db entity to access.
     * @return The value of the field.
     */
    public String getFieldSQLite(String field, String table, String idColumn, int id) {
        return this.getFieldSQLite(field, table, idColumn, String.valueOf(id));
    }

    /**
     * From the API, query the list of all entities from the MySQL database in order to stores it into the SQLite
     * database.
     * @param url The url to request.
     */
    public void importAllFromMySQL(String url) {
        this.requestJsonArray(url, new Response.Listener<JSONArray>() {
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
     * Imports all the MySQL database into the SQLite one.
     */
    public static void importMySQLDatabase() {
        Context context = App.getAppContext();

        new AuthorDBManager(context).importAllFromMySQL();
        new CategoryDBManager(context).importAllFromMySQL();
        new CityDBManager(context).importAllFromMySQL();
        new CountryDBManager(context).importAllFromMySQL();
        new BookListTypeDBManager(context).importAllFromMySQL();
        new ProfileDBManager(context).importAllFromMySQL();
        new BookDBManager(context).importAllFromMySQL();
        new UserDBManager(context).importAllFromMySQL();
        new QuoteDBManager(context).importAllFromMySQL();
        new ReviewDBManager(context).importAllFromMySQL();
        new WriterDBManager(context).importAllFromMySQL();
    }

    /**
     * Adds a JsonArray HTTP request to the queue.
     * @param url The url to request.
     * @param successListener The instance implementing response listener in order to call the associated callback
     * function.
     */
    protected void requestJsonArray(String url, Response.Listener<JSONArray> successListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST,
                url,
                null,
                successListener,
                new OnRequestError());

        HTTPRequestQueueSingleton.getInstance(this.context).addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Adds a JsonObject HTTP request to the queue.
     * @param url The url to request.
     * @param successListener The instance implementing response listener in order to call the associated callback
     * function.
     */
    protected void requestJsonObject(String url, Response.Listener<JSONObject> successListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                null,
                successListener,
                new OnRequestError());

        HTTPRequestQueueSingleton.getInstance(this.context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Adds a String HTTP request to the queue.
     * @param url The url to request.
     * @param successListener The instance implementing response listener in order to call the associated callback
     * function.
     */
    protected void requestString(String url, Response.Listener<String> successListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, successListener, new OnRequestError());

        HTTPRequestQueueSingleton.getInstance(this.context).addToRequestQueue(stringRequest);
    }

    /**
     * Inner class used to manage HTTP request errors while contacting the API.
     */
    protected class OnRequestError implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(SERVER_TAG, error.getMessage());
        }
    }

    /**
     * From a JSON object creates the associated entity into the database.
     * @param entity The JSON object to store into the database.
     */
    protected abstract void createSQLite(@NonNull JSONObject entity);

    /**
     * Opens and set the SQLiteDatabase.
     */
    private void open() {
        if (DBManager.database == null) {
            DBManager.database = this.handler.getWritableDatabase();
        }
    }

    /**
     * Closes the database.
     */
    private void close() {
        this.handler.close();
    }
}
