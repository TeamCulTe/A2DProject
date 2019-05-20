package com.imie.a2dev.teamculte.readeo.Providers;

import android.net.Uri;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookDBManager;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.TABLE;

/**
 * Content provider used to access to the books.
 */
public final class BookContentProvider extends ReadeoContentProvider {
    /**
     * Stores the content provider's uri.
     */
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);

    @Override
    public boolean onCreate() {
        this.manager = new BookDBManager(this.getContext());
        this.table = TABLE;

        return true;
    }
}
