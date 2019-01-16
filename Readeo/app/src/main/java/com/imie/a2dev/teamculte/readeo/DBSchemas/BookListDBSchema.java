package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the book list database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class BookListDBSchema {
    /**
     * Defines the book list's table name.
     */
    public static final String TABLE = "BookList";

    /**
     * Defines the book list's id field.
     */
    public static final String USER = UserDBSchema.ID;

    /**
     * Defines the book list's book id field.
     */
    public static final String BOOK = BookDBSchema.ID;

    /**
     * Defines the book list's type id field.
     */
    public static final String TYPE = BookListTypeDBSchema.ID;

}
