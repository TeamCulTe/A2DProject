package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the book list type database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class BookListTypeDBSchema {
    /**
     * Defines the book list type's table name.
     */
    public static final String TABLE =  "BookListType";

    /**
     * Defines the book list type's id field.
     */
    public static final String ID =  "id_book_list_type";

    /**
     * Defines the book list type's name field.
     */
    public static final String NAME =  "name_book_list_type";

    /**
     * Defines the book list type name field max size.
     */
    private static final int NAME_SIZE = 50;

    /**
     * Defines the book list type create table statement.
     */
    public static final String BOOK_LIST_TYPE_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s " +
                    "INTEGER PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL, %s DATETIME NOT NULL DEFAULT %s);",
            BookListTypeDBSchema.TABLE,
            BookListTypeDBSchema.ID,
            BookListTypeDBSchema.NAME,
            NAME_SIZE,
            CommonDBSchema.UPDATE,
            CommonDBSchema.UPDATE_DEFAULT);
}
