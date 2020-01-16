package com.imie.a2dev.teamculte.readeo.Providers;

import android.net.Uri;

import com.imie.a2dev.teamculte.readeo.DBManagers.CityDBManager;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CityDBSchema.TABLE;

/**
 * Content provider used to access to the cities.
 */
public final class CityContentProvider extends ReadeoContentProvider {
    /**
     * Stores the content provider's uri.
     */
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);

    @Override
    public boolean onCreate() {
        this.manager = new CityDBManager(this.getContext());
        this.table = TABLE;

        return true;
    }
}
