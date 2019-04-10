package com.imie.a2dev.teamculte.readeo.DBManagers;

import static com.imie.a2dev.teamculte.readeo.DBManagers.DBManager.MYSQL_TEST_ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthorDBManagerTest extends CommonDBManagerTest {
    /**
     * Stores the default name given for tests.
     */
    private final String TEST_NAME = "testName";

    /**
     * Stores the associated manager used to interact with the database.
     */
    private AuthorDBManager manager = new AuthorDBManager(this.context);

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() {
        this.context.deleteDatabase(TEST_DB);

        if (this.testedMySQL) {
            this.deleteMySQLTestEntities();

            this.testedMySQL = false;
        }
    }

    @Test
    public void testEntityCreateSQLite() {
        Author toCreate = new Author(MYSQL_TEST_ID, TEST_NAME);

        assertTrue(this.manager.createSQLite(toCreate));
        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());
        assertEquals(TEST_NAME, this.manager.loadSQLite(MYSQL_TEST_ID).getName());
    }

    @Test
    public void testJSONCreateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, MYSQL_TEST_ID);
        jsonObject.put(NAME, TEST_NAME);

        this.manager.createSQLite(jsonObject);

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());
        assertEquals(TEST_NAME, this.manager.loadSQLite(MYSQL_TEST_ID).getName());
    }

    @Test
    public void testEntityUpdateSQLite() {
        Author updated = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(updated);

        updated.setName(TEST_NAME);
        this.manager.updateSQLite(updated);

        assertEquals(TEST_NAME, this.manager.loadSQLite(ENTITY_NB).getName());
    }

    @Test
    public void testJSONUpdateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, ENTITY_NB);
        jsonObject.put(NAME, TEST_NAME);

        this.manager.updateSQLite(jsonObject);

        assertEquals(TEST_NAME, this.manager.loadSQLite(ENTITY_NB).getName());
    }

    @Test
    public void testLoadSQLite() {
        Author loaded = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(loaded);
    }

    @Test
    public void testQueryAllSQLite() {
        assertEquals(ENTITY_NB, this.manager.queryAllSQLite().size());
    }

    @Test
    public void testImportFromMySQL() {
        Author author = this.initTestEntityMySQL();

        this.manager.importFromMySQL(APIManager.API_URL + APIManager.AUTHORS + APIManager.READ + APIManager.START +
                "=" + TEST_START + "&" + APIManager.END + "=" + TEST_END);

        this.manager.waitForResponse();

        Author imported = this.manager.loadSQLite(author.getId());

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());
        assertNotNull(imported);
        assertEquals(author.getId(), imported.getId());
        assertEquals(author.getName(), imported.getName());
    }

    @Test
    public void testCreateMySQL() {
        Author created = this.initTestEntityMySQL();

        assertNotNull(created);
        assertEquals(MYSQL_TEST_ID, created.getId());
        assertEquals(TEST_NAME, created.getName());
    }

    @Test
    public void testLoadMySQL() {
        Author created = this.initTestEntityMySQL();
        Author loaded = this.manager.loadMySQL(created.getId());

        assertNotNull(created);
        assertNotNull(loaded);
        assertEquals(created.getId(), loaded.getId());
        assertEquals(created.getName(), loaded.getName());
    }


    @Test
    public void testGetFieldSQLite() {
        Author author = this.manager.loadSQLite(ENTITY_NB);
        String loadedName = this.manager.getFieldSQLite(NAME, ENTITY_NB);

        assertNotNull(author);
        assertEquals(author.getName(), loadedName);
    }

    @Test
    public void testDeleteSQLite() {
        this.manager.deleteSQLite(ENTITY_NB);

        assertEquals(ENTITY_NB - 1, this.manager.countSQLite());
    }

    @Test
    public void testCountSQLite() {
        assertEquals(ENTITY_NB, this.manager.countSQLite());
    }

    @Test
    public void testImportPaginatedFromMySQL() {
        // TODO: See if really needed.
    }

    @Test
    public void testImportMySQLDatabase() {
        // TODO: See if really needed and how to test it.
    }

    @Test
    public void testImportMySQLTable() {
        // TODO: See how to test it (latency).
    }

    @Test
    public void testGetUpdateFromMySQL() {
        // TODO: See how to test it (latency).
    }

    /**
     * Creates a test author into the database for testing.
     * @param id The id of the author to create.
     * @param name The name of the author to create.
     * @return The created author.
     */
    protected Author initTestEntityMySQL(int id, String name) {
        this.testedMySQL = true;

        this.manager.createMySQL(new Author(id, name));
        this.manager.waitForResponse();

        return this.manager.loadMySQL(id);
    }

    /**
     * Creates a test author according to the constant defined.
     * @return The created author.
     */
    protected Author initTestEntityMySQL() {
        return this.initTestEntityMySQL(MYSQL_TEST_ID, TEST_NAME);
    }

    /**
     * Deletes all the test entities from MySQL database.
     */
    protected void deleteMySQLTestEntities() {
        this.manager.deleteMySQLTestEntities();
    }
}