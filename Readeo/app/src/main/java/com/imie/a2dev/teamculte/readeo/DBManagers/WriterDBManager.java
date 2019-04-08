package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema.AUTHOR;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema.BOOK;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema.TABLE;

/**
 * Manager class used to manage the writer entities from databases.
 */
public final class WriterDBManager extends DBManager {
    /**
     * WriterDBManager's constructor.
     * @param context The associated context.
     */
    public WriterDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{AUTHOR, BOOK};
        this.baseUrl = APIManager.API_URL + APIManager.WRITERS;
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
                this.database.insertOrThrow(this.table, null, data);
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
    public List<Author> loadAuthorsSQLite(int idBook) {
        try {
            ArrayList<Author> authors = new ArrayList<>();
            AuthorDBManager authorDBManager = new AuthorDBManager(this.getContext());
            String[] selectArgs = {String.valueOf(idBook)};
            String query = String.format(this.SIMPLE_QUERY_ALL, this.table, BOOK);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                authors.add(authorDBManager.loadSQLite(result.getInt(result.getColumnIndexOrThrow(AUTHOR))));
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
    public List<Book> loadBooksSQLite(int idAuthor) {
        try {
            BookDBManager bookDBManager = new BookDBManager(this.getContext());
            ArrayList<Book> books = new ArrayList<>();
            String[] selectArgs = {String.valueOf(idAuthor)};
            String query = String.format(this.SIMPLE_QUERY_ALL, this.table, AUTHOR);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(bookDBManager.loadMySQL(result.getInt(result.getColumnIndex(BOOK))));
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

            return this.database.delete(this.table, whereClause, whereArgs) != 0;
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

            return this.database.delete(this.table, whereClause, whereArgs) != 0;
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
    public boolean deleteAuthorSQLite(int id) {
        try {
            String whereClause = String.format("%s = ?", AUTHOR);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(this.table, whereClause, whereArgs) != 0;
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

            return this.database.delete(this.table, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From the API, query the list of all writers from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(baseUrl + APIManager.READ);
    }

    /**
     * Creates a writer entity in MySQL database.
     * @param idAuthor The id of the associated author.
     * @param idBook The id of the book.
     */
    public void createMySQL(int idAuthor, int idBook) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        param.put(AUTHOR, String.valueOf(idAuthor));
        param.put(BOOK, String.valueOf(idBook));

        super.requestString(Request.Method.POST, url, null, param);
    }

    /**
     * Deletes the test entity in MySQL database.
     * @param idAuthor The id of the associated author to delete.
     * @param idBook The id of the book.
     */
    public void deleteTestEntityMySQL(int idAuthor, int idBook) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(this.ids[0], String.valueOf(idAuthor));
        param.put(this.ids[1], String.valueOf(idBook));

        param.put(APIManager.TEST, "1");

        this.requestString(Request.Method.DELETE, url, null, param);
    }

    @Override
    public String getFieldSQLite(String field, int id) {
        return null;
    }

    @Override
    public boolean deleteSQLite(int id) {
        return false;
    }

    @Override
    protected void createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(AUTHOR, entity.getInt(AUTHOR));
            data.put(BOOK, entity.getInt(BOOK));
            this.database.insertOrThrow(this.table, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }

    @Override
    public boolean updateSQLite(@NonNull JSONObject entity) {
        // Nothing to do as the entity is just a relation between Authors and Books.
        return false;
    }
}
