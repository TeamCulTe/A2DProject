package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the category database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class CategoryDBSchema {
    /**
     * Defines the category's table name.
     */
    public static final String TABLE = "Category";

    /**
     * Defines the category's id field.
     */
    public static final String ID = "id_category";

    /**
     * Defines the category's name field.
     */
    public static final String NAME = "name_category";

    /**
     * Defines the book list type name field max size.
     */
    private static final int NAME_SIZE = 50;

    /**
     * Defines the category create table statement.
     */
    public static final String CATEGORY_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                                                                        "PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL, %s DATETIME NOT NULL DEFAULT %s);",
                                                                        TABLE,
                                                                        ID,
                                                                        NAME,
                                                                        NAME_SIZE,
                                                                        CommonDBSchema.UPDATE,
                                                                        CommonDBSchema.UPDATE_DEFAULT);
}
