package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.TITLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.CATEGORY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.COVER;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.SUMMARY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.DATE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.DEFAULT_FORMAT;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;

/**
 * Manager class used to manage the book entities from databases.
 */
public final class BookDBManager extends SimpleDBManager {
    /**
     * BookDBManager's constructor.
     * @param context The associated context.
     */
    public BookDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
        this.baseUrl = APIManager.API_URL + APIManager.BOOKS;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull Book entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(TITLE, entity.getTitle());
            data.put(CATEGORY, entity.getCategory().getId());
            data.put(COVER, entity.getCover());
            data.put(SUMMARY, entity.getSummary());
            data.put(DATE, entity.getDatePublished());

            this.database.insertOrThrow(this.table, null, data);
            this.database.setTransactionSuccessful();

            return true;
        } catch (SQLiteException e) {
            this.logError("createSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull Book entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(TITLE, entity.getTitle());
            data.put(CATEGORY, entity.getCategory().getId());
            data.put(COVER, entity.getCover());
            data.put(SUMMARY, entity.getSummary());
            data.put(DATE, entity.getDatePublished());
            data.put(UPDATE, new DateTime().toString(DEFAULT_FORMAT));
            
            boolean success = this.database.update(this.table, data, whereClause, whereArgs) != 0;

            this.database.setTransactionSuccessful();
            
            return success;
        } catch (SQLiteException e) {
            this.logError("updateSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }

    /**
     * From an id, returns the associated java entity.
     * @param id The id of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public Book loadSQLite(int id) {
        Cursor result = this.loadCursorSQLite(id);

        if (result == null || result.getCount() == 0) {
            return null;
        }

        return new Book(result);
    }

    /**
     * From a string and a field, returns the associated java books where the string matches in the field values.
     * @param field The field to filter on.
     * @param filter The string that should match.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadFieldFilteredSQLite(String field, String filter) {
        List<Book> books = new ArrayList<>();

        try {
            String[] selectArgs = {filter};
            String query = String.format(this.SIMPLE_QUERY_ALL_LIKE_START, this.table, field);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result, false));
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("loadFieldFilteredSQLite", e);
        }

        return books;
    }

    /**
     * From a string and a field, returns the associated java books where the string matches in the field values 
     * filtered.
     * @param field The field to filter on.
     * @param filter The string that should match.
     * @param limit The limit to the query.
     * @param offset The offset of the query.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadFieldFilteredPaginatedSQLite(String field, String filter, int limit, int offset) {
        List<Book> books = new ArrayList<>();

        try {
            String[] selectArgs = {filter};
            String query = String.format(this.SIMPLE_QUERY_ALL_LIKE_START_PAGINATED, this.table, field, limit, offset);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result, false));
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("loadFieldFilteredSQLite", e);
        }

        return books;
    }

    /**
     * From a string, returns the associated java books where the string matches in the category values.
     * @param filter The string that should match.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadCategoryNameFilteredSQLite(String filter) {
        List<Book> books = new ArrayList<>();

        try {
            String[] selectArgs = {filter};
            String query = String.format("SELECT %s.* FROM %s INNER JOIN %s ON %s.%s = %s.%s WHERE %s LIKE ?||'%%'",
                                         this.table,
                                         this.table,
                                         CategoryDBSchema.TABLE,
                                         this.table,
                                         CATEGORY,
                                         CategoryDBSchema.TABLE,
                                         CategoryDBSchema.ID,
                                         CategoryDBSchema.NAME);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result, false));
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("loadCategoryNameFilteredSQLite", e);
        }

        return books;
    }

    /**
     * From a string, returns the associated java books where the string matches in the category values paginated.
     * @param filter The string that should match.
     * @param limit The limit to the query.
     * @param offset The offset of the query.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadCategoryNameFilteredPaginatedSQLite(String filter, int limit, int offset) {
        List<Book> books = new ArrayList<>();

        try {
            String[] selectArgs = {filter};
            String query = String.format("SELECT %s.* FROM %s INNER JOIN %s ON %s.%s = %s.%s WHERE %s LIKE ?||'%%' " +
                                         "LIMIT %d OFFSET %d",
                                         this.table,
                                         this.table,
                                         CategoryDBSchema.TABLE,
                                         this.table,
                                         CATEGORY,
                                         CategoryDBSchema.TABLE,
                                         CategoryDBSchema.ID,
                                         CategoryDBSchema.NAME,
                                         limit,
                                         offset);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result, false));
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("loadCategoryNameFilteredPaginatedSQLite", e);
        }

        return books;
    }

    /**
     * From a string, returns the associated java books where the string matches in the book writers.
     * @param filter The string that should match.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadAuthorNameFilteredSQLite(String filter) {
        List<Book> books = new ArrayList<>();

        try {
            String[] selectArgs = {filter};
            String query = String.format("SELECT %s.* FROM %s INNER JOIN %s ON %s.%s = %s.%s " +
                                         "INNER JOIN %s ON %s.%s = %s.%s WHERE %s.%s LIKE ?||'%%'",
                                         this.table,
                                         this.table,
                                         WriterDBSchema.TABLE,
                                         this.table,
                                         ID,
                                         WriterDBSchema.TABLE,
                                         WriterDBSchema.BOOK,
                                         AuthorDBSchema.TABLE,
                                         AuthorDBSchema.TABLE,
                                         AuthorDBSchema.ID,
                                         WriterDBSchema.TABLE,
                                         WriterDBSchema.AUTHOR,
                                         AuthorDBSchema.TABLE,
                                         AuthorDBSchema.NAME);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result, false));
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("loadAuthorNameFilteredSQLite", e);
        }

        return books;
    }

    /**
     * From a string, returns the associated java books where the string matches in the book writers paginated.
     * @param filter The string that should match.
     * @param limit The limit to the query.
     * @param offset The offset of the query.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadAuthorNameFilteredPaginatedSQLite(String filter, int limit, int offset) {
        List<Book> books = new ArrayList<>();

        try {
            String[] selectArgs = {filter};
            String query = String.format("SELECT %s.* FROM %s INNER JOIN %s ON %s.%s = %s.%s " +
                                         "INNER JOIN %s ON %s.%s = %s.%s WHERE %s.%s LIKE ?||'%%' LIMIT %d OFFSET %s",
                                         this.table,
                                         this.table,
                                         WriterDBSchema.TABLE,
                                         this.table,
                                         ID,
                                         WriterDBSchema.TABLE,
                                         WriterDBSchema.BOOK,
                                         AuthorDBSchema.TABLE,
                                         AuthorDBSchema.TABLE,
                                         AuthorDBSchema.ID,
                                         WriterDBSchema.TABLE,
                                         WriterDBSchema.AUTHOR,
                                         AuthorDBSchema.TABLE,
                                         AuthorDBSchema.NAME,
                                         limit,
                                         offset);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result, false));
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("loadAuthorNameFilteredSQLite", e);
        }

        return books;
    }

    /**
     * From a string filter and a value, returns the associated java books where the value matches in the filter values.
     * Uses the common filter fields (Author, Category, or inner Book fields).
     * @param filter The filter to filter on (matching to database inner or joined fields).
     * @param value The string value that should match.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadFilteredSQLite(String filter, String value) {
        switch (filter) {
            case CategoryDBSchema.NAME:
                return this.loadCategoryNameFilteredSQLite(value);
            case AuthorDBSchema.NAME:
                return this.loadAuthorNameFilteredSQLite(value);
            default:
                return this.loadFieldFilteredSQLite(filter, value);
        }
    }

    /**
     * From a string filter and a value, returns the associated java books where the value matches in the filter values.
     * Uses the common filter fields (Author, Category, or inner Book fields) with pagination.
     * @param filter The filter to filter on (matching to database inner or joined fields).
     * @param value The string value that should match.
     * @param limit The limit to the query.
     * @param offset The offset of the query.
     * @return The loaded entities if exists else null.
     */
    public List<Book> loadFilteredPaginatedSQLite(String filter, String value, int limit, int offset) {
        switch (filter) {
            case CategoryDBSchema.NAME:
                return this.loadCategoryNameFilteredPaginatedSQLite(value, limit, offset);
            case AuthorDBSchema.NAME:
                return this.loadAuthorNameFilteredPaginatedSQLite(value, limit, offset);
            default:
                return this.loadFieldFilteredPaginatedSQLite(filter, value, limit, offset);
        }
    }

    /**
     * From a category id, returns the associated list of books.
     * @param idCategory The id of the category to load books from the database.
     * @return The list of books.
     */
    public List<Book> loadCategorySQLite(int idCategory) {
        List<Book> books = new ArrayList<>();

        try {
            String[] selectArgs = {String.valueOf(idCategory)};
            String query = String.format(this.SIMPLE_QUERY_ALL, this.table, CATEGORY);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                books.add(new Book(result, false));
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("loadCategorySQLite", e);
        }

        return books;
    }

    /**
     * Queries all the books from the database.
     * @return The list of books.
     */
    public List<Book> queryAllSQLite() {
        //TODO : Make the query better to improve loading time...
        List<Book> books = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    books.add(new Book(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("queryAllSQLite", e);
        }

        return books;
    }

    /**
     * Queries all the books from the database paginated.
     * @param limit The limit index.
     * @param offset The offset.
     * @return The list of books.
     */
    public List<Book> queryAllPaginatedSQLite(int limit, int offset) {
        List<Book> books = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL_PAGINATED, this.table, limit, offset),
                                                   null);

            if (result.getCount() > 0) {
                do {
                    books.add(new Book(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("queryAllPaginatedSQLite", e);
        }

        return books;
    }

    /**
     * From the API, query the list of all books from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(baseUrl + APIManager.READ);
    }

    /**
     * Creates a book entity in MySQL database.
     * @param book The book to create.
     */
    public void createMySQL(final Book book) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        if (book.getId() != 0) {
            param.put(ID, String.valueOf(book.getId()));
        }

        param.put(TITLE, book.getTitle());
        param.put(CATEGORY, String.valueOf(book.getCategory().getId()));
        param.put(COVER, book.getCover());
        param.put(SUMMARY, book.getSummary());
        param.put(DATE, String.valueOf(book.getDatePublished()));

        StringRequest request = new StringRequest(Request.Method.POST, url, null, new OnRequestError()) {
            @Override
            protected Map<String, String> getParams() {
                return param;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(BookDBManager.this.getContext())
                                         .finishRequest(BookDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String resp = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Pattern pattern = Pattern.compile("^\\d.$");

                    if (pattern.matcher(resp).find()) {
                        book.setId(Integer.valueOf(resp));
                    }
                } catch (IOException e) {
                    BookDBManager.this.logError("createMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(BookDBManager.this.getContext())
                                             .finishRequest(BookDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();
    }

    /**
     * Loads a book from MySQL database.
     * @param idBook The id of the book.
     * @return The loaded book.
     */
    public Book loadMySQL(int idBook) {
        final Book book = new Book();
        final int idCategory[] = new int[1];

        String url = this.baseUrl + APIManager.READ + ID + "=" + idBook;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null, new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                   HttpHeaderParser.parseCharset(response.headers)));
                    JSONObject object = jsonArray.getJSONObject(0);

                    idCategory[0] = object.getInt(CATEGORY);

                    book.init(object);
                } catch (Exception e) {
                    BookDBManager.this.logError("loadMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(BookDBManager.this.getContext())
                                             .finishRequest(BookDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(BookDBManager.this.getContext())
                                         .finishRequest(BookDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();
        book.setCategory(new CategoryDBManager(this.getContext()).loadMySQL(idCategory[0]));

        return (book.isEmpty()) ? null : book;
    }

    @Override
    public boolean createSQLite(@NonNull JSONObject entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(TITLE, entity.getString(TITLE));
            data.put(CATEGORY, entity.getInt(CATEGORY));
            data.put(COVER, entity.getString(COVER));
            data.put(SUMMARY, entity.getString(SUMMARY));
            data.put(DATE, entity.getInt(DATE));

            this.database.insertOrThrow(this.table, null, data);
            this.database.setTransactionSuccessful();
            
            return true;
        } catch (Exception e) {
            this.logError("createSQLite", e);
            
            return false;
        } finally {
            this.database.endTransaction();
        }
    }

    @Override
    public boolean updateSQLite(@NonNull JSONObject entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{entity.getString(ID)};

            data.put(TITLE, entity.getString(TITLE));
            data.put(CATEGORY, entity.getInt(CATEGORY));
            data.put(COVER, entity.getString(COVER));
            data.put(SUMMARY, entity.getString(SUMMARY));
            data.put(DATE, entity.getInt(DATE));
            data.put(UPDATE, new DateTime().toString(DEFAULT_FORMAT));
            
            boolean success = this.database.update(this.table, data, whereClause, whereArgs) != 0;
            
            this.database.setTransactionSuccessful();

            return success;
        } catch (Exception e) {
            this.logError("updateSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }
}
