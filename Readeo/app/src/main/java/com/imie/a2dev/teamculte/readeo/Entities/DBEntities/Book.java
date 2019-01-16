package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.CategoryDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.QuoteDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ReviewDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.WriterDBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * Final class representing a book from the application.
 */
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
     * Gets the title attribute.
     * @return The String value of title attribute.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the author attribute.
     * @return The List<Author> value of author attribute.
     */
    public List<Author> getAuthors() {
        return this.authors;
    }

    /**
     * Gets the cover attribute.
     * @return The String value of cover attribute.
     */
    public String getCover() {
        return this.cover;
    }

    /**
     * Gets the summary attribute.
     * @return The String value of summary attribute.
     */
    public String getSummary() {
        return this.summary;
    }

    /**
     * Gets the datePublished attribute.
     * @return The int value of datePublished attribute.
     */
    public int getDatePublished() {
        return this.datePublished;
    }

    /**
     * Gets the category attribute.
     * @return The Category value of category attribute.
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * Gets the quotes attribute.
     * @return The List<Quote> value of quotes attribute.
     */
    public List<Quote> getQuotes() {
        return this.quotes;
    }

    /**
     * Gets the reviews attribute.
     * @return The List<Review> value of reviews attribute.
     */
    public List<Review> getReviews() {
        return this.reviews;
    }

    /**
     * Sets the title attribute.
     * @param newTitle The new String value to set.
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     * Sets the author attribute.
     * @param newAuthors The new List<Author> value to set.
     */
    public void setAuthors(List<Author> newAuthors) {
        this.authors = newAuthors;
    }

    /**
     * Sets the cover attribute.
     * @param newCover The new String value to set.
     */
    public void setCover(String newCover) {
        this.cover = newCover;
    }

    /**
     * Sets the summary attribute.
     * @param newSummary The new String value to set.
     */
    public void setSummary(String newSummary) {
        this.summary = newSummary;
    }

    /**
     * Sets the datePublished attribute.
     * @param newDatePublished The new int value to set.
     */
    public void setDatePublished(int newDatePublished) {
        this.datePublished = newDatePublished;
    }

    /**
     * Sets the category attribute.
     * @param newCategory The new Category value to set.
     */
    public void setCategory(Category newCategory) {
        this.category = newCategory;
    }

    /**
     * Sets the quotes attribute.
     * @param newQuotes The new List<Quote> value to set.
     */
    public void setQuotes(List<Quote> newQuotes) {
        this.quotes = newQuotes;
    }

    /**
     * Sets the reviews attribute.
     * @param newReviews The new List<Review> value to set.
     */
    public void setReviews(List<Review> newReviews) {
        this.reviews = newReviews;
    }

    @Override
    protected void init(Cursor result, boolean close) {
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
            this.authors = new WriterDBManager(context).loadSQLiteAuthors(this.id);

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(DBManager.SQLITE_TAG, e.getMessage());
        }
    }
}
