package com.imie.a2dev.teamculte.readeo.Providers;

import android.net.Uri;

import com.imie.a2dev.teamculte.readeo.DBManagers.CategoryDBManager;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema.TABLE;

/**
 * Content provider used to access to the categories.
 */
public final class CategoryContentProvider extends ReadeoContentProvider {
    /**
     * Stores the content provider's uri.
     */
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);

    @Override
    public boolean onCreate() {
        this.manager = new CategoryDBManager(this.getContext());
        this.table = TABLE;

        return true;
    }
}
