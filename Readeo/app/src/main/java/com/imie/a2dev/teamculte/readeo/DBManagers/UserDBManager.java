package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.CITY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.COUNTRY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.EMAIL;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PASSWORD;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PROFILE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PSEUDO;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.JSON_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.SQLITE_TAG;

/**
 * Manager class used to manage the user entities from databases.
 */
public final class UserDBManager extends SimpleDBManager {
    /**
     * UserDBManager's constructor.
     * @param context The associated context.
     */
    public UserDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
        this.baseUrl = APIManager.API_URL + APIManager.USERS;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull PublicUser entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(PSEUDO, entity.getPseudo());
            data.put(PROFILE, entity.getProfile().getId());
            this.database.insertOrThrow(this.table, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Queries the value of a specific field from a specific pseudo.
     * @param field The field to access.
     * @param pseudo The pseudo of the db entity to access.
     * @return The value of the field.
     */
    public String getFieldFromPseudoSQLite(String field, String pseudo) {
        return this.getFieldSQLite(field, PSEUDO, pseudo);
    }

    /**
     * From a user pseudo, gets its associated id.
     * @param pseudo The pseudo of the user.
     * @return The id associated to the pseudo.
     */
    public int SQLiteGetId(String pseudo) {
        return Integer.valueOf(this.getFieldFromPseudoSQLite(ID, pseudo));
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull PublicUser entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(ID, entity.getId());
            data.put(PSEUDO, entity.getPseudo());
            data.put(PROFILE, entity.getProfile().getId());
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
    public PublicUser loadSQLite(int id) {
        Cursor result = this.loadCursorSQLite(id);

        if (result == null || result.getCount() == 0) {
            return null;
        }

        return new PublicUser(result);
    }

    /**
     * From a pseudo, returns the associated java entity.
     * @param pseudo The pseudo of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public PublicUser loadSQLite(String pseudo) {
        try {
            String[] selectArgs = {pseudo};
            String query = String.format(this.SIMPLE_QUERY_ALL, this.table, PSEUDO);
            Cursor result = this.database.rawQuery(query, selectArgs);

            return new PublicUser(result);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a string and a field, returns the associated java entities where the string matches in the field values.
     * @param field The field to filter on.
     * @param filter The string contained in the pseudo of the entity to load from the database.
     * @return The loaded entities if exists else null.
     */
    public List<PublicUser> loadFilteredSQLite(String field, String filter) {
        try {
            List<PublicUser> users = new ArrayList<>();
            String[] selectArgs = {filter};
            String query = String.format(this.SIMPLE_QUERY_ALL_LIKE_START, this.table, field);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                users.add(new PublicUser(result, false));
            }

            return users;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Query all the users from the database.
     * @return The list of users.
     */
    public List<PublicUser> queryAllSQLite() {
        List<PublicUser> users = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    users.add(new PublicUser(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }

        return users;
    }

    /**
     * From the API, query the list of all public users from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(this.baseUrl + APIManager.READ + "?public=true");
    }

    /**
     * Creates a user entity in MySQL database.
     * @param user The user to create.
     */
    public void createMySQL(final PrivateUser user) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        if (user.getId() != 0) {
            param.put(ID, String.valueOf(user.getId()));
        }

        param.put(PSEUDO, user.getPseudo());
        param.put(PASSWORD, user.getPassword());
        param.put(EMAIL, user.getEmail());
        param.put(PROFILE, String.valueOf(user.getProfile().getId()));
        param.put(CITY, String.valueOf(user.getCity().getId()));
        param.put(COUNTRY, String.valueOf(user.getCountry().getId()));

        super.requestString(Request.Method.POST, url, response -> {
            Pattern pattern = Pattern.compile("^\\d.$");

            if (pattern.matcher(response).find()) {
                user.setId(Integer.valueOf(response));
            }
            
            HTTPRequestQueueSingleton.getInstance(this.getContext()).finishRequest(this.getClass().getName());
        }, param);

        this.waitForResponse();
    }

    /**
     * Updates a user entity in MySQL database.
     * @param user The user to update.
     */
    public void updateMySQL(PrivateUser user) {
        String url = this.baseUrl + APIManager.UPDATE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(user.getId()));
        param.put(PSEUDO, user.getPseudo());
        param.put(PASSWORD, user.getPassword());
        param.put(EMAIL, user.getEmail());
        param.put(PROFILE, String.valueOf(user.getProfile().getId()));
        param.put(CITY, String.valueOf(user.getCity().getId()));
        param.put(COUNTRY, String.valueOf(user.getCountry().getId()));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Updates a user fields given in parameter in MySQL database.
     * @param id The id of user to update.
     * @param field The field of the user to update.
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
     * Loads a user from MySQL database.
     * @param idUser The id of the user.
     * @return The loaded user.
     */
    public PrivateUser loadMySQL(int idUser) {
        final PrivateUser user = new PrivateUser();
        final int idProfile[] = new int[1];
        final int idCity[] = new int[1];
        final int idCountry[] = new int[1];

        String url = this.baseUrl + APIManager.READ + ID + "=" + idUser;

        super.requestJsonArray(Request.Method.GET, url,  response -> {
            try {
                JSONObject object = response.getJSONObject(0);
                idProfile[0] = object.getInt(PROFILE);
                idCity[0] = object.getInt(CITY);
                idCountry[0] = object.getInt(COUNTRY);

                user.init(object);
            } catch (JSONException e) {
                Log.e(JSON_TAG, e.getMessage());
            } finally {
                HTTPRequestQueueSingleton.getInstance(this.getContext()).finishRequest(this.getClass().getName());
            }
        });

        this.waitForResponse();
        user.setProfile(new ProfileDBManager(this.getContext()).loadMySQL(idProfile[0]));
        user.setCountry(new CountryDBManager(this.getContext()).loadMySQL(idCountry[0]));
        user.setCity(new CityDBManager(this.getContext()).loadMySQL(idCity[0]));
        user.setReviews(new ReviewDBManager(this.getContext()).loadUserMySQL(user.getId()));
        user.setBookLists(new BookListDBManager(this.getContext()).loadUserMySQL(user.getId()));

        return (user.isEmpty()) ? null : user;
    }

    /**
     * Loads a user from MySQL database.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return The loaded user.
     */
    public PrivateUser loadMySQL(String email, String password) {
        final PrivateUser user = new PrivateUser();
        String url = this.baseUrl + APIManager.READ + EMAIL + "=" + email + "&" + PASSWORD + "=" + password;
        final int idProfile[] = new int[1];
        final int idCity[] = new int[1];
        final int idCountry[] = new int[1];

        super.requestJsonArray(Request.Method.GET, url, response -> {
            try {
                JSONObject object = response.getJSONObject(0);
                idProfile[0] = object.getInt(PROFILE);
                idCity[0] = object.getInt(CITY);
                idCountry[0] = object.getInt(COUNTRY);

                user.init(object);
            } catch (JSONException e) {
                Log.e(JSON_TAG, e.getMessage());
            } finally {
                HTTPRequestQueueSingleton.getInstance(this.getContext()).finishRequest(this.getClass().getName());
            }
        });

        this.waitForResponse();
        user.setProfile(new ProfileDBManager(this.getContext()).loadMySQL(idProfile[0]));
        user.setCountry(new CountryDBManager(this.getContext()).loadMySQL(idCountry[0]));
        user.setCity(new CityDBManager(this.getContext()).loadMySQL(idCity[0]));
        user.setReviews(new ReviewDBManager(this.getContext()).loadUserMySQL(user.getId()));
        user.setBookLists(new BookListDBManager(this.getContext()).loadUserMySQL(user.getId()));

        return (user.isEmpty()) ? null : user;
    }

    /**
     * Checks if a value is available for a specific field (not already taken).
     * @param field The associated field.
     * @param value The value to check.
     * @return True if the value is available (not taken) else false.
     */
    public boolean isAvailableMySQL(String field, String value) {
        final boolean[] available = new boolean[1];
        String url = this.baseUrl + APIManager.READ + field + "=" + value + "&public=1";
        available[0] = true;
        
        super.requestJsonArray(Request.Method.GET, url, response -> {
            available[0] = false;

            HTTPRequestQueueSingleton.getInstance(this.getContext()).finishRequest(this.getClass().getName());
        });
        
        this.waitForResponse();
        
        return available[0];
    }

    /**
     * Deletes a user entity in MySQL database.
     * @param email The email of the user to delete.
     * @param password The password of the user to delete.
     */
    public void deleteMySQL(String email, String password) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(PASSWORD, password);
        param.put(EMAIL, email);

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Restores a user entity in MySQL database.
     * @param email The email of the user to restore.
     * @param password The password of the user to restore.
     */
    public void restoreMySQL(String email, String password) {
        String url = this.baseUrl + APIManager.RESTORE;
        Map<String, String> param = new HashMap<>();

        param.put(PASSWORD, password);
        param.put(EMAIL, email);

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Restores a user from MySQL database.
     * @param idUser The id of the user to restore.
     */
    public void restoreMySQL(int idUser) {
        String url = this.baseUrl + APIManager.RESTORE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(idUser));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Deletes a user entity in MySQL database.
     * @param user The user to delete.
     */
    public void deleteMySQL(PrivateUser user) {
        this.deleteMySQL(user.getEmail(), user.getPassword());
    }

    @Override
    public void createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(PSEUDO, entity.getString(PSEUDO));
            data.put(PROFILE, entity.getInt(PROFILE));
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

            data.put(PSEUDO, entity.getString(PSEUDO));

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
