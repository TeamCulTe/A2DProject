package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

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

    /**
     * Defines the log tag.
     */
    protected final static String TAG = "SQLiteError";

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
     * Stores the manager database in order to manage.
     */
    protected SQLiteDatabase database;

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
    public SQLiteDatabase getDatabase() {
        return this.database;
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
     * Sets the database attribute.
     * @param newDatabase The new SQLiteDatabase value to set.
     */
    public void setDatabase(SQLiteDatabase newDatabase) {
        this.database = newDatabase;
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
     * Opens and set the SQLiteDatabase.
     */
    private void open() {
        this.database = this.handler.getWritableDatabase();
    }

    /**
     * Closes the database.
     */
    public void close() {
        this.handler.close();
    }

    /**
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param table The concerned database table.
     * @param filter The name of the column to filter on.
     * @param filterValue The value to filter on.
     * @return The value of the field.
     */
    public String SQLiteGetField(String field, String table, String filter, String filterValue) {
        try {
            String[] selectArgs = {filterValue};
            String query = String.format(SIMPLE_QUERY_FIELD, field, table, filter);
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
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param table The concerned database table.
     * @param idColumn The name of the id column to filter on.
     * @param id The id of the db entity to access.
     * @return The value of the field.
     */
    public String SQLiteGetField(String field, String table, String idColumn, int id) {
        return this.SQLiteGetField(field, table, idColumn, String.valueOf(id));
    }
}
