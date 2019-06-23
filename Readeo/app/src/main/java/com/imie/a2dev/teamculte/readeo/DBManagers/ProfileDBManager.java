package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Profile;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.AVATAR;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.DESCRIPTION;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.ERROR_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.JSON_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.SQLITE_TAG;

/**
 * Manager class used to manage the profile entities from databases.
 */
public final class ProfileDBManager extends SimpleDBManager {
    /**
     * ProfileDBManager's constructor.
     * @param context The associated context.
     */
    public ProfileDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
        this.baseUrl = APIManager.API_URL + APIManager.PROFILES;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull Profile entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(AVATAR, entity.getAvatar());
            data.put(DESCRIPTION, entity.getDescription());
            this.database.insertOrThrow(this.table, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull Profile entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(AVATAR, entity.getAvatar());
            data.put(DESCRIPTION, entity.getDescription());
            data.put(UPDATE, new Date().toString());

            return this.database.update(this.table, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From an id, returns the associated java entity.
     * @param id The id of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public Profile loadSQLite(int id) {
        Cursor result = this.loadCursorSQLite(id);

        if (result == null || result.getCount() == 0) {
            return null;
        }

        return new Profile(result);
    }

    /**
     * Query all the profiles from the database.
     * @return The list of profiles.
     */
    public List<Profile> queryAllSQLite() {
        List<Profile> profiles = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    profiles.add(new Profile(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }

        return profiles;
    }

    /**
     * From the API, query the list of all profiles from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(baseUrl + APIManager.READ);
    }

    /**
     * Creates a profile entity in MySQL database.
     * @param profile The profile to create.
     */
    public void createMySQL(final Profile profile) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        if (profile.getId() != 0) {
            param.put(ID, String.valueOf(profile.getId()));
        }

        param.put(AVATAR, profile.getAvatar());
        param.put(DESCRIPTION, profile.getDescription());

        StringRequest request = new StringRequest(Request.Method.POST, url, null, new OnRequestError()) {
            @Override
            protected Map<String, String> getParams() {
                return param;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(ProfileDBManager.this.getContext()).finishRequest(ProfileDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String resp = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Pattern pattern = Pattern.compile("^\\d.$");

                    if (pattern.matcher(resp).find()) {
                        profile.setId(Integer.valueOf(resp));
                    }
                } catch (IOException e) {
                    Log.e(ERROR_TAG, e.getMessage());
                } finally {
                    HTTPRequestQueueSingleton.getInstance(ProfileDBManager.this.getContext()).finishRequest(ProfileDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();
    }

    /**
     * Loads a profile from MySQL database.
     * @param idProfile The id of the profile.
     * @return The loaded profile.
     */
    public Profile loadMySQL(int idProfile) {
        final Profile profile = new Profile();
        String url = this.baseUrl + APIManager.READ + ID + "=" + idProfile;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null, new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers)));
                    JSONObject object = jsonArray.getJSONObject(0);

                    profile.init(object);
                } catch (JSONException e) {
                    Log.e(JSON_TAG, e.getMessage());
                } catch (IOException e) {
                    Log.e(ERROR_TAG, e.getMessage());
                } finally {
                    HTTPRequestQueueSingleton.getInstance(ProfileDBManager.this.getContext()).finishRequest(ProfileDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(ProfileDBManager.this.getContext()).finishRequest(ProfileDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();

        return (profile.isEmpty()) ? null : profile;
    }

    /**
     * Updates a profile entity in MySQL database.
     * @param profile The profile to update.
     */
    public void updateMySQL(Profile profile) {
        String url = this.baseUrl + APIManager.UPDATE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(profile.getId()));
        param.put(AVATAR, profile.getAvatar());
        param.put(DESCRIPTION, profile.getDescription());

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Updates a profile field given in parameter in MySQL database.
     * @param id The id of profile to update.
     * @param field The field of the profile to update.
     * @param value The the new value to set.
     */
    public void updateFieldMySQL(int id, String field, String value) {
        String url = this.baseUrl + APIManager.UPDATE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(id));
        param.put(field, value);

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Deletes a profile entity in MySQL database.
     * @param id The id of the entity to delete.
     */
    public void deleteMySQL(int id) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(id));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Restores a profile entity in MySQL database.
     * @param id The id of the entity to restore.
     */
    public void restoreMySQL(int id) {
        String url = this.baseUrl + APIManager.RESTORE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(id));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Deletes a profile entity in MySQL database.
     * @param profile The profile to delete.
     */
    public void deleteMySQL(Profile profile) {
        this.deleteMySQL(profile.getId());
    }

    @Override
    public void createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(AVATAR, entity.getString(AVATAR));
            data.put(DESCRIPTION, entity.getString(DESCRIPTION));
            this.database.insertOrThrow(this.table, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }

    @Override
    public boolean updateSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{entity.getString(ID)};

            data.put(AVATAR, entity.getString(AVATAR));
            data.put(DESCRIPTION, entity.getString(DESCRIPTION));

            return this.database.update(this.table, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }
}
