package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Final class representing a review written by an user for a specific book.
 */
@Getter
@Setter
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
     * Review's full filled constructor providing all its attributes values from a json object.
     * @param result The json object.
     */
    public Review(JSONObject result) {
        this.init(result);
    }

    /**
     * Initializes the review from a JSON response object.
     * @param object The JSON response from the API.
     */
    public void init(@NonNull JSONObject object) {
        try {
            this.id = object.getInt(ReviewDBSchema.BOOK);
            this.author = new UserDBManager(App.getAppContext()).getFieldSQLite(UserDBSchema.PSEUDO,
                    object.getInt(ReviewDBSchema.USER));
            this.review = object.getString(ReviewDBSchema.REVIEW);
        } catch (JSONException e) {
            Log.e(DBManager.JSON_TAG, e.getMessage());
        }
    }

    @Override
    protected void init(@NonNull Cursor result, boolean close) {
        try {
            if (result.getPosition() == -1) {
                result.moveToNext();
            }

            this.id = result.getInt(result.getColumnIndexOrThrow(ReviewDBSchema.BOOK));
            this.author = new UserDBManager(App.getAppContext()).getFieldSQLite(UserDBSchema.PSEUDO,
                    result.getInt(result.getColumnIndexOrThrow(ReviewDBSchema.USER)));
            this.review = result.getString(result.getColumnIndexOrThrow(ReviewDBSchema.REVIEW));
            this.shared = true;

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(DBManager.SQLITE_TAG, e.getMessage());
        }
    }
}
