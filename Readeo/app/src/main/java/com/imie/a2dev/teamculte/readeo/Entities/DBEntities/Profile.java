package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ProfileDBManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Final class representing the profile of an user with its related information.
 */
public final class Profile extends DBEntity {
    /**
     * Stores the path of the user's avatar displayed on his profile.
     */
    private String avatar;

    /**
     * Stores the description written by the user for his profile.
     */
    private String description;

    /**
     * Profile's default constructor.
     */
    public Profile() {
        super();
    }

    /**
     * Profile's nearly full filled constructor, providing all attributes values, except for database related ones
     * @param avatar The path of the avatar to set.
     * @param description The description to set.
     */
    public Profile(String avatar, String description) {
        super();

        this.avatar = avatar;
        this.description = description;
    }

    /**
     * Profile's full filled constructor, providing all attributes values.
     * @param id The id to set.
     * @param avatar The path of the avatar to set.
     * @param description The description to set.
     */
    public Profile(int id, String avatar, String description) {
        super(id);

        this.avatar = avatar;
        this.description = description;
    }

    /**
     * Profile's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     */
    public Profile(Cursor result) {
        this.init(result, true);
    }

    /**
     * Profile's full filled constructor providing all its attributes values from the result of a database query,
     * closes the cursor if specified.
     * @param result The result of the query.
     * @param close Defines if the cursor should be closed or not.
     */
    public Profile(Cursor result, boolean close) {
        this.init(result, close);
    }

    /**
     * Gets the avatar attribute.
     * @return The String value of avatar attribute.
     */
    public String getAvatar() {
        return this.avatar;
    }

    /**
     * Gets the description attribute.
     * @return The String value of description attribute.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the avatar attribute.
     * @param newAvatar The new String value to set.
     */
    public void setAvatar(String newAvatar) {
        this.avatar = newAvatar;
    }

    /**
     * Sets the description attribute.
     * @param newDescription The new String value to set.
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * Initializes the profile from a JSON response object.
     * @param object The JSON response from the API.
     */
    public void init(JSONObject object) {
        try {
            this.setId(object.getInt(ProfileDBManager.ID));
            this.setAvatar(object.getString(ProfileDBManager.AVATAR));
            this.setDescription(object.getString(ProfileDBManager.DESCRIPTION));
        } catch (JSONException e) {
            Log.e(DBManager.JSON_TAG, e.getMessage());
        }
    }

    @Override
    protected void init(Cursor result, boolean close) {
        try {
            if (result.isFirst()) {
                result.moveToNext();
            }

            this.id = result.getInt(result.getColumnIndexOrThrow(ProfileDBManager.ID));
            this.avatar = result.getString(result.getColumnIndexOrThrow(ProfileDBManager.AVATAR));
            this.description = result.getString(result.getColumnIndexOrThrow(ProfileDBManager.DESCRIPTION));

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(DBManager.SQLITE_TAG, e.getMessage());
        }
    }
}
