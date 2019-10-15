package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.CategoryDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.QuoteDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ReviewDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.WriterDBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Final class representing a book from the application.
 */
@Getter
@Setter
public final class Book extends DBEntity {
    /**
     * Stores the title of the book.
     */
    private String title;

    /**
     * Stores the authors of the book.
     */
    private List<Author> authors;

    /**
     * Stores the path of the cover image.
     */
    private String cover;

    /**
     * Stores the summary of the book.
     */
    private String summary;

    /**
     * Stores the year of the book's publication.
     */
    private int datePublished;

    /**
     * Stores the category of the book.
     */
    private Category category;

    /**
     * Stores the list of quotes of the book.
     */
    private List<Quote> quotes;

    /**
     * Stores the list of reviews of the book.
     */
    private List<Review> reviews;

    /**
     * Book's default constructor.
     */
    public Book() {
        super();

        this.authors = new ArrayList<>();
        this.quotes = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    /**
     * Book's nearly full filled constructor, feeding all attributes except the one related to database (id and
     * deleted).
     * @param title The title to set.
     * @param authors The list of authors to set.
     * @param cover The cover to set.
     * @param summary The summary to set.
     * @param datePublished The publication date to set.
     * @param category The category to set.
     * @param quotes The list of quotes to set.
     * @param reviews The list of reviews to set.
     */
    public Book(String title,
                List<Author> authors,
                String cover,
                String summary,
                int datePublished,
                Category category,
                List<Quote> quotes,
                List<Review> reviews) {
        super();

        this.title = title;
        this.authors = authors;
        this.cover = cover;
        this.summary = summary;
        this.datePublished = datePublished;
        this.category = category;
        this.quotes = quotes;
        this.reviews = reviews;
    }

    /**
     * Book's full filled constructor, feeding all attributes.
     * @param id The id to set.
     * @param title The title to set.
     * @param authors The author to set.
     * @param cover The cover to set.
     * @param summary The summary to set.
     * @param datePublished The publication date to set.
     * @param category The category to set.
     * @param quotes The list of quotes to set.
     * @param reviews The list of reviews to set.
     */
    public Book(int id,
                String title,
                List<Author> authors,
                String cover,
                String summary,
                int datePublished,
                Category category,
                List<Quote> quotes,
                List<Review> reviews) {
        super(id);

        this.title = title;
        this.authors = authors;
        this.cover = cover;
        this.summary = summary;
        this.datePublished = datePublished;
        this.category = category;
        this.quotes = quotes;
        this.reviews = reviews;
    }

    /**
     * Book's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     */
    public Book(Cursor result) {
        this.init(result, true);
    }

    /**
     * Book's full filled constructor providing all its attributes values from the result of a database query, closes
     * the cursor if close is true..
     * @param result The result of the query.
     * @param close Defines if the cursor should be closed or not.
     */
    public Book(Cursor result, boolean close) {
        this.init(result, close);
    }

    /**
     * Book's full filled constructor providing all its attributes values from a ContentValues object.
     * @param contentValues The ContentValues object used to initialize the entity.
     */
    public Book(ContentValues contentValues) {
        this.init(contentValues);
    }
    /**
     * Initializes the book from a ContentValues object.
     * @param contentValues The ContentValues object.
     */
    public void init(@NonNull ContentValues contentValues) {
        this.id = contentValues.getAsInteger(BookDBSchema.ID);
        this.title = contentValues.getAsString(BookDBSchema.TITLE);
        this.cover = contentValues.getAsString(BookDBSchema.COVER);
        this.summary = contentValues.getAsString(BookDBSchema.SUMMARY);
        this.datePublished = contentValues.getAsInteger(BookDBSchema.DATE);
        this.category =
                new CategoryDBManager(App.getAppContext())
                        .loadSQLite(contentValues.getAsInteger(BookDBSchema.CATEGORY));
    }

    /**
     * Initializes the book from a JSON response object (except for relation objects attribute).
     * @param object The JSON response from the API.
     */
    public void init(@NonNull JSONObject object) {
        try {
            this.id = object.getInt(BookDBSchema.ID);
            this.title = object.getString(BookDBSchema.TITLE);
            this.cover = object.getString(BookDBSchema.COVER);
            this.summary = object.getString(BookDBSchema.SUMMARY);
            this.datePublished = object.getInt(BookDBSchema.DATE);
        } catch (JSONException e) {
            this.logError("init", e);
        }
    }

    @Override
    protected void init(@NonNull Cursor result, boolean close) {
        try {
            if (result.getPosition() == -1) {
                result.moveToNext();
            }

            Context context = App.getAppContext();

            this.id = result.getInt(result.getColumnIndexOrThrow(BookDBSchema.ID));
            this.title = result.getString(result.getColumnIndexOrThrow(BookDBSchema.TITLE));
            this.cover = result.getString(result.getColumnIndexOrThrow(BookDBSchema.COVER));
            this.summary = result.getString(result.getColumnIndexOrThrow(BookDBSchema.SUMMARY));
            this.datePublished = result.getInt(result.getColumnIndexOrThrow(BookDBSchema.DATE));
            this.category = new CategoryDBManager(context).loadSQLite(result.getInt(result.getColumnIndexOrThrow
                    (BookDBSchema.CATEGORY)));
            this.reviews = new ReviewDBManager(context).loadBookSQLite(this.id);
            this.quotes = new QuoteDBManager(context).loadBookSQLite(this.id);
            this.authors = new WriterDBManager(context).loadAuthorsSQLite(this.id);

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            this.logError("init", e);
        }
    }
}
