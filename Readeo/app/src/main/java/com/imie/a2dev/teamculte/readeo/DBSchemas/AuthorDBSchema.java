package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the author database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class AuthorDBSchema {
    /**
     * Defines the author's table name.
     */
    public static final String TABLE = "Author";

    /**
     * Defines the author's id field.
     */
    public static final String ID = "id_author";

    /**
     * Defines the author's name field.
     */
    public static final String NAME = "name_author";

    /**
     * Defines the author's name field max size.
     */
    private static final int NAME_SIZE = 50;

    /**
     * Defines the author create table statement.
     */
    public static final String AUTHOR_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL, %s DATETIME NOT NULL DEFAULT %s);",
            AuthorDBSchema.TABLE,
            AuthorDBSchema.ID,
            AuthorDBSchema.NAME,
            NAME_SIZE,
            CommonDBSchema.UPDATE,
            CommonDBSchema.UPDATE_DEFAULT);
}
