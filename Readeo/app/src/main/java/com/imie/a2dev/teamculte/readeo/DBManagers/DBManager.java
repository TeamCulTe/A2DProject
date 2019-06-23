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
import com.android.volley.toolbox.StringRequest;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.ERROR_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.JSON_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.SERVER_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.SQLITE_TAG;

/**
 * Abstract class extended by all DBManager classes (used to manage entities into databases).
 */
public abstract class DBManager {
    /**
     * Defines the database version.
     */
    private final int VERSION = 1;

    /**
     * Defines the database file name.
     */
    protected static String dbFileName = "readeo.db";

    // Predefined queries.

    /**
     * Defines the default all fields database query.
     */
    protected final String QUERY_ALL = "SELECT * FROM %s";

    /**
     * Defines the default all fields database query with paginated results.
     */
    protected final String QUERY_ALL_PAGINATED = "SELECT * FROM %s LIMIT %s OFFSET %s";

    /**
     * Defines the default all fields database query with a simple where clause.
     */
    protected final String SIMPLE_QUERY_ALL = "SELECT * FROM %s WHERE %s = ?";

    // Other attributes

    /**
     * Stores the default id value for tests on MySQL database (as big as possible).
     */
    public static final int MYSQL_TEST_ID = -666;

    /**
     * Defines the count param and json value from MySQL query alias.
     */
    private final String COUNT = "count";

    /**
     * Defines the default pagination results.
     */
    private final int PAGINATION = 2000;

    /**
     * Stores the manager's API url.
     */
    protected String baseUrl;

    /**
     * Stores the managers database in order to manage.
     */
    protected SQLiteDatabase database;

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
        this.handler = new DBHandler(context, dbFileName, null, this.VERSION);
        this.context = context;

        this.open();
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();

        super.finalize();
    }

    /**
     * Gets the database attribute.
     * @return The SQLiteDatabase value of database attribute.
     */
    public SQLiteDatabase getDatabase() {
        return this.database;
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
     * Gets the baseUrl attribute.
     * @return The String value of baseUrl attribute.
     */
    public final String getBaseUrl() {
        return this.baseUrl;
    }

    /**
     * Gets the ids attribute.
     * @return The String value of ids attribute.
     */
    public String[] getIds() {
        return this.ids;
    }

    /**
     * Gets the table attribute.
     * @return The String value of table attribute.
     */
    public final String getTable() {
        return this.table;
    }

    /**
     * Returns the database file name.
     * @return The name of the file.
     */
    public static String getDbFileName() {
        return dbFileName;
    }

    /**
     * Sets the database file name.
     * @param newDbFileName The new name to set.
     */
    public static void setDbFileName(String newDbFileName) {
        dbFileName = newDbFileName;
    }

    /**
     * Loops while there are no response received from API (fix the asynchronous mechanism issue).
     */
    public void waitForResponse() {
        HTTPRequestQueueSingleton httpRequestQueueSingleton = HTTPRequestQueueSingleton.getInstance(this.context);

        while (httpRequestQueueSingleton.hasRequestPending(this.table)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.e(ERROR_TAG, "Error while sleeping the thread.");
            }
        }
    }

    /**
     * Queries the number of entries for a specific table.
     * @return The number of entries or -1 if an error occurred.
     */
    public final int countSQLite() {
        try {
            String[] selectArgs = {};
            String query = String.format("SELECT COUNT(*) as %s FROM %s", COUNT, this.table);
            Cursor result = this.database.rawQuery(query, selectArgs);

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
     * From an id given in parameter, deletes the associated entity in the database.
     * @param ids The ids of the entity to delete.
     * @return true if success else false.
     */
    public boolean deleteSQLite(int ... ids) {
        try {
            StringBuilder builder = new StringBuilder();
            String[] whereArgs = new String[ids.length];

            for (int i = 0; i < ids.length; i++) {
                builder.append(this.ids[i]);
                builder.append(" = ?");

                whereArgs[i] = String.valueOf(ids[i]);

                if (i < (ids.length - 1)) {
                    builder.append(" AND ");
                }
            }

            return this.database.delete(this.table, builder.toString(), whereArgs) != 0;
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
        this.requestJsonArray(Request.Method.GET, url, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    DBManager.this.createSQLite(response.getJSONObject(i));
                } catch (JSONException e) {
                    Log.e(JSON_TAG, e.getMessage());
                }
            }

            HTTPRequestQueueSingleton.getInstance(DBManager.this.context).finishRequest(this.table);
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

        this.requestJsonArray(Request.Method.GET, url + COUNT, response -> {
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

            HTTPRequestQueueSingleton.getInstance(DBManager.this.context).finishRequest(this.table);
        });
    }

    /**
     * Deletes all the test entities in MySQL database.
     */
    public void deleteMySQLTestEntities() {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(APIManager.TEST, "1");

        this.requestString(Request.Method.PUT, url, null, param);
        this.waitForResponse();
    }
    
    /**
     * From a JSON object creates the associated entity into the database.
     * @param entity The JSON object to store into the database.
     */
    public abstract void createSQLite(@NonNull JSONObject entity);

    /**
     * From a JSON object updates the associated entity into the database.
     * @param entity The JSON object to update into the database.
     * @return true if success else false.
     */
    public abstract boolean updateSQLite(@NonNull JSONObject entity);

    /**
     * Adds a JsonArray HTTP request to the queue.
     * @param method The method to use (POST, GET, PUT...).
     * @param url The url to request.
     * @param successListener The instance implementing response listener in order to call the associated callback
     * function.
     */
    public final void requestJsonArray(int method, String url, Response.Listener<JSONArray> successListener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(method,
                url,
                null,
                successListener,
                new OnRequestError());

        HTTPRequestQueueSingleton.getInstance(this.context).addToRequestQueue(this.table,
                jsonArrayRequest);
    }

    /**
     * Adds a String HTTP request to the queue.
     * @param method The method to use (POST, GET, PUT...).
     * @param url The url to request.
     * @param successListener The instance implementing response listener in order to call the associated callback.
     * @param params The param to send (POST and PUT requests).
     *
     */
    public final void requestString(int method,
                                       String url,
                                       Response.Listener<String> successListener,
                                       final Map<String, String> params) {
        if (successListener == null) {
            successListener = response -> HTTPRequestQueueSingleton.getInstance(DBManager.this.context).finishRequest(this.table);
        }

        StringRequest stringRequest = new StringRequest(method, url, successListener, new OnRequestError())
        {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.context).addToRequestQueue(this.table, stringRequest);
    }
    
    /**
     * Opens and set the SQLiteDatabase.
     */
    private void open() {
        if (this.database == null) {
            this.database = this.handler.getWritableDatabase();
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
            HTTPRequestQueueSingleton requestQueueSingleton =
                    HTTPRequestQueueSingleton.getInstance(DBManager.this.context);
            requestQueueSingleton.finishRequest(DBManager.this.getTable());

            String statusCode = (error.networkResponse != null) ? String.valueOf(error.networkResponse.statusCode) :
                    "unexpected";
            String requested = requestQueueSingleton.getLastRequestUrl();
            Log.e(SERVER_TAG,
                    "Error " + statusCode + " while contacting the API at request : \n" + requested);
        }
    }
}
