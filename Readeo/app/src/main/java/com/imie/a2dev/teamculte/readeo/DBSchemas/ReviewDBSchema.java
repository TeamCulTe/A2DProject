package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the review database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class ReviewDBSchema {
    /**
     * Defines the review's table name.
     */
    public static final String TABLE = "Review";

    /**
     * Defines the review's user id field.
     */
    public static final String USER = UserDBSchema.ID;

    /**
     * Defines the review's book id field.
     */
    public static final String BOOK = BookDBSchema.ID;

    /**
     * Defines the review's text field.
     */
    public static final String REVIEW = "review";

    /**
     * Defines the review's shared field.
     */
    public static final String SHARED = "shared";

    /**
     * Defines the review create table statement.
     */
    public static final String REVIEW_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER NOT " +
                    "NULL, %s INTEGER NOT NULL, %s TEXT NOT NULL, %s DATETIME NOT NULL DEFAULT %s, CONSTRAINT " +
                    "Review_PK PRIMARY KEY (%s, %s), CONSTRAINT Review_User_FK FOREIGN KEY (%s) REFERENCES User(%s), " +
                    "CONSTRAINT Review_Book_FK FOREIGN KEY (%s) REFERENCES Book(%s));",
            TABLE,
            USER,
            BOOK,
            REVIEW,
            CommonDBSchema.UPDATE,
            CommonDBSchema.UPDATE_DEFAULT,
            USER,
            BOOK,
            USER,
            UserDBSchema.ID,
            BOOK,
            BookDBSchema.ID);
}
