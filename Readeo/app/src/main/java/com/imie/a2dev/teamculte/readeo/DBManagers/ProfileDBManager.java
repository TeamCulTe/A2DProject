package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Profile;
import com.imie.a2dev.teamculte.readeo.HTTPRequestQueueSingleton;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.AVATAR;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.DESCRIPTION;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.TABLE;

/**
 * Manager class used to manage the profile entities from databases.
 */
public final class ProfileDBManager extends DBManager {
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
        try {
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(this.SIMPLE_QUERY_ALL, this.table, ID);
            Cursor result = this.database.rawQuery(query, selectArgs);

            if (result.getCount() == 0) {
                return null;
            }

            return new Profile(result);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
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
    public void createMySQL(Profile profile) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        if (profile.getId() != 0) {
            param.put(ID, String.valueOf(profile.getId()));
        }

        param.put(AVATAR, profile.getAvatar());
        param.put(DESCRIPTION, profile.getDescription());

        super.requestString(Request.Method.POST, url, null, param);
    }

    /**
     * Loads a profile from MySQL database.
     * @param idProfile The id of the profile.
     * @return The loaded profile.
     */
    public Profile loadMySQL(int idProfile) {
        final Profile profile = new Profile();
        String url = this.baseUrl + APIManager.READ + ID + "=" + idProfile;

        super.requestJsonArray(Request.Method.GET, url,  response -> {
            try {
                profile.init(response.getJSONObject(0));
                HTTPRequestQueueSingleton.getInstance(ProfileDBManager.this.getContext()).finishRequest(this.getClass().getName());
            } catch (JSONException e) {
                Log.e(JSON_TAG, e.getMessage());
            }
        });

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
    protected void createSQLite(@NonNull JSONObject entity) {
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
