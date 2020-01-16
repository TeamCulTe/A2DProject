package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.android.volley.Request;
import com.imie.a2dev.teamculte.readeo.APIManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract manager class extended by all "double identified entity" managers (with two id).
 */
public abstract class RelationDBManager extends DBManager {
    /**
     * Defines the default field database query with a simple where clause.
     */
    protected final String DOUBLE_QUERY_FIELD = "SELECT %s FROM %s WHERE %s = ? AND %s = ?";

    /**
     * DBManager's full filled constructor initializing the handler attribute and opening the database.
     * @param context The associated context.
     */
    public RelationDBManager(Context context) {
        super(context);
    }

    /**
     * From an id, returns the associated java entity as a cursor (relation entities).
     * @param firstId The first id of entity to load from the database.
     * @param secId The second id of entity to load from the database.
     * @return The cursor of the loaded entity.
     */
    public Cursor loadCursorSQLite(int firstId, int secId) {
        try {
            String[] selectArgs = {String.valueOf(firstId), String.valueOf(secId)};
            String query = String.format(this.DOUBLE_QUERY_ALL, this.table, this.ids[0], this.ids[1]);

            return this.database.rawQuery(query, selectArgs);
        } catch (SQLiteException e) {
            this.logError("loadCursorSQLite", e);

            return null;
        }
    }

    /**
     * Deletes the test entity in MySQL database.
     * @param ids The ids of the test entity to delete.
     */
    public void deleteTestEntityMySQL(int... ids) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        for (int i = 0; i < ids.length; ++i) {
            param.put(this.ids[i], String.valueOf(ids[i]));
        }
        
        param.put(APIManager.TEST, "1");

        super.requestString(Request.Method.PUT, url, null, param);
        this.waitForResponse();
    }
}
