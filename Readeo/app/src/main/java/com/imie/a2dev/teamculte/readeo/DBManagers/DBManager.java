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
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CityDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CountryDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema;
import com.imie.a2dev.teamculte.readeo.HTTPRequestQueueSingleton;
import com.imie.a2dev.teamculte.readeo.Utils.UpdateDataElement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;

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
    private final String DB_FILE_NAME = "readeo.db";

    // Log tags.

    /**
     * Defines the common error tag.
     */
    public final static String ERROR_TAG = "CommonError";

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
    protected final String QUERY_ALL = "SELECT * FROM %s";

    /**
     * Defines the default all fields database query with paginated results.
     */
    protected final String QUERY_ALL_PAGINATED = "SELECT * FROM %s LIMIT %s OFFSET %s";

    /**
     * Defines the default all fields database query with a simple where clause.
     */
    protected final String SIMPLE_QUERY_ALL = "SELECT * FROM %s WHERE %s = ?";

    /**
     * Defines the default all fields database query with a simple where - like (from start) clause.
     */
    protected final String SIMPLE_QUERY_ALL_LIKE_START = "SELECT * FROM %s WHERE %s LIKE '?%%'";

    /**
     * Defines the default field database query with a simple where clause.
     */
    protected final String SIMPLE_QUERY_FIELD = "SELECT %s FROM %s WHERE %s = ?";

    /**
     * Defines the default field database query with a simple where clause.
     */
    protected final String DOUBLE_QUERY_FIELD = "SELECT %s FROM %s WHERE %s = ? AND %s = ?";

    /**
     * Defines the default all fields database query with a double where clause.
     */
    protected final String DOUBLE_QUERY_ALL = "SELECT * FROM %s WHERE %s = ? AND %s = ?";

    /**
     * Defines the default simple update query (getting the id and the last_update fields) from SQLite db.
     */
    protected final String SIMPLE_QUERY_UPDATE = "SELECT %s, " + UPDATE + " FROM %s";

    /**
     * Defines the default double update query (getting the id and the last_update fields) from SQLite db.
     */
    protected final String DOUBLE_QUERY_UPDATE = "SELECT %s, %s, " + UPDATE + " FROM %s";

    // Update keys

    /**
     * Defines the update key from sync maps.
     */
    protected static final String TO_UPDATE_KEY = "update";

    /**
     * Defines the delete key from sync maps.
     */
    protected static final String TO_DELETE_KEY = "delete";

    /**
     * Defines the create key from sync maps.
     */
    protected static final String TO_CREATE_KEY = "create";

    // Other attributes

    /**
     * Defines the count param and json value from MySQL query alias.
     */
    private final String COUNT = "count";

    /**
     * Defines the default pagination results.
     */
    private final int PAGINATION = 2000;

    /**
     * Stores the manager's API.
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
        this.handler = new DBHandler(context, this.DB_FILE_NAME, null, this.VERSION);
        this.context = context;

        this.open();
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

            return this.database.delete(this.table, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From a list of UpdateElement from local database and a distant one (SQLite - MySQL), checks the ids and date
     * in order to return a map gathering the element to update, create and delete.
     * @param local The local elements list.
     * @param distant The distant elements list.
     * @return The map of elements (create, update, delete).
     */
    public static Map<String, List<int[]>> getSyncData(List<UpdateDataElement> local, List<UpdateDataElement> distant) {
        if (local.size() == 0 || distant.size() == 0 || local.get(0).size() != distant.get(0).size()) {
            return null;
        }

        boolean same;
        UpdateDataElement localElement;
        UpdateDataElement distantElement;

        int syncIdNb = distant.get(0).size();
        Map<String, List<int[]>> syncDataMap = new HashMap<>();
        ArrayList<int[]> toCreateData = new ArrayList<>();
        ArrayList<int[]> toUpdateData = new ArrayList<>();
        ArrayList<int[]> toDeleteData = new ArrayList<>();

        //TODO: Factorize the method / see if better if defined in an update class.
        for (int i = 0, j = 0; i < distant.size() && j < local.size(); i++, j++) {
            same = true;
            localElement = local.get(j);
            distantElement = distant.get(i);

            // Browsing all the ids indexes.
            for (int k = 0; k < syncIdNb; k++) {
                if (localElement.getId(k) != distantElement.getId(k)) {
                    // If the id from distant is higher than the local one, element has to be deleted (unless we
                    // reached the last local element, which means it should be created).
                    if (distantElement.getId(k) > localElement.getId(k) && i < local.size() - 1) {
                        toDeleteData.add(Arrays.copyOfRange(localElement.getIds(), 0, syncIdNb));

                        i--;
                    } else {
                        toCreateData.add(Arrays.copyOfRange(distantElement.getIds(), 0, syncIdNb));

                        j--;
                    }

                    same = false;
                }
            }

            // If the ids are the same, checking the last update date to see if adding into update list.
            if (same) {
                if (localElement.getDateUpdated().before(distantElement.getDateUpdated())) {
                    toUpdateData.add(Arrays.copyOfRange(distantElement.getIds(), 0, syncIdNb));
                }
            }

            // Temporary prevents ArrayOutOfBoundsException or going out of the loop if a list is longer than the other.
            if (i == distant.size() - 1 && j < local.size() - 2) {
                i--;
            } else if (j == local.size() - 1 && i < distant.size() - 2) {
                j--;
            }
        }

        syncDataMap.put(TO_CREATE_KEY, toCreateData);
        syncDataMap.put(TO_UPDATE_KEY, toUpdateData);
        syncDataMap.put(TO_DELETE_KEY, toDeleteData);

        return syncDataMap;
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

            HTTPRequestQueueSingleton.getInstance(DBManager.this.context).finishRequest();
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

            HTTPRequestQueueSingleton.getInstance(DBManager.this.context).finishRequest();
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
        // TODO: Method body and see if needed.
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
     * Gets the list of MySQL ids and last update fields in order to check which entities needs to be updated.
     */
    public void getUpdateFromMySQL() {
        final List<UpdateDataElement> updateFieldsSQLite = this.getUpdateFieldsSQLite();
        this.requestJsonArray(Request.Method.POST, this.baseUrl + APIManager.READ_UPDATE,
                response ->  {
                    List<UpdateDataElement> fieldsMySQL = this.getUpdateFieldsFromJSON(response);
                    Map<String, List<int[]>> syncDataMap = DBManager.getSyncData(updateFieldsSQLite, fieldsMySQL);

                    this.performDbUpdates(syncDataMap);
                });
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
            Cursor result = this.database.rawQuery(query, selectArgs);

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
     * Gets the list of id(s) / last_update from the SQLite database.
     * @return A list of UpdateDataElements.
     */
    protected final List<UpdateDataElement> getUpdateFieldsSQLite() {
        try {
            String query;

            int idsNumber = this.ids.length;
            SimpleDateFormat dateFormat = new SimpleDateFormat(CommonDBSchema.DATE_FORMAT);

            if (idsNumber == 1) {
                query = String.format(SIMPLE_QUERY_UPDATE, ids[0], this.table);
            } else if (idsNumber == 2) {
                query = String.format(this.DOUBLE_QUERY_UPDATE, ids[0], ids[1], this.table);
            } else {
                // No case yet.
                return null;
            }

            Cursor result = this.database.rawQuery(query, null);
            List<UpdateDataElement> data = new ArrayList<>();

            while (result.moveToNext()) {
                int[] idsToAdd = new int[idsNumber];

                for (int j = 0; j < idsNumber; j++) {
                    idsToAdd[j] = result.getInt(result.getColumnIndex(this.ids[j]));
                }

                try {
                    data.add(new UpdateDataElement(idsToAdd,
                            dateFormat.parse(result.getString(result.getColumnIndex(UPDATE)))));
                } catch (ParseException e) {
                    Log.e(ERROR_TAG, e.getMessage());
                }
            }

            result.close();

            return data;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a json update response (ids and last update date), gets the values and returns them into a list of
     * UpdateDataElements.
     * @param result The JSON response containing the ids and last update date.
     * @return The result into a list of UpdateDataElements.
     */
    protected final List<UpdateDataElement> getUpdateFieldsFromJSON(JSONArray result) {
        JSONObject elt;

        List<UpdateDataElement> data = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(CommonDBSchema.DATE_FORMAT);

        for (int i = 0; i < result.length(); i++) {
            try {
                elt = result.getJSONObject(i);
                int[] idsToAdd = new int[this.ids.length];

                for (int j = 0; j < idsToAdd.length; j++) {
                    idsToAdd[j] = elt.getInt(this.ids[j]);
                }

                try {
                    data.add(new UpdateDataElement(idsToAdd, dateFormat.parse(elt.getString(UPDATE))));
                } catch (ParseException e) {
                    Log.e(ERROR_TAG, e.getMessage());
                }
            } catch (JSONException e) {
                Log.e(JSON_TAG, e.getMessage());

                return null;
            }
        }

        return data;
    }

    /**
     * From a map containing the elements to create, update, delete, perform the associated operations.
     * @param syncDataMap The map containing the elements.
     */
    protected void performDbUpdates(Map<String, List<int[]>> syncDataMap) {
        for (int[] elt : syncDataMap.get(TO_CREATE_KEY)) {
            this.requestJsonArray(Request.Method.POST,
                    this.baseUrl + APIManager.READ + this.ids[0] + "=" + String.valueOf(elt[0]),
                    response -> {
                        try {
                            this.createSQLite(response.getJSONObject(0));
                        } catch (JSONException e) {
                            Log.e(JSON_TAG, e.getMessage());
                        }
                    });
        }

        for (int[] elt : syncDataMap.get(TO_UPDATE_KEY)) {
            this.requestJsonArray(Request.Method.POST,
                    this.baseUrl + APIManager.READ + this.ids[0] + "=" + String.valueOf(elt[0]),
                    response -> {
                        try {
                            this.updateSQLite(response.getJSONObject(0));
                        } catch (JSONException e) {
                            Log.e(JSON_TAG, e.getMessage());
                        }
                    });
        }

        for (int[] elt : syncDataMap.get(TO_DELETE_KEY)) {
            this.deleteSQLite(elt[0]);
        }
    }

    /**
     * From a JSON object creates the associated entity into the database.
     * @param entity The JSON object to store into the database.
     */
    protected abstract void createSQLite(@NonNull JSONObject entity);

    /**
     * From a JSON object updates the associated entity into the database.
     * @param entity The JSON object to update into the database.
     * @return true if success else false.
     */
    protected abstract boolean updateSQLite(@NonNull JSONObject entity);

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
            Log.e(SERVER_TAG, error.getMessage());
        }
    }
}
