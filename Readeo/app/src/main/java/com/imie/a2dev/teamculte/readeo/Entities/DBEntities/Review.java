package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.ReviewDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import static android.content.ContentValues.TAG;

/**
 * Final class representing a review written by an user for a specific book.
 */
public final class Review extends DBEntity {
    /**
     * Stores the author's pseudo.
     */
    private String author;

    /**
     * Stores the review's text.
     */
    private String review;

    /**
     * Not yet implemented.
     * Defines if the review can be read by the other users or not.
     */
    private boolean shared = false;

    /**
     * Review's default constructor.
     */
    public Review() {
        super();
    }

    /**
     * Review's nearly full filled constructor, providing all attributes values, except for database related ones.
     * @param author The name of the author to set.
     * @param review The review to set.
     * @param shared The value defining if the review is shared or not.
     */
    public Review(String author, String review, boolean shared) {
        super();

        this.author = author;
        this.review = review;
        this.shared = shared;
    }

    /**
     * Review's full filled constructor, providing all attributes values.
     * @param author The name of the author to set.
     * @param review The review to set.
     * @param shared The value defining if the review is shared or not.
     */
    public Review(int id, String review, String author, boolean shared) {
        super(id);

        this.author = author;
        this.review = review;
        this.shared = shared;
    }

    /**
     * Review's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     */
    public Review(Cursor result) {
        this.init(result, true);
    }

    /**
     * Review's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     * @param close Defines if the cursor should be closed or not.
     */
    public Review(Cursor result, boolean close) {
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
     * Gets the review attribute.
     * @return The String value of review attribute.
     */
    public String getReview() {
        return this.review;
    }

    /**
     * Gets the shared attribute.
     * @return The boolean value of the sgit hared attribute.
     */
    public boolean isShared() {
        return this.shared;
    }

    /**
     * Sets the author attribute.
     * @param newAuthor The new User value to set.
     */
    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }

    /**
     * Sets the review attribute.
     * @param newReview The new String value to set.
     */
    public void setReview(String newReview) {
        this.review = newReview;
    }

    /**
     * Sets the shared attribute.
     * @param newShared The new boolean value to set.
     */
    public void setShared(boolean newShared) {
        this.shared = newShared;
    }

    @Override
    protected void init(Cursor result, boolean close) {
        try {
            if (result.isFirst()) {
                result.moveToNext();
            }

            this.id = result.getInt(result.getColumnIndexOrThrow(ReviewDBManager.BOOK));
            this.author = new UserDBManager(App.getAppContext()).SQLiteGetField(UserDBManager.PSEUDO,
                    result.getInt(result.getColumnIndexOrThrow(ReviewDBManager.USER)));
            this.review = result.getString(result.getColumnIndexOrThrow(ReviewDBManager.REVIEW));
            this.shared = true;

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
