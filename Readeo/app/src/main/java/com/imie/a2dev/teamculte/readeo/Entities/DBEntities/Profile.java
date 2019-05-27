package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.JSON_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.SQLITE_TAG;

/**
 * Final class representing the profile of an user with its related information.
 */
@Getter
@Setter
public final class Profile extends DBEntity {
    /**
     * Stores the path of the user's avatar displayed on his profile.
     */
    private String avatar = "";

    /**
     * Stores the description written by the user for his profile.
     */
    private String description = "";

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
     * Profile's full filled constructor providing all its attributes values from a json object.
     * @param result The json object.
     */
    public Profile(JSONObject result) {
        this.init(result);
    }

    /**
     * Profile's full filled constructor providing all its attributes values from a ContentValues object.
     * @param contentValues The ContentValues object used to initialize the entity.
     */
    public Profile(ContentValues contentValues) {
        this.init(contentValues);
    }

    /**
     * Initializes the profile from a ContentValues object.
     * @param contentValues The ContentValues object.
     */
    public void init(@NonNull ContentValues contentValues) {
        this.id = contentValues.getAsInteger(ProfileDBSchema.ID);
        this.avatar = contentValues.getAsString(ProfileDBSchema.AVATAR);
        this.description = contentValues.getAsString(ProfileDBSchema.DESCRIPTION);
    }

    /**
     * Initializes the profile from a JSON response object.
     * @param object The JSON response from the API.
     */
    public void init(@NonNull JSONObject object) {
        try {
            this.id = object.getInt(ProfileDBSchema.ID);
            this.avatar = object.getString(ProfileDBSchema.AVATAR);
            this.description = object.getString(ProfileDBSchema.DESCRIPTION);
        } catch (JSONException e) {
            Log.e(JSON_TAG, e.getMessage());
        }
    }

    @Override
    protected void init(@NonNull Cursor result, boolean close) {
        try {
            if (result.getPosition() == -1) {
                result.moveToNext();
            }

            this.id = result.getInt(result.getColumnIndexOrThrow(ProfileDBSchema.ID));
            this.avatar = result.getString(result.getColumnIndexOrThrow(ProfileDBSchema.AVATAR));
            this.description = result.getString(result.getColumnIndexOrThrow(ProfileDBSchema.DESCRIPTION));

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
