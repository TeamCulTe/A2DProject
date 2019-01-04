package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.DBManagers.AuthorDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;

/**
 * Final class representing an author from the application.
 */
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
     * Gets the name attribute.
     * @return The String value of name attribute.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name attribute.
     * @param newName The new String value to set.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    protected void init(Cursor result, boolean close) {
        try {
            if (result.isFirst()) {
                result.moveToNext();
            }

            this.id = result.getInt(result.getColumnIndexOrThrow(AuthorDBManager.ID));
            this.name = result.getString(result.getColumnIndexOrThrow(AuthorDBManager.NAME));

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(DBManager.SQLITE_TAG, e.getMessage());
        }
    }
}
