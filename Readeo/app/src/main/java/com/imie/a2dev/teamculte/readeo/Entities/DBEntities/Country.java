package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CountryDBSchema;
import lombok.Getter;
import lombok.Setter;

/**
 * Final class representing a country from the application.
 */
@Getter
@Setter
public final class Country extends DBEntity {
    /**
     * Stores the country name.
     */
    private String name;

    /**
     * Country's default constructor.
     */
    public Country() {
        super();
    }

    /**
     * Country's full filled constructor providing all its attributes values.
     * @param id The id to set.
     * @param name The name to set.
     */
    public Country(int id, String name) {
        super(id);

        this.name = name;
    }

    /**
     * Country's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     */
    public Country(Cursor result) {
        this.init(result, true);
    }

    /**
     * Country's full filled constructor providing all its attributes values from the result of a database query,
     * closes the cursor if close is true.
     * @param result The result of the query.
     * @param close Defines if the cursor should be closed or not.
     */
    public Country(Cursor result, boolean close) {
        this.init(result, false);
    }

    @Override
    protected void init(@NonNull Cursor result, boolean close) {
        try {
            if (result.getPosition() == -1) {
                result.moveToNext();
            }

            this.id = result.getInt(result.getColumnIndexOrThrow(CountryDBSchema.ID));
            this.name = result.getString(result.getColumnIndexOrThrow(CountryDBSchema.NAME));

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(DBManager.SQLITE_TAG, e.getMessage());
        }
    }
}
