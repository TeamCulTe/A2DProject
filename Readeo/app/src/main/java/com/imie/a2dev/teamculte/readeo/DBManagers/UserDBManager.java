package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
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
    public boolean SQLiteCreate(@NonNull PublicUser entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(PSEUDO, entity.getPseudo());
            data.put(PROFILE, entity.getProfile().getId());
            this.database.insertOrThrow(TABLE, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param id The id of the db entity to access.
     * @return The value of the field.
     */
    public String SQLiteGetField(String field, int id) {
        return this.SQLiteGetField(field, TABLE, ID, id);
    }

    /**
     * Queries the value of a specific field from a specific pseudo.
     * @param field The field to access.
     * @param id The pseudo of the db entity to access.
     * @return The value of the field.
     */
    public String SQLiteGetField(String field, String id) {
        return this.SQLiteGetField(field, TABLE, PSEUDO, id);
    }

    /**
     * From a user pseudo, gets its associated id.
     * @param pseudo The pseudo of the user.
     * @return The id associated to the pseudo.
     */
    public int SQLiteGetId(String pseudo) {
        return Integer.valueOf(this.SQLiteGetField(ID, pseudo));
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean SQLiteUpdate(@NonNull PublicUser entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(ID, entity.getId());
            data.put(PSEUDO, entity.getPseudo());
            data.put(PROFILE, entity.getProfile().getId());

            return this.database.update(TABLE, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From an id, returns the associated java entity.
     * @param id The id of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public PublicUser SQLiteLoad(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, ID);
            Cursor result = this.database.rawQuery(query, selectArgs);

            return new PublicUser(result);
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From an id given in parameter, deletes the associated entity in the database.
     * @param id The id of the entity to delete.
     * @return true if success else false.
     */
    public boolean SQLiteDelete(int id) {
        try {
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Query all the users from the database.
     * @return The list of users.
     */
    public List<PublicUser> queryAll() {
        List<PublicUser> users = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(QUERY_ALL, TABLE), null);

            while (result.moveToNext()) {
                users.add(new PublicUser(result));
            }

        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }

        return users;
    }
}
