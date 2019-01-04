package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Response;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Profile;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class used to manage the profile entities from databases.
 */
public final class ProfileDBManager extends DBManager {
    /**
     * Defines the profile's table name.
     */
    public static final String TABLE = "Profile";

    /**
     * Defines the profile's id field.
     */
    public static final String ID = "id_profile";

    /**
     * Defines the profile's avatar field.
     */
    public static final String AVATAR = "avatar";

    /**
     * Defines the profile's description field.
     */
    public static final String DESCRIPTION = "description";

    /**
     * Stores the base of the profiles API url.
     */
    private final String baseUrl = APIManager.API_URL + APIManager.PROFILES;

    /**
     * ProfileDBManager's constructor.
     * @param context The associated context.
     */
    public ProfileDBManager(Context context) {
        super(context);
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
            this.database.insertOrThrow(TABLE, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param id The id of the db entity to access.
     * @return The value of the field.
     */
    public String getFieldSQLite(String field, int id) {
        return this.getFieldSQLite(field, TABLE, ID, id);
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

            return this.database.update(TABLE, data, whereClause, whereArgs) != 0;
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
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, ID);
            Cursor result = this.database.rawQuery(query, selectArgs);

            return new Profile(result);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From an id given in parameter, deletes the associated entity in the database.
     * @param id The id of the entity to delete.
     * @return true if success else false.
     */
    public boolean deleteSQLite(int id) {
        try {
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Query all the profiles from the database.
     * @return The list of profiles.
     */
    public List<Profile> queryAllSQLite() {
        List<Profile> profiles = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(QUERY_ALL, TABLE), null);

            while (result.moveToNext()) {
                profiles.add(new Profile(result));
            }

        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }

        return profiles;
    }

    /**
     * From the API, query the list of all profiles from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importAllFromMySQL() {
        super.importAllFromMySQL(baseUrl + APIManager.READ);
    }

    /**
     * Creates a profile entity in MySQL database.
     * @param profile The profile to create.
     */
    public void createMySQL(Profile profile) {
        String url = String.format(baseUrl + APIManager.CREATE + AVATAR + "=%s&" + DESCRIPTION + "=%s",
                profile.getAvatar(),
                profile.getDescription());

        super.requestString(url, null);
    }

    /**
     * Updates a profile entity in MySQL database.
     * @param profile The profile to update.
     */
    public void updateMySQL(Profile profile) {
        String url = String.format(baseUrl + APIManager.UPDATE + ID + "=%s&" + AVATAR + "=%s&" + DESCRIPTION + "=%s",
                profile.getId(),
                profile.getAvatar(),
                profile.getDescription());

        super.requestString(url, null);
    }

    /**
     * Updates a profile field given in parameter in MySQL database.
     * @param id The id of profile to update.
     * @param field The field of the profile to update.
     * @param value The the new value to set.
     */
    public void updateFieldMySQL(int id, String field, String value) {
        String url = String.format(baseUrl + APIManager.UPDATE + ID + "=%s&" + field + "=%s",
                id,
                value);

        super.requestString(url, null);
    }

    /**
     * Loads a profile from MySQL database.
     * @param id The id of the profile to load.
     * @return The loaded profile.
     */
    public Profile loadMySQL(int id) {
        final Profile profile = new Profile();

        String url = String.format(baseUrl + APIManager.READ + ID + "=%s", id);

        super.requestJsonObject(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                profile.init(response);
            }
        });

        return profile;
    }

    /**
     * Deletes a profile entity in MySQL database.
     * @param id The id of the entity to delete.
     */
    public void deleteMySQL(int id) {
        String url = String.format(baseUrl + APIManager.DELETE + ID + "=%s", id);

        super.requestString(url, null);
    }

    /**
     * Restores a profile entity in MySQL database.
     * @param id The id of the entity to restore.
     */
    public void restoreMySQL(int id) {
        String url = String.format(baseUrl + APIManager.RESTORE + ID + "=%s", id);

        super.requestString(url, null);
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
            this.database.insertOrThrow(TABLE, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
