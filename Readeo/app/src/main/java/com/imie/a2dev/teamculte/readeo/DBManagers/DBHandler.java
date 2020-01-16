package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
     * Defines the database version.
     */
    private static final int VERSION = 1;
    /**
     * Using singleton pattern, stores the instance.
     */
    private static DBHandler instance;

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

    public static DBHandler getInstance() {
        if (DBHandler.instance == null) {
            DBHandler.instance = new DBHandler(App.getAppContext(), DBManager.dbFileName, null, VERSION);
        }

        return DBHandler.instance;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
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

    public void copyDatabase(String databaseName) {
        try {
            InputStream myinput = App.getAppContext().getAssets().open("databases/" + databaseName);
            String outfilename = App.getAppContext().getDatabasePath(DBManager.getDbFileName()).getAbsolutePath();
            OutputStream myoutput = new FileOutputStream(outfilename);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = myinput.read(buffer)) > 0) {
                myoutput.write(buffer, 0, length);
            }

            myoutput.flush();
            myoutput.close();
            myinput.close();
        } catch (IOException e) {
            Log.e(String.format("[%s:%s] : ", this.getClass().getName(), "copyTestDatabase"), e.getMessage());
        }
    }
}
