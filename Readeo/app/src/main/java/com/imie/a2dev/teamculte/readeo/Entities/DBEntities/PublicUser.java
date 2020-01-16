package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.ProfileDBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema;

import lombok.Getter;
import lombok.Setter;

/**
 * Final class representing a public user of the application (without personal data, book lists and
 * reviews).
 */
@Getter
@Setter
public class PublicUser extends DBEntity {
    /**
     * Stores the user's pseudo.
     */
    protected String pseudo;

    /**
     * Stores the user's profile.
     */
    protected Profile profile;

    /**
     * User's default constructor.
     */
    public PublicUser() {
        super();
    }

    /**
     * User's nearly full filled constructor, providing all attributes values, except for database related ones.
     * @param pseudo The pseudo to set.
     * @param profile The profile to set.
     */
    public PublicUser(String pseudo, Profile profile) {
        super();

        this.pseudo = pseudo;
        this.profile = profile;
    }

    /**
     * User's full filled constructor, providing all attributes values.
     * @param id The id to set.
     * @param pseudo The pseudo to set.
     * @param profile The profile to set.
     */
    public PublicUser(int id, String pseudo, Profile profile) {
        super(id);

        this.pseudo = pseudo;
        this.profile = profile;
    }

    /**
     * PublicUser's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     */
    public PublicUser(Cursor result) {
        this.init(result, true);
    }

    /**
     * PublicUser's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     * @param close Defines if the cursor should be closed or not.
     */
    public PublicUser(Cursor result, boolean close) {
        this.init(result, close);
    }

    /**
     * PublicUser's full filled constructor providing all its attributes values from a ContentValues object.
     * @param contentValues The ContentValues object used to initialize the entity.
     */
    public PublicUser(ContentValues contentValues) {
        this.init(contentValues);
    }

    /**
     * Initializes the user from a ContentValues object.
     * @param contentValues The ContentValues object.
     */
    public void init(@NonNull ContentValues contentValues) {
        this.id = contentValues.getAsInteger(UserDBSchema.ID);
        this.pseudo = contentValues.getAsString(UserDBSchema.PSEUDO);
        this.profile =
                new ProfileDBManager(App.getAppContext()).loadSQLite(contentValues.getAsInteger(UserDBSchema.PROFILE));
    }

    @Override
    protected void init(@NonNull Cursor result, boolean close) {
        try {
            if (result.getPosition() == -1) {
                result.moveToNext();
            }

            this.id = result.getInt(result.getColumnIndexOrThrow(UserDBSchema.ID));
            this.pseudo = result.getString(result.getColumnIndexOrThrow(UserDBSchema.PSEUDO));
            this.profile = new ProfileDBManager(App.getAppContext()).loadSQLite(
                    result.getInt(result.getColumnIndexOrThrow(UserDBSchema.PROFILE)));
        } catch (SQLiteException e) {
            this.logError("init", e);
        } finally {
            if (close) {
                result.close();
            }
        }
    }
}
