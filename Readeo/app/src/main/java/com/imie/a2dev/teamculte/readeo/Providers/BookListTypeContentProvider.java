package com.imie.a2dev.teamculte.readeo.Providers;

import android.net.Uri;

import com.imie.a2dev.teamculte.readeo.DBManagers.BookListTypeDBManager;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema.TABLE;

/**
 * Content provider used to access to the book list types.
 */
public final class BookListTypeContentProvider extends ReadeoContentProvider {
    /**
     * Stores the content provider's uri.
     */
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);

    @Override
    public boolean onCreate() {
        this.manager = new BookListTypeDBManager(this.getContext());
        this.table = TABLE;

        return true;
    }
}
