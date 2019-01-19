package com.imie.a2dev.teamculte.readeo.DBSchemas;

import com.imie.a2dev.teamculte.readeo.DBManagers.CategoryDBManager;

/**
 * Class used to define the book database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class BookDBSchema {
    /**
     * Defines the book's table name.
     */
    public static final String TABLE = "Book";

    /**
     * Defines the book's id field.
     */
    public static final String ID = "id_book";

    /**
     * Defines the book's category id field.
     */
    public static final String CATEGORY = CategoryDBSchema.ID;

    /**
     * Defines the book's title field.
     */
    public static final String TITLE = "title";

    /**
     * Defines the book's cover field.
     */
    public static final String COVER = "cover";

    /**
     * Defines the book's summary field.
     */
    public static final String SUMMARY = "summary";

    /**
     * Defines the book's date published field.
     */
    public static final String DATE = "date_published";

    /**
     * Defines the books's title and cover field max size.
     */
    private static final int TEXT_SIZE = 255;

    /**
     * Defines the book create table statement.
     */
    public static final String BOOK_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s INTEGER NOT NULL, %s TEXT(%s) UNIQUE NOT NULL, %s TEXT(%s), %s TEXT, %s " +
                    "INTEGER(4) NOT NULL, %s DATETIME NOT NULL DEFAULT %s, CONSTRAINT Book_Category_FK FOREIGN KEY " +
                    "(%s) REFERENCES Category(%s));",
            BookDBSchema.TABLE,
            BookDBSchema.ID,
            BookDBSchema.CATEGORY,
            BookDBSchema.TITLE,
            TEXT_SIZE,
            BookDBSchema.COVER,
            TEXT_SIZE,
            BookDBSchema.SUMMARY,
            BookDBSchema.DATE,
            CommonDBSchema.UPDATE,
            CommonDBSchema.UPDATE_DEFAULT,
            BookDBSchema.CATEGORY,
            CategoryDBSchema.ID);
}
