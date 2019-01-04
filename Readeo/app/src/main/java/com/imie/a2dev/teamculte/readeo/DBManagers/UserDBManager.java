package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Response;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class used to manage the user entities from databases.
 */
public final class UserDBManager extends DBManager {
    /**
     * Defines the user's table name.
     */
    public static final String TABLE = "User";

    /**
     * Defines the user's id field.
     */
    public static final String ID = "id_user";

    /**
     * Defines the user's pseudo field.
     */
    public static final String PSEUDO = "pseudo";

    /**
     * Defines the user's password field.
     */
    public static final String PASSWORD = "password";

    /**
     * Defines the user's email field.
     */
    public static final String EMAIL = "email";

    /**
     * Defines the user's profile id field.
     */
    public static final String PROFILE = ProfileDBManager.ID;

    /**
     * Defines the user's city id field.
     */
    public static final String CITY = "id_city";

    /**
     * Defines the user's country id field.
     */
    public static final String COUNTRY = "id_country";

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
     * Queries the value of a specific field from a specific pseudo.
     * @param field The field to access.
     * @param id The pseudo of the db entity to access.
     * @return The value of the field.
     */
    public String getFieldSQLite(String field, String id) {
        return this.getFieldSQLite(field, TABLE, PSEUDO, id);
    }

    /**
     * From a user pseudo, gets its associated id.
     * @param pseudo The pseudo of the user.
     * @return The id associated to the pseudo.
     */
    public int SQLiteGetId(String pseudo) {
        return Integer.valueOf(this.getFieldSQLite(ID, pseudo));
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
    public PublicUser loadSQLite(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, ID);
            Cursor result = this.database.rawQuery(query, selectArgs);

            return new PublicUser(result);
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
     * Query all the users from the database.
     * @return The list of users.
     */
    public List<PublicUser> queryAllSQLite() {
        List<PublicUser> users = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(QUERY_ALL, TABLE), null);

            while (result.moveToNext()) {
                users.add(new PublicUser(result));
            }

        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }

        return users;
    }

    /**
     * From the API, query the list of all public users from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importAllFromMySQL() {
        super.importAllFromMySQL(this.baseUrl + APIManager.READ + "?public=true");
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

        super.requestString(url, null);
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

        super.requestString(url, null);
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

        super.requestString(url, null);
    }

    /**
     * Loads a user from MySQL database.
     * @param idUser The id of the user.
     * @return The loaded review.
     */
    public PrivateUser loadMySQL(int idUser) {
        final PrivateUser user = new PrivateUser();

        String url = String.format(baseUrl + APIManager.READ + ID + "=%s", idUser);

        super.requestJsonObject(url, new Response.Listener<JSONObject>() {
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

        super.requestJsonObject(url, new Response.Listener<JSONObject>() {
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

        super.requestString(url, null);
    }

    /**
     * Restores a user entity in MySQL database.
     * @param email The email of the user to restore.
     * @param password The password of the user to derestorelete.
     */
    public void restoreMySQL(String email, String password) {
        String url = String.format(baseUrl + APIManager.RESTORE + EMAIL + "=%s&" + PASSWORD + "=%s", email, password);

        super.requestString(url, null);
    }

    /**
     * Restores a user from MySQL database.
     * @param idUser The id of the user to restore.
     */
    public void restoreMySQL(int idUser) {
        String url = String.format(baseUrl + APIManager.RESTORE + ID + "=%s", idUser);

        super.requestString(url, null);
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
            this.database.insertOrThrow(TABLE, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
