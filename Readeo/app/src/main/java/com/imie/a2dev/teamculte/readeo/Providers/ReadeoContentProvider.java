package com.imie.a2dev.teamculte.readeo.Providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.imie.a2dev.teamculte.readeo.DBManagers.AuthorDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;

/**
 * Project's content provider.
 */
public abstract class ReadeoContentProvider extends ContentProvider {
    /**
     * Stores the content provider's authority.
     */
    public static final String AUTHORITY = "com.imie.a2dev.teamculte.readeo.providers";

    /**
     * Stores the content providers type prefix.
     */
    public static final String TYPE_PREFIX = "vnd.android.cursor.item/";

    /**
     * Stores the associated db manager.
     */
    protected DBManager manager;

    /**
     * Stores the associated table. 
     */
    protected String table;

    @Override
    public boolean onCreate() {
        return (this.manager != null && this.table != null);
    }
    
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        if (this.getContext() != null && this.manager != null) {
            final Cursor cursor = this.manager.getDatabase().query(this.table, projection, selection, selectionArgs,
                    null, null, sortOrder);

            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return TYPE_PREFIX + AUTHORITY + "." + this.table;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (this.getContext() != null && this.manager != null) {
            AuthorDBManager manager = new AuthorDBManager(this.getContext());
            long id = manager.getDatabase().insertOrThrow(this.table, null, values);

            if (id != 0) {
                this.getContext().getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, id);
            }
        }

        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (this.getContext() != null && this.manager != null) {
            int count = this.manager.getDatabase().delete(this.table, selection, selectionArgs);

            this.getContext().getContentResolver().notifyChange(uri, null);

            return count;
        }

        throw new IllegalArgumentException("Failed to delete row into " + uri);
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        if (this.getContext() != null && this.manager != null) {
            int count = this.manager.getDatabase().update(this.table, values, selection, selectionArgs);

            this.getContext().getContentResolver().notifyChange(uri, null);

            return count;
        }
        
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }
}
