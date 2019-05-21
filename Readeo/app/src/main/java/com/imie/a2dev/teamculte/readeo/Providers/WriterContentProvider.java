package com.imie.a2dev.teamculte.readeo.Providers;

import android.net.Uri;
import com.imie.a2dev.teamculte.readeo.DBManagers.WriterDBManager;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema.TABLE;

/**
 * Content provider used to access to the writers.
 */
public final class WriterContentProvider extends ReadeoContentProvider {
    /**
     * Stores the content provider's uri.
     */
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);

    @Override
    public boolean onCreate() {
        this.manager = new WriterDBManager(this.getContext());
        this.table = TABLE;

        return true;
    }
}
