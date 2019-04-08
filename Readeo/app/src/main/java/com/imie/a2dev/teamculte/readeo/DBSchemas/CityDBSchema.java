package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the city database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class CityDBSchema {
    /**
     * Defines the city's table name.
     */
    public static final String TABLE =  "City";

    /**
     * Defines the city's id field.
     */
    public static final String ID =  "id_city";

    /**
     * Defines the city's name field.
     */
    public static final String NAME =  "name_city";

    /**
     * Defines the book list type name field max size.
     */
    private static final int NAME_SIZE = 50;

    /**
     * Defines the city create table statement.
     */
    public static final String CITY_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL, %s DATETIME NOT NULL DEFAULT %s);",
            TABLE,
            ID,
            NAME,
            NAME_SIZE,
            CommonDBSchema.UPDATE,
            CommonDBSchema.UPDATE_DEFAULT);
}
