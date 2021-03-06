package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema;

import lombok.Getter;
import lombok.Setter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Final class representing an author from the application.
 */
@Getter
@Setter
public final class Author extends DBEntity {
    /**
     * Stores the author name.
     */
    private String name;

    /**
     * Author's default constructor.
     */
    public Author() {
        super();
    }

    /**
     * Author's nearly full filled constructor providing  its name.
     * @param name The name to set.
     */
    public Author(String name) {
        super();

        this.name = name;
    }

    /**
     * Author's full filled constructor providing all its attributes values.
     * @param id The id to set.
     * @param name The name to set.
     */
    public Author(int id, String name) {
        super(id);

        this.name = name;
    }

    /**
     * Author's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     */
    public Author(Cursor result) {
        this.init(result, true);
    }

    /**
     * Author's full filled constructor providing all its attributes values from the result of a database query
     * closes the cursor if close is true.
     * @param result The result of the query.
     * @param close Defines if the cursor should be closed or not.
     */
    public Author(Cursor result, boolean close) {
        this.init(result, close);
    }

    /**
     * Author's full filled constructor providing all its attributes values from a ContentValues object.
     * @param contentValues The ContentValues object used to initialize the entity.
     */
    public Author(ContentValues contentValues) {
        this.init(contentValues);
    }

    /**
     * Initializes the author from a ContentValues object.
     * @param contentValues The ContentValues object.
     */
    public void init(@NonNull ContentValues contentValues) {
        this.id = contentValues.getAsInteger(AuthorDBSchema.ID);
        this.name = contentValues.getAsString(AuthorDBSchema.NAME);
    }

    /**
     * Initializes the author from a JSON response object.
     * @param object The JSON response from the API.
     */
    public void init(@NonNull JSONObject object) {
        try {
            this.id = object.getInt(AuthorDBSchema.ID);
            this.name = object.getString(AuthorDBSchema.NAME);
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

            this.id = result.getInt(result.getColumnIndexOrThrow(AuthorDBSchema.ID));
            this.name = result.getString(result.getColumnIndexOrThrow(AuthorDBSchema.NAME));
        } catch (SQLiteException e) {
            this.logError("init", e);
        } finally {
            if (close) {
                result.close();
            }
        }
    }
}
