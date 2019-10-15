package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the user database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class UserDBSchema {
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
    public static final String PROFILE = ProfileDBSchema.ID;

    /**
     * Defines the user's city id field.
     */
    public static final String CITY = CityDBSchema.ID;

    /**
     * Defines the user's country id field.
     */
    public static final String COUNTRY = CountryDBSchema.ID;

    /**
     * Defines the user's key field.
     */
    public static final String KEY = "api_key";

    /**
     * Defines the book list type name field max size.
     */
    private static final int PSEUDO_SIZE = 50;

    /**
     * Defines the user create table statement.
     */
    public static final String USER_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL, %s INTEGER NOT NULL, %s DATETIME NOT NULL DEFAULT %s, " +
                    "CONSTRAINT User_Profile_FK FOREIGN KEY (%s) REFERENCES Profile(%s));",
            TABLE,
            ID,
            PSEUDO,
            PSEUDO_SIZE,
            PROFILE,
            CommonDBSchema.UPDATE,
            CommonDBSchema.UPDATE_DEFAULT,
            PROFILE,
            ProfileDBSchema.ID);

    /**
     * Stores the trigger statement on user table when deleting in order to delete the other user occurrences.
     */
    public static final String USER_TRIGGER_STATEMENT = "CREATE TRIGGER IF NOT EXISTS user_trigger BEFORE DELETE ON " +
            "User FOR EACH ROW BEGIN DELETE FROM Quote WHERE Quote.id_user = OLD.id_user; DELETE FROM Review WHERE " +
            "Review.id_user = OLD.id_user; DELETE FROM Profile WHERE Profile.id_profile = OLD.id_profile; END;";
}
