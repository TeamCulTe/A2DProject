package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Quote;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.BOOK;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.QUOTE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.USER;

/**
 * Manager class used to manage the quote entities from databases.
 */
public final class QuoteDBManager extends DBManager {
    /**
     * Stores the base of the quotes API url.
     */
    private final String baseUrl = APIManager.API_URL + APIManager.QUOTES;

    /**
     * QuoteDBManager's constructor.
     * @param context The associated context.
     */
    public QuoteDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull Quote entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(USER, new UserDBManager(this.getContext()).SQLiteGetId(entity.getAuthor()));
            data.put(BOOK, entity.getBookId());
            DBManager.database.insertOrThrow(this.table, null, data);

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
    public boolean updateSQLite(@NonNull Quote entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(QUOTE, entity.getQuote());
            data.put(UPDATE, new Date().toString());

            return DBManager.database.update(this.table, data, whereClause, whereArgs) != 0;
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
    public Quote loadSQLite(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, this.table, ID);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

            return new Quote(result);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From a book id, returns the associated list of quotes.
     * @param idBook The id of the book.
     * @return The list of entities if exists else an empty ArrayList.
     */
    public List<Quote> loadBookSQLite(int idBook) {
        return this.loadSQLite(idBook, BOOK);
    }

    /**
     * From a user id, returns the associated list of quotes.
     * @param idUser The id of the user.
     * @return The list of entities if exists else an empty ArrayList.
     */
    public List<Quote> loadUserSQLite(int idUser) {
        return this.loadSQLite(idUser, USER);
    }

    /**
     * Deletes all quotes entries from the SQLite database for a specific user.
     * @param id The id of the user.
     * @return True if success else false.
     */
    public boolean deleteUserSQLite(int id) {
        try {
            String whereClause = String.format("%s = ?", USER);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return DBManager.database.delete(this.table, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Deletes all quotes entries from the SQLite database for a specific book.
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
     * Deletes all quotes from the SQLite database for a specific id and filter (book or user).
     * @param id The id from which delete the entries.
     * @return True if success else false.
     */
    private boolean deleteSQLite(int id, String filter) {
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
     * From an id and a filter type (column), returns the associated list of quotes.
     * @param id The id to filter on.
     * @param column The id column to filter on.
     * @return The list of entities if exists else an empty ArrayList.
     */
    private List<Quote> loadSQLite(int id, String column) {
        try {
            ArrayList<Quote> quotes = new ArrayList<>();
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, this.table, column);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                quotes.add(new Quote(result, false));
            }

            result.close();

            return quotes;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Query all the quotes from the database.
     * @return The list of quotes.
     */
    public List<Quote> queryAllSQLite() {
        List<Quote> quotes = new ArrayList<>();

        try {
            Cursor result = DBManager.database.rawQuery(String.format(QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    quotes.add(new Quote(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }

        return quotes;
    }

    /**
     * From the API, query the list of all quotes from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(baseUrl + APIManager.READ);
    }

    /**
     * Creates a quote entity in MySQL database.
     * @param quote The quote to create.
     */
    public void createMySQL(Quote quote) {
        String url = String.format(baseUrl + APIManager.CREATE + USER + "=%s&" + BOOK + "=%s&" + QUOTE + "=%s",
                new UserDBManager(this.getContext()).SQLiteGetId(quote.getAuthor()),
                quote.getBookId(),
                quote.getQuote());

        super.requestString(Request.Method.POST, url, null);
    }

    /**
     * Updates a quote entity in MySQL database.
     * @param quote The quote to update.
     */
    public void updateMySQL(Quote quote) {
        String url = String.format(baseUrl + APIManager.UPDATE + ID + "=%s&" + QUOTE + "=%s",
                quote.getId(),
                quote.getQuote());

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Loads a quote from MySQL database.
     * @param id The id of the quote to load.
     * @return The loaded quote.
     */
    public Quote loadMySQL(int id) {
        final Quote quote = new Quote();

        String url = String.format(baseUrl + APIManager.READ + ID + "=%s", id);

        super.requestJsonObject(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                quote.init(response);
            }
        });

        return quote;
    }

    /**
     * Deletes a quote entity in MySQL database.
     * @param id The id of the entity to delete.
     */
    public void deleteMySQL(int id) {
        String url = String.format(baseUrl + APIManager.DELETE + ID + "=%s", id);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Deletes all quote entities in MySQL database from a specific user.
     * @param idUser The id of the user.
     */
    public void deleteUserMySQL(int idUser) {
        String url = String.format(baseUrl + APIManager.DELETE + USER + "=%s", idUser);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Restores a quote entity in MySQL database.
     * @param id The id of the entity to delete.
     */
    public void restoreMySQL(int id) {
        String url = String.format(baseUrl + APIManager.RESTORE + ID + "=%s", id);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Restores all quote entities in MySQL database from a specific user.
     * @param idUser The id of the user.
     */
    public void restoreUserMySQL(int idUser) {
        String url = String.format(baseUrl + APIManager.RESTORE + USER + "=%s", idUser);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Deletes a quote entity in MySQL database.
     * @param quote The quote to delete.
     */
    public void deleteMySQL(Quote quote) {
        this.deleteMySQL(quote.getId());
    }

    @Override
    protected void createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(USER, entity.getInt(USER));
            data.put(BOOK, entity.getInt(BOOK));
            data.put(QUOTE, entity.getString(QUOTE));
            DBManager.database.insertOrThrow(this.table, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
