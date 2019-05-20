package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import org.junit.After;
import org.junit.Before;

/**
 * Abstract class extended by all database test classes.
 */
public abstract class CommonDBManagerTest {
    /**
     * Defines the test database name.
     */
    public static final String TEST_DB = "readeo.test.db";

    /**
     * Defines the default number of entity in each table.
     */
    protected final int ENTITY_NB = 5;

    /**
     * Defines the start index for MySQL import test.
     */
    protected final int TEST_START = 0;

    /**
     * Defines the end index for MySQL import test.
     */
    protected final int TEST_END = 1;

    /**
     * Defines if the test entity tested MySQL database in order to delete all test entities in tearDown and avoid
     * errors.
     */
    protected boolean testedMySQL = false;

    /**
     * Stores the context used to manage database.
     */
    protected Context context = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() throws Exception {
        DBManager.setDbFileName(TEST_DB);
    }

    @After
    public void tearDown() throws Exception {
    }
}
