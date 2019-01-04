package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Response;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class used to manage the writer entities from databases.
 */
public final class WriterDBManager extends DBManager {
    /**
     * Defines the writer's table name.
     */
    public static final String TABLE = "Writer";

    /**
     * Defines the user's pseudo field.
     */
    public static final String AUTHOR = AuthorDBManager.ID;

    /**
     * Defines the user's pseudo field.
     */
    public static final String BOOK = BookDBManager.ID;

    /**
     * Stores the base of the users API url.
     */
    private final String baseUrl = APIManager.API_URL + APIManager.WRITERS;

    /**
     * UserDBManager's constructor.
     * @param context The associated context.
     */
    public WriterDBManager(Context context) {
        super(context);
    }

    /**
     * From a java book creates the associated writers into the database.
     * @param entity The model to use.
     * @return true if success else false.
     */
    public boolean createSQLiteBook(@NonNull Book entity) {
        try {
            ContentValues data;

            int idBook = entity.getId();

            for (Author author : entity.getAuthors()) {
                data = new ContentValues();

                data.put(BOOK, idBook);
                data.put(AUTHOR, author.getId());
                this.database.insertOrThrow(TABLE, null, data);
            }

            return true;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From a book id, returns the associated list of authors.
     * @param idBook The id of the book.
     * @return The list of entities if exists else else an empty ArrayList.
     */
    public List<Author> loadSQLiteAuthors(int idBook) {
        try {
            ArrayList<Author> authors = new ArrayList<>();
            String[] selectArgs = {String.valueOf(idBook)};
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, BOOK);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                authors.add(new Author(result, false));
            }

            result.close();

            return authors;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From an author id, returns the associated list of books.
     * @param idAuthor The id of the author.
     * @return The list of entities if exists else an empty ArrayList.
     */
    public List<Book> loadBookSQLites(int idAuthor) {
        try {
            ArrayList<Book> books = new ArrayList<>();
            String[] selectArgs = {String.valueOf(idAuthor)};
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, AUTHOR);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result, false));
            }

            result.close();

            return books;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Deletes a writer from the SQLite database.
     * @param idAuthor The id of the author.
     * @param idBook The id of the book.
     * @return True if success else false.
     */
    public boolean deleteSQLite(int idAuthor, int idBook) {
        try {
            String whereClause = String.format("%s = ? AND %s = ?", AUTHOR, BOOK);
            String[] whereArgs = new String[]{String.valueOf(idAuthor), String.valueOf(idBook)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all writers from the SQLite database for a specific id and filter (book or author).
     * @param id The id from which delete the entries.
     * @return True if success else false.
     */
    public boolean deleteSQLite(int id, String filter) {
        try {
            String whereClause = String.format("%s = ?", filter);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all writers entries from the SQLite database for a specific author.
     * @param id The id of the author.
     * @return True if success else false.
     */
    public boolean deleteSQLiteAuthor(int id) {
        try {
            String whereClause = String.format("%s = ?", AUTHOR);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all writers entries from the SQLite database for a specific book.
     * @param id The id of the book.
     * @return True if success else false.
     */
    public boolean deleteBookSQLite(int id) {
        try {
            String whereClause = String.format("%s = ?", BOOK);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From the API, query the list of all writers from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importAllFromMySQL() {
        super.importAllFromMySQL(baseUrl + APIManager.READ);
    }

    @Override
    protected void createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(AUTHOR, entity.getInt(AUTHOR));
            data.put(BOOK, entity.getInt(BOOK));
            this.database.insertOrThrow(TABLE, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
