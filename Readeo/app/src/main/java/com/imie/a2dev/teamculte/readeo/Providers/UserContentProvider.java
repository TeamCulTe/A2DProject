package com.imie.a2dev.teamculte.readeo.Providers;

import android.net.Uri;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.TABLE;

/**
 * Content provider used to access to the users.
 */
public final class UserContentProvider extends ReadeoContentProvider {
    /**
     * Stores the content provider's uri.
     */
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);

    @Override
    public boolean onCreate() {
        this.manager = new UserDBManager(this.getContext());
        this.table = TABLE;

        return true;
    }
}
