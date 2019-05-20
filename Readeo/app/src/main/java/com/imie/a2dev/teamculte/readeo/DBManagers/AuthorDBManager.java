package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.NAME;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.JSON_TAG;
import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.SQLITE_TAG;

/**
 * Manager class used to manage the author entities from databases.
 */
public final class AuthorDBManager extends SimpleDBManager {
    /**
     * AuthorDBManager's constructor.
     * @param context The associated context.
     */
    public AuthorDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
        this.baseUrl = APIManager.API_URL + APIManager.AUTHORS;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull Author entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(NAME, entity.getName());
            this.database.insertOrThrow(this.table, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull Author entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(NAME, entity.getName());
            data.put(UPDATE, new Date().toString());

            return this.database.update(this.table, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }
    
    /**
     * From an id, returns the associated java entity.
     * @param id The id of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public Author loadSQLite(int id) {
        Cursor result = this.loadCursorSQLite(id);

        if (result == null || result.getCount() == 0) {
            return null;
        }

        return new Author(result);
    }

    /**
     * Queries all the authors from the database.
     * @return The list of authors.
     */
    public List<Author> queryAllSQLite() {
        List<Author> authors = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    authors.add(new Author(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }

        return authors;
    }

    /**
     * From the API, query the list of all authors from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(this.baseUrl + APIManager.READ);
    }

    /**
     * Creates a author entity in MySQL database.
     * @param author The author to create.
     */
    public void createMySQL(final Author author) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        if (author.getId() != 0) {
            param.put(ID, String.valueOf(author.getId()));
        }

        param.put(NAME, author.getName());

        super.requestString(Request.Method.POST, url, response -> {
            Pattern pattern = Pattern.compile("^\\d.$");
            
            if (pattern.matcher(response).find()) {
                author.setId(Integer.valueOf(response));
            }
            
            HTTPRequestQueueSingleton.getInstance(this.getContext()).finishRequest(this.getClass().getName());
        }, param);
        
        this.waitForResponse();
    }

    /**
     * Loads an author from MySQL database.
     * @param idAuthor The id of the author.
     * @return The loaded author.
     */
    public Author loadMySQL(int idAuthor) {
        final Author author = new Author();
        String url = this.baseUrl + APIManager.READ + ID + "=" + idAuthor;

        super.requestJsonArray(Request.Method.GET, url, response -> {
            try {
                author.init(response.getJSONObject(0));
            } catch (JSONException e) {
                Log.e(JSON_TAG, e.getMessage());
            } finally {
                HTTPRequestQueueSingleton.getInstance(this.getContext()).finishRequest(this.getClass().getName());
            }
        });

        this.waitForResponse();

        return (author.isEmpty()) ? null : author;
    }

    @Override
    public void createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(NAME, entity.getString(NAME));
            this.database.insertOrThrow(this.table, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }

    @Override
    public boolean updateSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{entity.getString(ID)};

            data.put(NAME, entity.getString(NAME));
            data.put(UPDATE, new Date().toString());

            return this.database.update(this.table, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }
}
