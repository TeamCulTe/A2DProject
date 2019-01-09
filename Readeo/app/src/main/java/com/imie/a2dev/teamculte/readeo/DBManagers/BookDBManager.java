package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class used to manage the book entities from databases.
 */
public final class BookDBManager extends DBManager {
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
    public static final String CATEGORY = CategoryDBManager.ID;

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
     * Stores the base of the books API url.
     */
    private final String baseUrl = APIManager.API_URL + APIManager.BOOKS;

    /**
     * BookDBManager's constructor.
     * @param context The associated context.
     */
    public BookDBManager(Context context) {
        super(context);
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

            DBManager.database.insertOrThrow(TABLE, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param id The id of the db entity to access.
     * @return The value of the field.
     */
    public String getFieldSQLite(String field, int id) {
        return this.getFieldSQLite(field, TABLE, ID, id);
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

            return DBManager.database.update(TABLE, data, whereClause, whereArgs) != 0;
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
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, ID);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

            return new Book(result);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From an id given in parameter, deletes the associated entity in the database.
     * @param id The id of the entity to delete.
     * @return true if success else false.
     */
    public boolean deleteSQLite(int id) {
        try {
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return DBManager.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
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
            Cursor result = DBManager.database.rawQuery(String.format(QUERY_ALL, TABLE), null);

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
    public void importAllFromMySQL() {
        super.importAllFromMySQL(baseUrl + APIManager.READ);
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
            DBManager.database.insertOrThrow(TABLE, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
