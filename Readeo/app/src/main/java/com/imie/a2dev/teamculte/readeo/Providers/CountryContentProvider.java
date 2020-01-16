package com.imie.a2dev.teamculte.readeo.Providers;

import android.net.Uri;

import com.imie.a2dev.teamculte.readeo.DBManagers.CountryDBManager;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CountryDBSchema.TABLE;

/**
 * Content provider used to access to the countries.
 */
public final class CountryContentProvider extends ReadeoContentProvider {
    /**
     * Stores the content provider's uri.
     */
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);

    @Override
    public boolean onCreate() {
        this.manager = new CountryDBManager(this.getContext());
        this.table = TABLE;

        return true;
    }
}
