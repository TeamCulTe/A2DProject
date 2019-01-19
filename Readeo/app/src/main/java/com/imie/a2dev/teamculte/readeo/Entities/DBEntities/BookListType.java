package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema;
import lombok.Getter;
import lombok.Setter;

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
     * Book list type's default constructor.
     */
    public BookListType() {
        super();
    }

    /**
     * Book list type's full filled constructor providing all its attributes values.
     * @param id The id to set.
     * @param name The name to set.
     */
    public BookListType(int id, String name) {
        super(id);

        this.name = name;
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

    @Override
    protected void init(@NonNull Cursor result, boolean close) {
        try {
            if (result.getPosition() == -1) {
                result.moveToNext();
            }

            this.id = result.getInt(result.getColumnIndexOrThrow(BookListTypeDBSchema.ID));
            this.name = result.getString(result.getColumnIndexOrThrow(BookListTypeDBSchema.NAME));

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(DBManager.SQLITE_TAG, e.getMessage());
        }
    }
}
