package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema;
import lombok.Getter;
import lombok.Setter;

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
            Log.e(DBManager.SQLITE_TAG, e.getMessage());
        }
    }
}
