package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.TITLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.CATEGORY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.COVER;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.SUMMARY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.DATE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;

/**
 * Manager class used to manage the book entities from databases.
 */
public final class BookDBManager extends DBManager {
    /**
     * BookDBManager's constructor.
     * @param context The associated context.
     */
    public BookDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
        this.baseUrl = APIManager.API_URL + APIManager.BOOKS;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull Book entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(TITLE, entity.getTitle());
            data.put(CATEGORY, entity.getCategory().getId());
            data.put(COVER, entity.getCover());
            data.put(SUMMARY, entity.getSummary());
            data.put(DATE, entity.getDatePublished());

            this.database.insertOrThrow(this.table, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull Book entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(TITLE, entity.getTitle());
            data.put(CATEGORY, entity.getCategory().getId());
            data.put(COVER, entity.getCover());
            data.put(SUMMARY, entity.getSummary());
            data.put(DATE, entity.getDatePublished());
            data.put(UPDATE, new Date().toString());

            return this.database.update(this.table, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From an id, returns the associated java entity.
     * @param id The id of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public Book loadSQLite(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(this.SIMPLE_QUERY_ALL, this.table, ID);
            Cursor result = this.database.rawQuery(query, selectArgs);

            return new Book(result);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a string and a field, returns the associated java books where the string matches in the field values.
     * @param field The field to filter on.
     * @param filter The string that should match.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadFieldFilteredSQLite(String field, String filter) {
        try {
            List<Book> books = new ArrayList<>();
            String[] selectArgs = {filter};
            String query = String.format(this.SIMPLE_QUERY_ALL_LIKE_START, this.table, field);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result));
            }

            return books;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a string, returns the associated java books where the string matches in the category values.
     * @param filter The string that should match.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadCategoryNameFilteredSQLite(String filter) {
        try {
            List<Book> books = new ArrayList<>();
            String[] selectArgs = {filter};
            String query = String.format("SELECT %s.* FROM %s INNER JOIN %s ON %s.%s = %s.%s WHERE %s LIKE '?%%')",
                    this.table,
                    this.table,
                    CategoryDBSchema.TABLE,
                    this.table,
                    CATEGORY,
                    CategoryDBSchema.TABLE,
                    ID,
                    CategoryDBSchema.NAME);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result));
            }

            return books;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a string, returns the associated java books where the string matches in the book writers.
     * @param filter The string that should match.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadAuthorNameFilteredSQLite(String filter) {
        try {
            List<Book> books = new ArrayList<>();
            String[] selectArgs = {filter};
            String query = String.format("SELECT %s.* FROM %s INNER JOIN %s ON %s.%s = %s.%s " +
                            "INNER JOIN %s ON %s.%s = %s.%s WHERE %s.%s LIKE '?%%'",
                    this.table,
                    this.table,
                    WriterDBSchema.TABLE,
                    this.table,
                    ID,
                    WriterDBSchema.TABLE,
                    WriterDBSchema.BOOK,
                    AuthorDBSchema.TABLE,
                    AuthorDBSchema.TABLE,
                    AuthorDBSchema.ID,
                    WriterDBSchema.TABLE,
                    WriterDBSchema.AUTHOR,
                    AuthorDBSchema.TABLE,
                    AuthorDBSchema.NAME);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result));
            }

            return books;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a string filter and a value, returns the associated java books where the value matches in the filter values.
     * Uses the common filter fields (Author, Category, or inner Book fields).
     * @param filter The filter to filter on (matching to database inner or joined fields).
     * @param value The string value that should match.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadFilteredSQLite(String filter, String value) {
        switch (filter) {
            case CategoryDBSchema.NAME:
                return this.loadCategoryNameFilteredSQLite(value);
            case AuthorDBSchema.NAME:
                return this.loadAuthorNameFilteredSQLite(value);
            default:
                return this.loadFieldFilteredSQLite(filter, value);
        }
    }

    /**
     * From a category id, returns the associated list of books.
     * @param idCategory The id of the category to load books from the database.
     * @return The list of books.
     */
    public List<Book> loadCategorySQLite(int idCategory) {
        try {
            List<Book> books = new ArrayList<>();
            String[] selectArgs = {String.valueOf(idCategory)};
            String query = String.format(this.SIMPLE_QUERY_ALL, this.table, CATEGORY);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result));
            }

            result.close();

            return books;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Queries all the books from the database.
     * @return The list of books.
     */
    public List<Book> queryAllSQLite() {
        //TODO : Make the query better to improve loading time...
        List<Book> books = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    books.add(new Book(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }

        return books;
    }

    /**
     * From the API, query the list of all books from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(baseUrl + APIManager.READ);
    }

    @Override
    protected void createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(TITLE, entity.getString(TITLE));
            data.put(CATEGORY, entity.getInt(CATEGORY));
            data.put(COVER, entity.getString(COVER));
            data.put(SUMMARY, entity.getString(SUMMARY));
            data.put(DATE, entity.getInt(DATE));
            this.database.insertOrThrow(this.table, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }

    @Override
    public boolean updateSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{entity.getString(ID)};

            data.put(TITLE, entity.getString(TITLE));
            data.put(CATEGORY, entity.getInt(CATEGORY));
            data.put(COVER, entity.getString(COVER));
            data.put(SUMMARY, entity.getString(SUMMARY));
            data.put(DATE, entity.getInt(DATE));

            return this.database.update(this.table, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }
}
