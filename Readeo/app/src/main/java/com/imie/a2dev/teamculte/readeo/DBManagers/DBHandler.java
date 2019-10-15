package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CityDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CountryDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Class used to manage database structure (tables, upgrades...).
 */
public final class DBHandler extends SQLiteAssetHelper {
    /**
     * Defines the index statement.
     */
    private static final String INDEX_STATEMENT = "CREATE INDEX i_%s ON %s(%s);";

    /**
     * Defines the drop statement.
     */
    private static final String DROP_STATEMENT = "DROP TABLE IF EXISTS %s;";

    /**
     * Using singleton pattern, stores the instance.
     */
    private static DBHandler instance;

    /**
     * Defines the database version.
     */
    private static final int VERSION = 1;

    /**
     * DBHandler's constructor.
     * @param context The associated application context.
     * @param name The name of the database file.
     * @param factory If creating cursor objects, null if default.
     * @param version The database version number.
     */
    private DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
    
    public static DBHandler getInstance() {
        if (DBHandler.instance == null) {
            DBHandler.instance = new DBHandler(App.getAppContext(), DBManager.dbFileName, null, VERSION);
        }
        
        return DBHandler.instance;
    }

    /**
     * Creates the database.
     * @param db The database object.
     */
    public void create(SQLiteDatabase db) {
        //Author table
        db.execSQL(AuthorDBSchema.AUTHOR_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT, AuthorDBSchema.NAME, AuthorDBSchema.TABLE, AuthorDBSchema.NAME));

        //BookListType table
        db.execSQL(BookListTypeDBSchema.BOOK_LIST_TYPE_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT,
                                 BookListTypeDBSchema.NAME,
                                 BookListTypeDBSchema.TABLE,
                                 BookListTypeDBSchema.NAME));

        //Category table
        db.execSQL(CategoryDBSchema.CATEGORY_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT,
                                 CategoryDBSchema.NAME,
                                 CategoryDBSchema.TABLE,
                                 CategoryDBSchema.NAME));

        //Book table
        db.execSQL(BookDBSchema.BOOK_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT, BookDBSchema.TITLE, BookDBSchema.TABLE, BookDBSchema.TITLE));

        //City table
        db.execSQL(CityDBSchema.CITY_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT,
                                 CityDBSchema.NAME,
                                 CityDBSchema.TABLE,
                                 CityDBSchema.NAME));

        //Country table
        db.execSQL(CountryDBSchema.COUNTRY_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT,
                                 CountryDBSchema.NAME,
                                 CountryDBSchema.TABLE,
                                 CountryDBSchema.NAME));

        //Profile table
        db.execSQL(ProfileDBSchema.PROFILE_TABLE_STATEMENT);

        //Quote table
        db.execSQL(QuoteDBSchema.QUOTE_TABLE_STATEMENT);

        //Review table
        db.execSQL(ReviewDBSchema.REVIEW_TABLE_STATEMENT);

        //User table
        db.execSQL(UserDBSchema.USER_TABLE_STATEMENT);
        db.execSQL(String.format(INDEX_STATEMENT, UserDBSchema.PSEUDO, UserDBSchema.TABLE, UserDBSchema.PSEUDO));
        db.execSQL(UserDBSchema.USER_TRIGGER_STATEMENT);

        //Writer table
        db.execSQL(WriterDBSchema.WRITER_TABLE_STATEMENT);

        //BookListType table
        db.execSQL(BookListTypeDBSchema.BOOK_LIST_TYPE_TABLE_STATEMENT);

        //BookList table
        db.execSQL(BookListDBSchema.BOOK_LIST_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(String.format(DROP_STATEMENT, AuthorDBSchema.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, BookDBSchema.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, BookListDBSchema.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, BookListTypeDBSchema.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, CategoryDBSchema.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, CityDBSchema.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, CountryDBSchema.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, ProfileDBSchema.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, QuoteDBSchema.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, ReviewDBSchema.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, UserDBSchema.TABLE));
        db.execSQL(String.format(DROP_STATEMENT, WriterDBSchema.TABLE));

        this.onCreate(db);
    }
}
