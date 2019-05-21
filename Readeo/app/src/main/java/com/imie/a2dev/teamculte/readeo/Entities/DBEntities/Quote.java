package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.JSON_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.SQLITE_TAG;

/**
 * Final class representing a quote from a user to a specific book.
 */
@Getter
@Setter
public final class Quote extends DBEntity {
    /**
     * Stores the id of the user who wrote the quote.
     */
    private int userId;

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
     * @param userId The user's id related to the quote.
     * @param bookId The id of the associated book.
     * @param quote The text of the quote to set.
     */
    public Quote(int userId, int bookId, String quote) {
        super();

        this.userId = userId;
        this.bookId = bookId;
        this.quote = quote;
    }

    /**
     * Quote's full filled constructor, providing all attributes values.
     * @param id The id to set.
     * @param userId The user's id related to the quote.
     * @param bookId The id of the associated book.
     * @param quote The text of the quote to set.
     */
    public Quote(int id, int bookId, int userId, String quote) {
        super(id);

        this.userId = userId;
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
     * Quote's full filled constructor providing all its attributes values from a json object.
     * @param result The json object.
     */
    public Quote(JSONObject result) {
        this.init(result);
    }

    /**
     * Quote's full filled constructor providing all its attributes values from a ContentValues object.
     * @param contentValues The ContentValues object used to initialize the entity.
     */
    public Quote(ContentValues contentValues) {
        this.init(contentValues);
    }

    /**
     * Initializes the quote from a ContentValues object.
     * @param contentValues The ContentValues object.
     */
    public void init(@NonNull ContentValues contentValues) {
        this.id = contentValues.getAsInteger(QuoteDBSchema.ID);
        this.bookId = contentValues.getAsInteger(QuoteDBSchema.BOOK);
        this.userId = contentValues.getAsInteger(QuoteDBSchema.USER);
        this.quote = contentValues.getAsString(QuoteDBSchema.QUOTE);
    }

    /**
     * Initializes the quote from a JSON response object.
     * @param object The JSON response from the API.
     */
    public void init(@NonNull JSONObject object) {
        try {
            this.id = object.getInt(QuoteDBSchema.ID);
            this.bookId = object.getInt(QuoteDBSchema.BOOK);
            this.userId = object.getInt(QuoteDBSchema.USER);
            this.quote = object.getString(QuoteDBSchema.QUOTE);
        } catch (JSONException e) {
            Log.e(JSON_TAG, e.getMessage());
        }
    }

    @Override
    protected void init(@NonNull Cursor result, boolean close) {
        try {
            if (result.getPosition() == -1) {
                result.moveToNext();
            }

            this.id = result.getInt(result.getColumnIndexOrThrow(QuoteDBSchema.ID));
            this.bookId = result.getInt(result.getColumnIndexOrThrow(QuoteDBSchema.BOOK));
            this.userId = result.getInt(result.getColumnIndexOrThrow(QuoteDBSchema.USER));
            this.quote = result.getString(result.getColumnIndexOrThrow(QuoteDBSchema.QUOTE));

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
