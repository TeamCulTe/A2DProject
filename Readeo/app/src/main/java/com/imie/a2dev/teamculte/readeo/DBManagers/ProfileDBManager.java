package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Profile;
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
    public boolean SQLiteCreate(@NonNull Profile entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(AVATAR, entity.getAvatar());
            data.put(DESCRIPTION, entity.getDescription());
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
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean SQLiteUpdate(@NonNull Profile entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(AVATAR, entity.getAvatar());
            data.put(DESCRIPTION, entity.getDescription());

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
    public Profile SQLiteLoad(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, ID);
            Cursor result = this.database.rawQuery(query, selectArgs);

            return new Profile(result);
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
     * Query all the profiles from the database.
     * @return The list of profiles.
     */
    public List<Profile> queryAll() {
        List<Profile> profiles = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(QUERY_ALL, TABLE), null);

            while (result.moveToNext()) {
                profiles.add(new Profile(result));
            }

        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }

        return profiles;
    }
}
