package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the writer database schema.
 * Useful to separate logic code from managers and structures.
 */
public abstract class WriterDBSchema {
    /**
     * Defines the writer's table name.
     */
    public static final String TABLE = "Writer";

    /**
     * Defines the user's pseudo field.
     */
    public static final String AUTHOR = AuthorDBSchema.ID;

    /**
     * Defines the user's pseudo field.
     */
    public static final String BOOK = BookDBSchema.ID;

    /**
     * Defines the writer create table statement.
     */
    public static final String WRITER_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "NOT NULL, %s INTEGER NOT NULL, CONSTRAINT Writer_PK PRIMARY KEY (%s, %s), CONSTRAINT " +
                    "Writer_Author_FK FOREIGN KEY (%s) REFERENCES Author(%s), CONSTRAINT Writer_Book_FK FOREIGN " +
                    "KEY (%s) REFERENCES Book(%s));",
            WriterDBSchema.TABLE,
            WriterDBSchema.AUTHOR,
            WriterDBSchema.BOOK,
            WriterDBSchema.AUTHOR,
            WriterDBSchema.BOOK,
            WriterDBSchema.AUTHOR,
            AuthorDBSchema.ID,
            WriterDBSchema.BOOK,
            BookDBSchema.ID);
}
