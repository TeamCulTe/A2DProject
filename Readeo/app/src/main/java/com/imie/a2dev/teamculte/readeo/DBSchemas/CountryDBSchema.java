package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the country database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class CountryDBSchema {
    /**
     * Defines the country's table name.
     */
    public static final String TABLE = "Country";

    /**
     * Defines the country's id field.
     */
    public static final String ID = "id_country";

    /**
     * Defines the country's name field.
     */
    public static final String NAME = "name_country";

    /**
     * Defines the book list type name field max size.
     */
    private static final int NAME_SIZE = 50;

    /**
     * Defines the country create table statement.
     */
    public static final String COUNTRY_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                                                                       "PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL, %s DATETIME NOT NULL DEFAULT %s);",
                                                                       TABLE,
                                                                       ID,
                                                                       NAME,
                                                                       NAME_SIZE,
                                                                       CommonDBSchema.UPDATE,
                                                                       CommonDBSchema.UPDATE_DEFAULT);
}
