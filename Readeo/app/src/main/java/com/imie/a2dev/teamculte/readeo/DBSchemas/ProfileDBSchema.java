package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the profile database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class ProfileDBSchema {
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
     * Defines the book list type name field max size.
     */
    private static final int AVATAR_SIZE = 50;

    /**
     * Defines the book list type name field max size.
     */
    private static final int DESCRIPTION_SIZE = 50;

    /**
     * Defines the profile create table statement.
     */
    public static final String PROFILE_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                                                                       "PRIMARY KEY, %s TEXT(%s) NOT NULL DEFAULT \"\" , %s TEXT(%s) NOT NULL DEFAULT \"\", " +
                                                                       "%s DATETIME NOT NULL DEFAULT %s);",
                                                                       TABLE,
                                                                       ID,
                                                                       AVATAR,
                                                                       AVATAR_SIZE,
                                                                       DESCRIPTION,
                                                                       DESCRIPTION_SIZE,
                                                                       CommonDBSchema.UPDATE,
                                                                       CommonDBSchema.UPDATE_DEFAULT);
}
