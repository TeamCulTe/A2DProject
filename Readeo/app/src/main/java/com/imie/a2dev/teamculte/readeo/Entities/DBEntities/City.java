package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CityDBSchema;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.JSON_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.SQLITE_TAG;

/**
 * Final class representing a city from the application.
 */
@Getter
@Setter
public final class City extends DBEntity {
    /**
     * Stores the city name.
     */
    private String name;

    /**
     * City's default constructor.
     */
    public City() {
        super();
    }

    /**
     * City's nearly full filled constructor.
     * @param name The name to set.
     */
    public City(String name) {
        super();

        this.name = name;
    }

    /**
     * City's full filled constructor providing all its attributes values.
     * @param id The id to set.
     * @param name The name to set.
     */
    public City(int id, String name) {
        super(id);

        this.name = name;
    }

    /**
     * City's full filled constructor providing all its attributes values from the result of a database query.
     * @param result The result of the query.
     */
    public City(Cursor result) {
        this.init(result, true);
    }

    /**
     * City's full filled constructor providing all its attributes values from the result of a database query,
     * closes the cursor if close is true.
     * @param result The result of the query.
     * @param close Defines if the cursor should be closed or not.
     */
    public City(Cursor result, boolean close) {
        this.init(result, false);
    }

    /**
     * City's full filled constructor providing all its attributes values from a ContentValues object.
     * @param contentValues The ContentValues object used to initialize the entity.
     */
    public City(ContentValues contentValues) {
        this.init(contentValues);
    }

    /**
     * Initializes the city from a ContentValues object.
     * @param contentValues The ContentValues object.
     */
    public void init(@NonNull ContentValues contentValues) {
        this.id = contentValues.getAsInteger(CityDBSchema.ID);
        this.name = contentValues.getAsString(CityDBSchema.NAME);
    }

    /**
     * Initializes the city from a JSON response object.
     * @param object The JSON response from the API.
     */
    public void init(@NonNull JSONObject object) {
        try {
            this.id = object.getInt(CityDBSchema.ID);
            this.name = object.getString(CityDBSchema.NAME);
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

            this.id = result.getInt(result.getColumnIndexOrThrow(CityDBSchema.ID));
            this.name = result.getString(result.getColumnIndexOrThrow(CityDBSchema.NAME));

            if (close) {
                result.close();
            }
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
