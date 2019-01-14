package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.QuoteDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Final class representing a quote from a user to a specific book.
 */
public final class Quote extends DBEntity {
    /**
     * Stores the pseudo of the user who wrote the quote.
     */
    private String author;

    /**
     * Stores the id of the associated book.
     */
    private int bookId;

    /**
     * Stores the text of the quote.
     */
    private String quote;

    /**
     * Quote's default constructor.
     */
    public Quote() {
        super();
    }

    /**
     * Quote's nearly full filled constructor, providing all attributes values except for the database's related ones.
     * @param author The user's pseudo related to the quote.
     * @param bookId The id of the associated book.
     * @param quote The text of the quote to set.
     */
    public Quote(String author, int bookId, String quote) {
        super();

        this.author = author;
        this.bookId = bookId;
        this.quote = quote;
    }

    /**
     * Quote's full filled constructor, providing all attributes values.
     * @param id The id to set.
     * @param author The user's pseudo related to the quote.
     * @param bookId The id of the associated book.
     * @param quote The text of the quote to set.
     */
    public Quote(int id, int bookId, String author, String quote) {
        super(id);

        this.author = author;
        this.bookId = bookId;
        this.quote = quote;
    }

    /**
     * Quote's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     */
    public Quote(Cursor result) {
        this.init(result, true);
    }

    /**
     * Quote's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     * @param close Defines if the cursor should be closed or not.
     */
    public Quote(Cursor result, boolean close) {
        this.init(result, close);
    }

    /**
     * Gets the author attribute.
     * @return The String value of author attribute.
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Gets the bookId attribute.
     * @return The int value of bookId attribute.
     */
    public int getBookId() {
        return this.bookId;
    }

    /**
     * Gets the quote attribute.
     * @return The String value of quote attribute.
     */
    public String getQuote() {
        return this.quote;
    }

    /**
     * Sets the author attribute.
     * @param newAuthor The new String value to set.
     */
    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }

    /**
     * Sets the bookId attribute.
     * @param newBookId The new int value to set.
     */
    public void setBookId(int newBookId) {
        this.bookId = newBookId;
    }

    /**
     * Sets the quote attribute.
     * @param newQuote The new String value to set.
     */
    public void setQuote(String newQuote) {
        this.quote = newQuote;
    }

    /**
     * Initializes the quote from a JSON response object.
     * @param object The JSON response from the API.
     */
    public void init(JSONObject object) {
        try {
            this.setId(object.getInt(QuoteDBManager.ID));
            this.setBookId(object.getInt(QuoteDBManager.BOOK));
            this.setQuote(object.getString(QuoteDBManager.QUOTE));
            // TODO : See how to get the name of the user -> Getting it from sqlite db ?
        } catch (JSONException e) {
            Log.e(DBManager.JSON_TAG, e.getMessage());
        }
    }

    @Override
    protected void init(Cursor result, boolean close) {
        try {
            if (result.getPosition() == -1) {
                result.moveToNext();
            }

            this.id = result.getInt(result.getColumnIndexOrThrow(QuoteDBManager.ID));
            this.bookId = result.getInt(result.getColumnIndexOrThrow(QuoteDBManager.BOOK));
            this.author = new UserDBManager(App.getAppContext()).getFieldSQLite(UserDBManager.PSEUDO,
                    result.getInt(result.getColumnIndexOrThrow(QuoteDBManager.USER)));
            this.quote = result.getString(result.getColumnIndexOrThrow(QuoteDBManager.QUOTE));

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(DBManager.SQLITE_TAG, e.getMessage());
        }
    }
}
