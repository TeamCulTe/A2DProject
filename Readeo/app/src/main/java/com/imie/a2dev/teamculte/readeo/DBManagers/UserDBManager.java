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
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.CITY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.COUNTRY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.EMAIL;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PASSWORD;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PROFILE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PSEUDO;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.TABLE;

/**
 * Manager class used to manage the user entities from databases.
 */
public final class UserDBManager extends DBManager {
    /**
     * Stores the base of the users API url.
     */
    private final String baseUrl = APIManager.API_URL + APIManager.USERS;

    /**
     * UserDBManager's constructor.
     * @param context The associated context.
     */
    public UserDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
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
            DBManager.database.insertOrThrow(this.table, null, data);

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

            return DBManager.database.update(this.table, data, whereClause, whereArgs) != 0;
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
        try {
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, this.table, ID);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

            return new PublicUser(result);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a pseudo, returns the associated java entity.
     * @param pseudo The pseudo of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public PublicUser loadSQLite(String pseudo) {
        try {
            String[] selectArgs = {pseudo};
            String query = String.format(SIMPLE_QUERY_ALL, this.table, PSEUDO);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

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
            String query = String.format(SIMPLE_QUERY_ALL_LIKE_START, this.table, field);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                users.add(new PublicUser(result));
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
            Cursor result = DBManager.database.rawQuery(String.format(QUERY_ALL, this.table), null);

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
    public void createMySQL(PrivateUser user) {
        String url = String.format(baseUrl + APIManager.CREATE + PSEUDO + "=%s&" + PASSWORD + "=%s&" + EMAIL +
                        "=%s&" + PROFILE + "=%s&" + CITY + "=%s&" + COUNTRY + "=%s",
                user.getPseudo(),
                user.getPassword(),
                user.getEmail(),
                user.getProfile().getId(),
                user.getCity().getId(),
                user.getCountry().getId());

        super.requestString(Request.Method.POST, url, null);
    }

    /**
     * Updates a user entity in MySQL database.
     * @param user The user to update.
     */
    public void updateMySQL(PrivateUser user) {
        String url = String.format(baseUrl + APIManager.UPDATE + ID + "=%s&" + PSEUDO + "=%s&" + PASSWORD + "=%s&" +
                        EMAIL + "=%s&" + PROFILE + "=%s&" + CITY + "=%s&" + COUNTRY + "=%s",
                user.getId(),
                user.getPseudo(),
                user.getPassword(),
                user.getEmail(),
                user.getProfile().getId(),
                user.getCity().getId(),
                user.getCountry().getId());

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Updates a user fields given in parameter in MySQL database.
     * @param id The id of user to update.
     * @param field The field of the user to update.
     * @param value The the new value to set.
     */
    public void updateFieldMySQL(int id, String field, String value) {
        String url = String.format(baseUrl + APIManager.UPDATE + ID + "=%s&" + field + "=%s",
                id,
                value);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Loads a user from MySQL database.
     * @param idUser The id of the user.
     * @return The loaded review.
     */
    public PrivateUser loadMySQL(int idUser) {
        final PrivateUser user = new PrivateUser();

        String url = String.format(baseUrl + APIManager.READ + ID + "=%s", idUser);

        super.requestJsonObject(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                user.init(response);
            }
        });

        return user;
    }

    /**
     * Loads a user from MySQL database.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return The loaded review.
     */
    public PrivateUser loadMySQL(String email, String password) {
        final PrivateUser user = new PrivateUser();

        String url = String.format(baseUrl + APIManager.READ + EMAIL + "=%s&" + PASSWORD + "=%s", email, password);

        super.requestJsonObject(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                user.init(response);
            }
        });

        return user;
    }

    /**
     * Deletes a user entity in MySQL database.
     * @param email The email of the user to delete.
     * @param password The password of the user to delete.
     */
    public void deleteMySQL(String email, String password) {
        String url = String.format(baseUrl + APIManager.DELETE + EMAIL + "=%s&" + PASSWORD + "=%s", email, password);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Restores a user entity in MySQL database.
     * @param email The email of the user to restore.
     * @param password The password of the user to derestorelete.
     */
    public void restoreMySQL(String email, String password) {
        String url = String.format(baseUrl + APIManager.RESTORE + EMAIL + "=%s&" + PASSWORD + "=%s", email, password);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Restores a user from MySQL database.
     * @param idUser The id of the user to restore.
     */
    public void restoreMySQL(int idUser) {
        String url = String.format(baseUrl + APIManager.RESTORE + ID + "=%s", idUser);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Deletes a user entity in MySQL database.
     * @param user The user to delete.
     */
    public void deleteMySQL(PrivateUser user) {
        this.deleteMySQL(user.getEmail(), user.getPassword());
    }

    @Override
    protected void createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(PSEUDO, entity.getString(PSEUDO));
            data.put(PROFILE, entity.getInt(PROFILE));
            DBManager.database.insertOrThrow(this.table, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
