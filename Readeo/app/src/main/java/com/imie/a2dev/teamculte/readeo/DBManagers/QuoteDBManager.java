package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Quote;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class used to manage the quote entities from databases.
 */
public final class QuoteDBManager extends DBManager {
    /**
     * Defines the quote's table name.
     */
    public static final String TABLE = "Quote";

    /**
     * Defines the quote's id field.
     */
    public static final String ID = "id_quote";

    /**
     * Defines the quote's user id field.
     */
    public static final String USER = UserDBManager.ID;

    /**
     * Defines the quote's book id field.
     */
    public static final String BOOK = BookDBManager.ID;

    /**
     * Defines the quote's quote text field.
     */
    public static final String QUOTE = "quote";

    /**
     * QuoteDBManager's constructor.
     * @param context The associated context.
     */
    public QuoteDBManager(Context context) {
        super(context);
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean SQLiteCreate(@NonNull Quote entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(USER, new UserDBManager(this.getContext()).SQLiteGetId(entity.getAuthor()));
            data.put(BOOK, entity.getBookId());
            this.database.insertOrThrow(TABLE, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param id The id of the db entity to access.
     * @return The value of the field.
     */
    public String SQLiteGetField(String field, int id) {
        return this.SQLiteGetField(field, TABLE, ID, id);
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean SQLiteUpdate(@NonNull Quote entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(USER, new UserDBManager(this.getContext()).SQLiteGetId(entity.getAuthor()));
            data.put(BOOK, entity.getBookId());
            data.put(QUOTE, entity.getQuote());

            return this.database.update(TABLE, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From an id, returns the associated java entity.
     * @param id The id of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public Quote SQLiteLoad(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, ID);
            Cursor result = this.database.rawQuery(query, selectArgs);

            return new Quote(result);
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a book id, returns the associated list of quotes.
     * @param idBook The id of the book.
     * @return The list of entities if exists else an empty ArrayList.
     */
    public List<Quote> SQLiteLoadBook(int idBook) {
        return this.SQLiteLoad(idBook, BOOK);
    }

    /**
     * From a user id, returns the associated list of quotes.
     * @param idUser The id of the user.
     * @return The list of entities if exists else an empty ArrayList.
     */
    public List<Quote> SQLiteLoadUser(int idUser) {
        return this.SQLiteLoad(idUser, USER);
    }

    /**
     * From an id given in parameter, deletes the associated entity in the database.
     * @param id The id of the entity to delete.
     * @return true if success else false.
     */
    public boolean SQLiteDelete(int id) {
        try {
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all quotes entries from the SQLite database for a specific user.
     * @param id The id of the user.
     * @return True if success else false.
     */
    public boolean SQLiteDeleteAuthor(int id) {
        try {
            String whereClause = String.format("%s = ?", USER);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return this.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all quotes entries from the SQLite database for a specific book.
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

    /**
     * Deletes all quotes from the SQLite database for a specific id and filter (book or user).
     * @param id The id from which delete the entries.
     * @return True if success else false.
     */
    private boolean SQLiteDelete(int id, String filter) {
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
     * From an id and a filter type (column), returns the associated list of quotes.
     * @param id The id to filter on.
     * @param column The id column to filter on.
     * @return The list of entities if exists else an empty ArrayList.
     */
    private List<Quote> SQLiteLoad(int id, String column) {
        try {
            ArrayList<Quote> quotes = new ArrayList<>();
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, column);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                quotes.add(new Quote(result, false));
            }

            result.close();

            return quotes;
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Query all the quotes from the database.
     * @return The list of quotes.
     */
    public List<Quote> queryAll() {
        List<Quote> quotes = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(QUERY_ALL, TABLE), null);

            while (result.moveToNext()) {
                quotes.add(new Quote(result));
            }

        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }

        return quotes;
    }
}
