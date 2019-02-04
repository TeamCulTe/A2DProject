package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema.AUTHOR;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema.BOOK;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema.TABLE;

/**
 * Manager class used to manage the writer entities from databases.
 */
public final class WriterDBManager extends DBManager {
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

        this.table = TABLE;
        this.ids = new String[]{AUTHOR, BOOK};
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
                DBManager.database.insertOrThrow(this.table, null, data);
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
            AuthorDBManager authorDBManager = new AuthorDBManager(this.getContext());
            String[] selectArgs = {String.valueOf(idBook)};
            String query = String.format(SIMPLE_QUERY_ALL, this.table, BOOK);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

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
    public List<Book> loadBookSQLites(int idAuthor) {
        try {
            ArrayList<Book> books = new ArrayList<>();
            String[] selectArgs = {String.valueOf(idAuthor)};
            String query = String.format(SIMPLE_QUERY_ALL, this.table, AUTHOR);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

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

            return DBManager.database.delete(this.table, whereClause, whereArgs) != 0;
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

            return DBManager.database.delete(this.table, whereClause, whereArgs) != 0;
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

            return DBManager.database.delete(this.table, whereClause, whereArgs) != 0;
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

            return DBManager.database.delete(this.table, whereClause, whereArgs) != 0;
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
            DBManager.database.insertOrThrow(this.table, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }

    @Override
    protected String[][] getUpdateFieldsSQLite() {
        try {
            int fieldsNumber = 3;
            String query = String.format(DOUBLE_QUERY_UPDATE, ids[0], ids[1], this.table);
            Cursor result = DBManager.database.rawQuery(query, null);
            String[][] data = new String[result.getCount() - 1][fieldsNumber];
            int i = 0;

            while (result.moveToNext()) {
                data[i][0] = result.getString(result.getColumnIndex(ids[0]));
                data[i][1] = result.getString(result.getColumnIndex(ids[1]));
                data[i][2] = result.getString(result.getColumnIndex(UPDATE));

                i++;
            }

            result.close();

            return data;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }
}
