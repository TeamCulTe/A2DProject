package com.imie.a2dev.teamculte.readeo.Providers;

import android.net.Uri;

import com.imie.a2dev.teamculte.readeo.DBManagers.ReviewDBManager;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema.TABLE;

/**
 * Content provider used to access to the reviews.
 */
public final class ReviewContentProvider extends ReadeoContentProvider {
    /**
     * Stores the content provider's uri.
     */
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);

    @Override
    public boolean onCreate() {
        this.manager = new ReviewDBManager(this.getContext());
        this.table = TABLE;

        return true;
    }
}
