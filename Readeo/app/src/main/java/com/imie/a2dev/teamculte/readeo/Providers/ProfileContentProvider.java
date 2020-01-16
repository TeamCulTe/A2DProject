package com.imie.a2dev.teamculte.readeo.Providers;

import android.net.Uri;

import com.imie.a2dev.teamculte.readeo.DBManagers.ProfileDBManager;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.TABLE;

/**
 * Content provider used to access to the profiles.
 */
public final class ProfileContentProvider extends ReadeoContentProvider {
    /**
     * Stores the content provider's uri.
     */
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);

    @Override
    public boolean onCreate() {
        this.manager = new ProfileDBManager(this.getContext());
        this.table = TABLE;

        return true;
    }
}
