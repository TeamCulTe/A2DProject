package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the quote database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class QuoteDBSchema {
    /**
     * Defines the quote's table name.
     */
    public static final String TABLE = "Quote";

    /**
     * Defines the quote's id field.
     */
    public static final String ID = "id_quote";

    /**
     * Defines the quote's user id field.
     */
    public static final String USER = UserDBSchema.ID;

    /**
     * Defines the quote's book id field.
     */
    public static final String BOOK = BookDBSchema.ID;

    /**
     * Defines the quote's quote text field.
     */
    public static final String QUOTE = "quote";

    /**
     * Defines the quote create table statement.
     */
    public static final String QUOTE_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s TEXT NOT NULL, %s DATETIME NOT NULL " +
                    "DEFAULT %s, CONSTRAINT Quote_User_FK FOREIGN KEY (%s) REFERENCES User(%s), " +
                    "CONSTRAINT Quote_Book_FK FOREIGN KEY (%s) REFERENCES Book(%s));",
            TABLE,
            ID,
            USER,
            BOOK,
            QUOTE,
            CommonDBSchema.UPDATE,
            CommonDBSchema.UPDATE_DEFAULT,
            USER,
            UserDBSchema.ID,
            BOOK,
            BookDBSchema.ID);
}
