package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Final class representing a book list type from the application.
 */
@Getter
@Setter
public final class BookListType extends DBEntity {
    /**
     * Stores the book list type name.
     */
    private String name;

    /**
     * Stores the book list type image.
     */
    private String image;

    /**
     * Book list type's default constructor.
     */
    public BookListType() {
        super();
    }

    /**
     * Book list type's nearly full filled constructor.
     * @param name The name to set.
     * @param image The image to set.
     */
    public BookListType(String name, String image) {
        super();

        this.name = name;
        this.image = image;
    }

    /**
     * Book list type's full filled constructor providing all its attributes values.
     * @param id The id to set.
     * @param name The name to set.
     * @param image The image to set.
     */
    public BookListType(int id, String name, String image) {
        super(id);

        this.name = name;
        this.image = image;
    }

    /**
     * Book list type's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     */
    public BookListType(Cursor result) {
        this.init(result, true);
    }

    /**
     * Book list type's full filled constructor providing all its attributes values from the result of a database query,
     * closes the cursor if close is true.
     * @param result The result of the query.
     * @param close Defines if the cursor should be closed or not.
     */
    public BookListType(Cursor result, boolean close) {
        this.init(result, false);
    }

    /**
     * BookListType's full filled constructor providing all its attributes values from a ContentValues object.
     * @param contentValues The ContentValues object used to initialize the entity.
     */
    public BookListType(ContentValues contentValues) {
        this.init(contentValues);
    }

    /**
     * Initializes the book list type from a ContentValues object.
     * @param contentValues The ContentValues object.
     */
    public void init(@NonNull ContentValues contentValues) {
        this.id = contentValues.getAsInteger(BookListTypeDBSchema.ID);
        this.name = contentValues.getAsString(BookListTypeDBSchema.NAME);
        this.image = contentValues.getAsString(BookListTypeDBSchema.IMAGE);
    }

    /**
     * Initializes the book list type from a JSON response object.
     * @param object The JSON response from the API.
     */
    public void init(@NonNull JSONObject object) {
        try {
            this.id = object.getInt(BookListTypeDBSchema.ID);
            this.name = object.getString(BookListTypeDBSchema.NAME);
            this.image = object.getString(BookListTypeDBSchema.IMAGE);
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

            this.id = result.getInt(result.getColumnIndexOrThrow(BookListTypeDBSchema.ID));
            this.name = result.getString(result.getColumnIndexOrThrow(BookListTypeDBSchema.NAME));
            this.image = result.getString(result.getColumnIndexOrThrow(BookListTypeDBSchema.IMAGE));

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            this.logError("init", e);
        }
    }
}
