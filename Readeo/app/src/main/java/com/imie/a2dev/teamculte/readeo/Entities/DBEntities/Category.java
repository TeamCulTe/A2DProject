package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.JSON_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.SQLITE_TAG;

/**
 * Final class representing a category from the application.
 */
@Getter
@Setter
public final class Category extends DBEntity {
    /**
     * Stores the category name.
     */
    private String name;

    /**
     * Category's default constructor.
     */
    public Category() {
        super();
    }

    /**
     * Category's nearly full filled constructor.
     * @param name The name to set.
     */
    public Category(String name) {
        super();

        this.name = name;
    }

    /**
     * Category's full filled constructor providing all its attributes values.
     * @param id The id to set.
     * @param name The name to set.
     */
    public Category(int id, String name) {
        super(id);

        this.name = name;
    }

    /**
     * Category's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     */
    public Category(Cursor result) {
        this.init(result, true);
    }

    /**
     * Category's full filled constructor providing all its attributes values from the result of a database query,
     * closes the cursor if close is true.
     * @param result The result of the query.
     * @param close Defines if the cursor should be closed or not.
     */
    public Category(Cursor result, boolean close) {
        this.init(result, false);
    }

    /**
     * Category's full filled constructor providing all its attributes values from a ContentValues object.
     * @param contentValues The ContentValues object used to initialize the entity.
     */
    public Category(ContentValues contentValues) {
        this.init(contentValues);
    }

    /**
     * Initializes the category from a ContentValues object.
     * @param contentValues The ContentValues object.
     */
    public void init(@NonNull ContentValues contentValues) {
        this.id = contentValues.getAsInteger(CategoryDBSchema.ID);
        this.name = contentValues.getAsString(CategoryDBSchema.NAME);
    }
    
    /**
     * Initializes the category from a JSON response object.
     * @param object The JSON response from the API.
     */
    public void init(@NonNull JSONObject object) {
        try {
            this.id = object.getInt(CategoryDBSchema.ID);
            this.name = object.getString(CategoryDBSchema.NAME);
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

            this.id = result.getInt(result.getColumnIndexOrThrow(CategoryDBSchema.ID));
            this.name = result.getString(result.getColumnIndexOrThrow(CategoryDBSchema.NAME));

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
