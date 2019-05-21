package com.imie.a2dev.teamculte.readeo.providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.support.test.InstrumentationRegistry;
import com.imie.a2dev.teamculte.readeo.DBManagers.CommonDBManagerTest;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import org.junit.After;
import org.junit.Before;

public abstract class ReadeoContentProviderTest {
    /**
     * Resolver used for tests.
     */
    protected ContentResolver contentResolver;
    
    @Before
    public void setUp() {
        DBManager.setDbFileName(CommonDBManagerTest.TEST_DB);
        
        this.contentResolver = InstrumentationRegistry.getTargetContext().getContentResolver();
    }
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    public abstract void testQuery();

    public abstract void testInsert();

    public abstract void testDelete();

    public abstract void testUpdate();
    
    protected abstract ContentValues generateTestItem();
}
