package com.imie.a2dev.teamculte.readeo.providers;

import android.content.ContentValues;
import android.database.Cursor;
import com.imie.a2dev.teamculte.readeo.Providers.AuthorContentProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.NAME;

public final class AuthorContentProviderTest extends ReadeoContentProviderTest {
    /**
     * Defines the default test id.
     */
    private final static int TEST_ID = 6;

    /**
     * Defines the default test name.
     */
    private final static String TEST_NAME = "TestNameAuthor";
    
    @Override
    @Test
    public void testQuery() {
        String[] projection = new String[]{ID, NAME};
        String selection = ID + " = ?";
        String[] selectionArgs = new String[]{"1"};
        
        Cursor cursor = this.contentResolver.query(AuthorContentProvider.URI, projection, selection, selectionArgs, null);
        
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
    }

    @Override
    public void testInsert() {

    }

    @Override
    public void testDelete() {

    }

    @Override
    public void testUpdate() {

    }

    @Override
    protected ContentValues generateTestItem() {
        ContentValues contentValues = new ContentValues();
        
        contentValues.put(ID, TEST_ID);
        contentValues.put(NAME, TEST_NAME);
        
        return contentValues;
    }
}
