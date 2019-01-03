package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
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
     * UserDBManager's constructor.
     * @param context The associated context.
     */
    public WriterDBManager(Context context) {
        super(context);
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean SQLiteCreate(@NonNull Book entity) {
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
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From a book id, returns the associated list of authors.
     * @param idBook The id of the book.
     * @return The list of entities if exists else else an empty ArrayList.
     */
    public List<Author> SQLiteLoadAuthors(int idBook) {
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
            Log.e(TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From an author id, returns the associated list of books.
     * @param idAuthor The id of the author.
     * @return The list of entities if exists else an empty ArrayList.
     */
    public List<Book> SQLiteLoadBooks(int idAuthor) {
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
            Log.e(TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Deletes a writer from the SQLite database.
     * @param idAuthor The id of the author.
     * @param idBook The id of the book.
     * @return True if success else false.
     */
    public boolean SQLiteDelete(int idAuthor, int idBook) {
        try {
            String whereClause = String.format("%s = ? AND %s = ?", AUTHOR, BOOK);
            String[] whereArgs = new String[]{String.valueOf(idAuthor), String.valueOf(idBook)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all writers from the SQLite database for a specific id and filter (book or author).
     * @param id The id from which delete the entries.
     * @return True if success else false.
     */
    public boolean SQLiteDelete(int id, String filter) {
        try {
            String whereClause = String.format("%s = ?", filter);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all writers entries from the SQLite database for a specific author.
     * @param id The id of the author.
     * @return True if success else false.
     */
    public boolean SQLiteDeleteAuthor(int id) {
        try {
            String whereClause = String.format("%s = ?", AUTHOR);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all writers entries from the SQLite database for a specific book.
     * @param id The id of the book.
     * @return True if success else false.
     */
    public boolean SQLiteDeleteBook(int id) {
        try {
            String whereClause = String.format("%s = ?", BOOK);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }
}
