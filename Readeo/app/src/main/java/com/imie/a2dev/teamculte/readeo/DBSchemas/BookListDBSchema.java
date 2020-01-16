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

    /**
     * Defines the book list create table statement.
     */
    public static final String BOOK_LIST_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                                                                         "NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s DATETIME NOT NULL DEFAULT %s, CONSTRAINT " +
                                                                         "BookList_PK PRIMARY KEY (%s, %s, %s), CONSTRAINT BookList_User_FK FOREIGN KEY (%s) REFERENCES User" +
                                                                         "(%s), CONSTRAINT BookList_Book_FK FOREIGN KEY (%s) REFERENCES Book(%s), CONSTRAINT " +
                                                                         "BookList_BookListType_FK FOREIGN KEY (%s) REFERENCES BookListType(%s));",
                                                                         TABLE,
                                                                         USER,
                                                                         BOOK,
                                                                         TYPE,
                                                                         CommonDBSchema.UPDATE,
                                                                         CommonDBSchema.UPDATE_DEFAULT,
                                                                         USER,
                                                                         BOOK,
                                                                         TYPE,
                                                                         USER,
                                                                         UserDBSchema.ID,
                                                                         BOOK,
                                                                         BookDBSchema.ID,
                                                                         TYPE,
                                                                         BookListTypeDBSchema.ID);
}
