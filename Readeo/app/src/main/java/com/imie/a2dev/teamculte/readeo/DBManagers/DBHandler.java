package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class used to manage database structure (tables, upgrades...).
 */
public final class DBHandler extends SQLiteOpenHelper {
    /**
     * Defines the default database labels size (such as pseudo, names, avatar...).
     */
    private static final String LABEL_SIZE = "50";

    /**
     * Defines the default database texts size (such as reviews and quotes).
     */
    private static final String TEXT_SIZE = "255";

    /**
     * Defines the author create table statement.
     */
    private static final String AUTHOR_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL);",
            AuthorDBManager.TABLE,
            AuthorDBManager.ID,
            AuthorDBManager.NAME,
            LABEL_SIZE);

    /**
     * Defines the book create table statement.
     */
    private static final String BOOK_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s INTEGER NOT NULL, %s TEXT(%s) UNIQUE NOT NULL, %s TEXT(%s), %s TEXT, %s " +
                    "INTEGER(4) NOT NULL, CONSTRAINT Book_Category_FK FOREIGN KEY (%s) REFERENCES Category(%s));",
            BookDBManager.TABLE,
            BookDBManager.ID,
            BookDBManager.CATEGORY,
            BookDBManager.TITLE,
            LABEL_SIZE,
            BookDBManager.COVER,
            TEXT_SIZE,
            BookDBManager.SUMMARY,
            BookDBManager.DATE,
            BookDBManager.CATEGORY,
            CategoryDBManager.ID);

    /**
     * Defines the book list type create table statement.
     */
    private static final String BOOK_LIST_TYPE_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s " +
                    "INTEGER " +
                    "PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL);",
            BookListTypeDBManager.TABLE,
            BookListTypeDBManager.ID,
            BookListTypeDBManager.NAME,
            LABEL_SIZE);

    /**
     * Defines the category create table statement.
     */
    private static final String CATEGORY_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL);",
            CategoryDBManager.TABLE,
            CategoryDBManager.ID,
            CategoryDBManager.NAME,
            LABEL_SIZE);

    /**
     * Defines the city create table statement.
     */
    private static final String CITY_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL);",
            CityDBManager.TABLE,
            CityDBManager.ID,
            CityDBManager.NAME,
            LABEL_SIZE);

    /**
     * Defines the country create table statement.
     */
    private static final String COUNTRY_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL);",
            CountryDBManager.TABLE,
            CountryDBManager.ID,
            CountryDBManager.NAME,
            LABEL_SIZE);

    /**
     * Defines the profile create table statement.
     */
    private static final String PROFILE_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s TEXT(%s) NOT NULL, %s TEXT(%s) NOT NULL);",
            ProfileDBManager.TABLE,
            ProfileDBManager.ID,
            ProfileDBManager.AVATAR,
            LABEL_SIZE,
            ProfileDBManager.DESCRIPTION,
            TEXT_SIZE);

    /**
     * Defines the quote create table statement.
     */
    private static final String QUOTE_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s TEXT NOT NULL, CONSTRAINT " +
                    "Quote_User_FK FOREIGN KEY (%s) REFERENCES User(%s), CONSTRAINT Quote_Book_FK FOREIGN KEY (%s) " +
                    "REFERENCES Book(%s));",
            QuoteDBManager.TABLE,
            QuoteDBManager.ID,
            QuoteDBManager.USER,
            QuoteDBManager.BOOK,
            QuoteDBManager.QUOTE,
            QuoteDBManager.USER,
            UserDBManager.ID,
            QuoteDBManager.BOOK,
            BookDBManager.ID);

    /**
     * Defines the review create table statement.
     */
    private static final String REVIEW_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER NOT " +
                    "NULL, %s INTEGER NOT NULL, %s TEXT(%s) NOT NULL, CONSTRAINT Review_PK PRIMARY KEY (%s, %s), " +
                    "CONSTRAINT Review_User_FK FOREIGN KEY (%s) REFERENCES User(%s), CONSTRAINT Review_Book_FK " +
                    "FOREIGN KEY (%s) REFERENCES Book(%s));",
            ReviewDBManager.TABLE,
            ReviewDBManager.USER,
            ReviewDBManager.BOOK,
            ReviewDBManager.REVIEW,
            TEXT_SIZE,
            ReviewDBManager.USER,
            ReviewDBManager.BOOK,
            ReviewDBManager.USER,
            UserDBManager.ID,
            ReviewDBManager.BOOK,
            BookDBManager.ID);

    /**
     * Defines the user create table statement.
     */
    private static final String USER_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "PRIMARY KEY, %s TEXT(%s) UNIQUE NOT NULL, %s INTEGER NOT NULL, deleted INT(1) NOT NULL DEFAULT " +
                    "0, CONSTRAINT User_Profile_FK FOREIGN KEY (%s) REFERENCES Profile(%s));",
            UserDBManager.TABLE,
            UserDBManager.ID,
            UserDBManager.PSEUDO,
            LABEL_SIZE,
            UserDBManager.PROFILE,
            UserDBManager.PROFILE,
            ProfileDBManager.ID);

    /**
     * Defines the writer create table statement.
     */
    private static final String WRITER_TABLE_STATEMENT = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER " +
                    "NOT NULL, %s INTEGER NOT NULL, CONSTRAINT Writer_PK PRIMARY KEY (%s, %s), CONSTRAINT " +
                    "Writer_Author_FK FOREIGN KEY (%s) REFERENCES Author(%s), CONSTRAINT Writer_Book_FK FOREIGN " +
                    "KEY (%s) REFERENCES Book(%s));",
            WriterDBManager.TABLE,
            WriterDBManager.AUTHOR,
            WriterDBManager.BOOK,
            WriterDBManager.AUTHOR,
            WriterDBManager.BOOK,
            WriterDBManager.AUTHOR,
            AuthorDBManager.ID,
            WriterDBManager.BOOK,
            BookDBManager.ID);

    /**
     * Defines the index statement.
     */
    private static final String INDEX_STATEMENT = "CREATE INDEX i_%s ON %s(%s);";

    /**
     * Defines the drop statement.
     */
    private static final String DROP_STATEMENT = "DROP TABLE IF EXISTS %s;";

    /**
     * Stores the trigger statement on user table when deleting in order to delete the other user occurrences.
     */
    // TODO: See what's wrong here.
    private static final String USER_TRIGGER_STATEMENT = "CREATE TRIGGER IF NOT EXISTS user_trigger BEFORE DELETE ON " +
            "User FOR EACH ROW BEGIN DELETE FROM Quote WHERE Quote.id_user = User.id_user; DELETE FROM Review WHERE " +
            "Review.id_user = User.id_user; DELETE FROM Profile WHERE Profile.id; END;";

    /**
     * DBHandler's constructor.
     * @param context The associated application context.
     * @param name The name of the database file.
     * @param factory If creating cursor objects, null if default.
     * @param version The database version number.
     */
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Author table
        db.execSQL(AUTHOR_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT, AuthorDBManager.NAME, AuthorDBManager.TABLE, AuthorDBManager.NAME));

        //Book table
        db.execSQL(BOOK_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT, BookDBManager.TITLE, BookDBManager.TABLE, BookDBManager.TITLE));

        //BookListType table
        db.execSQL(BOOK_LIST_TYPE_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT,
                BookListTypeDBManager.NAME,
                BookListTypeDBManager.TABLE,
                BookListTypeDBManager.NAME));

        //Category table
        db.execSQL(CATEGORY_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT,
                CategoryDBManager.NAME,
                CategoryDBManager.TABLE,
                CategoryDBManager.NAME));

        //City table
        db.execSQL(CITY_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT,
                CityDBManager.NAME,
                CityDBManager.TABLE,
                CityDBManager.NAME));

        //Country table
        db.execSQL(COUNTRY_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT,
                CountryDBManager.NAME,
                CountryDBManager.TABLE,
                CountryDBManager.NAME));

        //Profile table
        db.execSQL(PROFILE_TABLE_STATEMENT);

        //Quote table
        db.execSQL(QUOTE_TABLE_STATEMENT);

        //Review table
        db.execSQL(REVIEW_TABLE_STATEMENT);

        //User table
        db.execSQL(USER_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT, UserDBManager.PSEUDO, UserDBManager.TABLE, UserDBManager.PSEUDO));

        //Writer table
        db.execSQL(WRITER_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(String.format(DROP_STATEMENT, AuthorDBManager.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, BookDBManager.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, BookListTypeDBManager.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, CategoryDBManager.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, CityDBManager.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, CountryDBManager.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, ProfileDBManager.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, QuoteDBManager.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, ReviewDBManager.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, UserDBManager.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, WriterDBManager.TABLE));
        db.execSQL(USER_TRIGGER_STATEMENT);
        this.onCreate(db);
    }
}
