package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.android.volley.Request;
import com.imie.a2dev.teamculte.readeo.APIManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract manager class extended by all "simple id entity" managers (with only one id).
 */
public abstract class SimpleDBManager extends DBManager {
    /**
     * Defines the default all fields database query with a simple where - like (from start) clause.
     */
    protected final String SIMPLE_QUERY_ALL_LIKE_START = "SELECT * FROM %s WHERE %s LIKE ?||'%%'";

    /**
     * Defines the default all fields database query with a simple where - like (from start) clause.
     */
    protected final String SIMPLE_QUERY_ALL_LIKE_START_PAGINATED = "SELECT * FROM %s WHERE %s LIKE ?||'%%' LIMIT %d " +
                                                                   "OFFSET %d";

    /**
     * Defines the default field database query with a simple where clause.
     */
    protected final String SIMPLE_QUERY_FIELD = "SELECT %s FROM %s WHERE %s = ?";

    /**
     * SimpleDBManager's full filled constructor initializing the handler attribute and opening the database.
     * @param context The associated context.
     */
    public SimpleDBManager(Context context) {
        super(context);
    }

    /**
     * Queries the value of a specific field from a specific filter.
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
            this.logError("getFieldSQLite", e);

            return null;
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
     * From an id, returns the associated java entity as a cursor (simple entities).
     * @param id The id of entity to load from the database.
     * @return The cursor of the loaded entity.
     */
    public Cursor loadCursorSQLite(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(this.SIMPLE_QUERY_ALL, this.table, this.ids[0]);

            return this.database.rawQuery(query, selectArgs);
        } catch (SQLiteException e) {
            this.logError("loadCursorSQLite", e);

            return null;
        }
    }

    /**
     * Deletes the test entity in MySQL database.
     * @param id The id of the test entity to delete.
     */
    public void deleteTestEntityMySQL(int id) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(this.ids[0], String.valueOf(id));
        param.put(APIManager.TEST, "1");

        super.requestString(Request.Method.PUT, url, null, param);
        this.waitForResponse();
    }
}
