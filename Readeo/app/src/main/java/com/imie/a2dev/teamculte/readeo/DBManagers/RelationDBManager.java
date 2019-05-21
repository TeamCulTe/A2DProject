package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.android.volley.Request;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import com.imie.a2dev.teamculte.readeo.Utils.UpdateDataElement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.SQLITE_TAG;

/**
 * Abstract manager class extended by all "double identified entity" managers (with two id).
 */
public abstract class RelationDBManager extends DBManager {
    /**
     * Defines the default field database query with a simple where clause.
     */
    protected final String DOUBLE_QUERY_FIELD = "SELECT %s FROM %s WHERE %s = ? AND %s = ?";

    /**
     * Defines the default all fields database query with a double where clause.
     */
    protected final String DOUBLE_QUERY_ALL = "SELECT * FROM %s WHERE %s = ? AND %s = ?";
    
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
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Deletes the test entity in MySQL database.
     * @param firstId The first part of the id of the test entity to delete.
     * @param secId The second part of the id of the test entity to delete.
     */
    public void deleteTestEntityMySQL(int firstId, int secId) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(this.ids[0], String.valueOf(firstId));
        param.put(this.ids[1], String.valueOf(secId));
        param.put(APIManager.TEST, "1");

        super.requestString(Request.Method.PUT, url, null, param);
        this.waitForResponse();
    }
}
