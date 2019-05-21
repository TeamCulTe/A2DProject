package com.imie.a2dev.teamculte.readeo.Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.android.volley.Request;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema;
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
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.ERROR_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.JSON_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.SQLITE_TAG;

/**
 * Class used to update from distant database to locale.
 */
public final class UpdaterUtils {
    /**
     * Stores the default date format.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(CommonDBSchema.DATE_FORMAT);
    
    /**
     * Defines the update key from sync maps.
     */
    private static final String TO_UPDATE_KEY = "update";

    /**
     * Defines the delete key from sync maps.
     */
    private static final String TO_DELETE_KEY = "delete";

    /**
     * Defines the create key from sync maps.
     */
    private static final String TO_CREATE_KEY = "create";

    /**
     * From a json update response (ids and last update date), gets the values and returns them into a list of
     * UpdateDataElements.
     * @param result The JSON response containing the ids and last update date.
     * @param manager The associated DBManager.
     * @return The result into a list of UpdateDataElements.
     */
    public static List<UpdateDataElement> getUpdateFieldsFromJSON(JSONArray result, DBManager manager) {
        JSONObject elt;

        List<UpdateDataElement> data = new ArrayList<>();

        for (int i = 0; i < result.length(); i++) {
            try {
                elt = result.getJSONObject(i);
                int[] idsToAdd = new int[manager.getIds().length];

                for (int j = 0; j < idsToAdd.length; j++) {
                    idsToAdd[j] = elt.getInt(manager.getIds()[j]);
                }

                try {
                    data.add(new UpdateDataElement(idsToAdd, DATE_FORMAT.parse(elt.getString(UPDATE))));
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
     * Gets the list of MySQL ids and last update fields in order to check which entities needs to be updated, then 
     * compare and perform the update.
     * @param manager The associated DBManager.
     */
    public static void getUpdateFromMySQL(final DBManager manager) {
        final List<UpdateDataElement> updateFieldsSQLite = UpdaterUtils.getUpdateFieldsSQLite(manager);

        manager.requestJsonArray(Request.Method.GET, manager.getBaseUrl() + APIManager.READ_UPDATE,
                response -> {
                    List<UpdateDataElement> fieldsMySQL = UpdaterUtils.getUpdateFieldsFromJSON(response, manager);
                    Map<String, List<int[]>> syncDataMap = UpdaterUtils.getSyncData(updateFieldsSQLite, fieldsMySQL);

                    UpdaterUtils.performDbUpdates(syncDataMap, manager);

                    HTTPRequestQueueSingleton.getInstance(manager.getContext()).finishRequest(manager.getTable());
                });
    }
    
    /**
     * Gets the list of id(s) / last_update from the SQLite database.
     * @param manager The associated DBManager.
     * @return A list of UpdateDataElements.
     */
    public static List<UpdateDataElement> getUpdateFieldsSQLite(final DBManager manager) {
        try {
            int idsNumber = manager.getIds().length;
            StringBuilder builder = new StringBuilder("SELECT ");
            
            for (int i = 0; i < idsNumber; i++) {
                builder.append(manager.getIds()[i]);
                builder.append(", ");
            }
            
            builder.append(UPDATE);
            builder.append(" FROM ");
            builder.append(manager.getTable());
            builder.append(" ORDER BY ");
            builder.append(manager.getIds()[0]);
            
            Cursor result = manager.getDatabase().rawQuery(builder.toString(), null);
            List<UpdateDataElement> data = new ArrayList<>();

            while (result.moveToNext()) {
                int[] idsToAdd = new int[idsNumber];

                for (int j = 0; j < idsToAdd.length; j++) {
                    idsToAdd[j] = result.getInt(result.getColumnIndex(manager.getIds()[j]));
                }

                try {
                    data.add(new UpdateDataElement(idsToAdd,
                            DATE_FORMAT.parse(result.getString(result.getColumnIndex(UPDATE)))));
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
     * From a map containing the elements to create, update, delete, perform the associated operations.
     * @param syncDataMap The map containing the elements.
     * @param manager The associated DBManager.
     */
    public static void performDbUpdates(Map<String, List<int[]>> syncDataMap, final DBManager manager) {
        String url;
        
        for (int[] elt : syncDataMap.get(TO_CREATE_KEY)) {
            url = UpdaterUtils.buildUpdateUrl(manager, elt);
            
            manager.requestJsonArray(Request.Method.POST, url, response -> {
                try {
                    manager.createSQLite(response.getJSONObject(0));
                } catch (JSONException e) {
                    Log.e(JSON_TAG, e.getMessage());
                } finally {
                    HTTPRequestQueueSingleton.getInstance(manager.getContext()).finishRequest(
                            manager.getTable());
                }
            });
        }

        for (int[] elt : syncDataMap.get(TO_UPDATE_KEY)) {
            url = UpdaterUtils.buildUpdateUrl(manager, elt);
            
            manager.requestJsonArray(Request.Method.POST, url, response -> {
                try {
                    manager.updateSQLite(response.getJSONObject(0));
                } catch (JSONException e) {
                    Log.e(JSON_TAG, e.getMessage());
                } finally {
                    HTTPRequestQueueSingleton.getInstance(manager.getContext()).finishRequest(
                            manager.getTable());
                }
            });
        }

        for (int[] elt : syncDataMap.get(TO_DELETE_KEY)) {
            manager.deleteSQLite(elt[0], elt[1]);
        }
    }

    /**
     * From a list of UpdateElement from local database and a distant one (SQLite - MySQL), checks the ids and date
     * in order to return a map gathering the element to update, create and delete.
     * @param local The local elements list.
     * @param distant The distant elements list.
     * @return The map of elements (create, update, delete).
     */
    private static Map<String, List<int[]>> getSyncData(List<UpdateDataElement> local, List<UpdateDataElement> distant) {
        if (local.size() == 0 || distant.size() == 0 || local.get(0).size() != distant.get(0).size()) {
            return null;
        }
        
        boolean same;
        boolean added;
        UpdateDataElement localElement;
        UpdateDataElement distantElement;

        final int syncIdNb = distant.get(0).size();
        final UpdateDataElement element = new UpdateDataElement();
        Map<String, List<int[]>> syncDataMap = new HashMap<>();
        ArrayList<int[]> toCreateData = new ArrayList<>();
        ArrayList<int[]> toUpdateData = new ArrayList<>();
        ArrayList<int[]> toDeleteData = new ArrayList<>();

        //TODO: Factorize the method + fix elements order issue => having same elements in toCreate and toDelete.
        for (int i = 0, j = 0; i < distant.size() && j < local.size(); i++, j++) {
            same = true;
            added = false;
            localElement = local.get(j);
            distantElement = distant.get(i);

            // Browsing all the ids indexes.
            for (int k = 0; k < syncIdNb; k++) {
                if (localElement.getId(k) != distantElement.getId(k)) {
                    // If the id from distant is higher than the local one, element has to be deleted (unless we
                    // reached the last local element, which means it should be created).
                    if (distantElement.getId(k) > localElement.getId(k)) {
                        element.setIds(Arrays.copyOfRange(localElement.getIds(), 0, syncIdNb));
                        
                        if (toCreateData.stream().anyMatch(a -> Arrays.equals(a, element.getIds()))) {
                            toDeleteData.add(element.getIds());
                        }
                    } else {
                        element.setIds(Arrays.copyOfRange(distantElement.getIds(), 0, syncIdNb));

                        if (toDeleteData.stream().anyMatch(a -> Arrays.equals(a, element.getIds()))) {
                            toCreateData.add(element.getIds());
                        }
                        
                        added = true;
                    }

                    same = false;

                    break;
                }
            }

            // If the ids are the same, checking the last update date to see if adding into update list.
            if (same) {
                if (localElement.getDateUpdated().before(distantElement.getDateUpdated())) {
                    toUpdateData.add(Arrays.copyOfRange(distantElement.getIds(), 0, syncIdNb));
                }
            } else {
                if (added) {
                    j--;
                } else {
                    i--;
                }
            }
        }
        
        syncDataMap.put(TO_CREATE_KEY, toCreateData);
        syncDataMap.put(TO_UPDATE_KEY, toUpdateData);
        syncDataMap.put(TO_DELETE_KEY, toDeleteData);

        return syncDataMap;
    }

    /**
     * Builds the update url to contact the API depending on the manager given in parameter. 
     * @param manager The associated manager.
     * @param updateElts The update elements (ids).
     * @return The update url that can be used to contact the API.
     */
    private static String buildUpdateUrl(DBManager manager, int[] updateElts) {
        StringBuilder builder = new StringBuilder(manager.getBaseUrl() + APIManager.READ);
        int idsNb = manager.getIds().length;
        
        if (idsNb != updateElts.length) {
            Log.e(ERROR_TAG, "The number of id fields from the manager differs from the update elements.");
        }

        for (int i = 0; i < idsNb; i++) {
            builder.append(manager.getIds()[i]);
            builder.append("=");
            builder.append(String.valueOf(updateElts[i]));
            
            if (i < (idsNb - 1)) {
                builder.append("&");
            }
        }

        return builder.toString();
    }
}
