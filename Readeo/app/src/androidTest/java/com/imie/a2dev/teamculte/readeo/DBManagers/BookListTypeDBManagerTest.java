package com.imie.a2dev.teamculte.readeo.DBManagers;

import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.imie.a2dev.teamculte.readeo.DBManagers.DBManager.MYSQL_TEST_ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema.IMAGE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema.NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public final class BookListTypeDBManagerTest extends CommonDBManagerTest {
    /**
     * Stores the default name given for tests.
     */
    protected final String TEST_NAME = "testName";

    /**
     * Stores the default image given for tests.
     */
    protected final String TEST_IMAGE = "testImage";

    /**
     * Stores the associated manager used to interact with the database.
     */
    private BookListTypeDBManager manager = new BookListTypeDBManager(this.context);

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
        BookListType toCreate = new BookListType(MYSQL_TEST_ID, TEST_NAME, TEST_IMAGE);

        assertTrue(this.manager.createSQLite(toCreate));
        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());
        
        toCreate = this.manager.loadSQLite(MYSQL_TEST_ID);

        assertNotNull(toCreate);
        assertEquals(TEST_NAME, toCreate.getName());
        assertEquals(TEST_IMAGE, toCreate.getImage());
    }

    @Test
    public void testJSONCreateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, MYSQL_TEST_ID);
        jsonObject.put(NAME, TEST_NAME);
        jsonObject.put(IMAGE, TEST_IMAGE);

        this.manager.createSQLite(jsonObject);

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());
        
        BookListType created = this.manager.loadSQLite(MYSQL_TEST_ID);
        
        assertNotNull(created);
        assertEquals(TEST_NAME, created.getName());
        assertEquals(TEST_IMAGE, created.getImage());
    }

    @Test
    public void testEntityUpdateSQLite() {
        BookListType updated = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(updated);
        
        updated.setName(TEST_NAME);
        updated.setImage(TEST_IMAGE);
        
        this.manager.updateSQLite(updated);
        
        updated = this.manager.loadSQLite(ENTITY_NB);
        
        assertNotNull(updated);
        assertEquals(TEST_NAME, updated.getName());
        assertEquals(TEST_IMAGE, updated.getImage());
    }

    @Test
    public void testJSONUpdateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, ENTITY_NB);
        jsonObject.put(NAME, TEST_NAME);
        jsonObject.put(IMAGE, TEST_IMAGE);

        this.manager.updateSQLite(jsonObject);
        
        BookListType updated = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(updated);
        assertEquals(TEST_NAME, updated.getName());
        assertEquals(TEST_IMAGE, updated.getImage());
    }

    @Test
    public void testLoadSQLite() {
        BookListType loaded = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(loaded);
    }

    @Test
    public void testQueryAllSQLite() {
        assertEquals(ENTITY_NB, this.manager.queryAllSQLite().size());
    }

    @Test
    public void testImportFromMySQL() {
        BookListType bookListType = this.initTestEntityMySQL();

        this.manager.importFromMySQL(APIManager.API_URL + APIManager.BOOK_LIST_TYPES + APIManager.READ +
                APIManager.START + "=" + TEST_START + "&" + APIManager.END + "=" + TEST_END);

        this.manager.waitForResponse();

        BookListType imported = this.manager.loadSQLite(bookListType.getId());

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());
        assertNotNull(imported);
        assertEquals(bookListType.getId(), imported.getId());
        assertEquals(bookListType.getName(), imported.getName());
        assertEquals(bookListType.getImage(), imported.getImage());
    }

    @Test
    public void testCreateMySQL() {
        BookListType created = this.initTestEntityMySQL();

        assertNotNull(created);
        assertEquals(MYSQL_TEST_ID, created.getId());
        assertEquals(TEST_NAME, created.getName());
        assertEquals(TEST_IMAGE, created.getImage());
    }

    @Test
    public void testLoadMySQL() {
        BookListType created = this.initTestEntityMySQL();
        BookListType loaded = this.manager.loadMySQL(created.getId());

        assertNotNull(created);
        assertNotNull(loaded);
        assertEquals(created.getId(), loaded.getId());
        assertEquals(created.getName(), loaded.getName());
        assertEquals(created.getImage(), loaded.getImage());
    }

    @Test
    public void testGetFieldSQLite() {
        BookListType type = this.manager.loadSQLite(ENTITY_NB);
        String loadedName = this.manager.getFieldSQLite(NAME, ENTITY_NB);
        String loadedImage = this.manager.getFieldSQLite(IMAGE, ENTITY_NB);
        
        assertNotNull(type);
        assertEquals(type.getName(), loadedName);
        assertEquals(type.getImage(), loadedImage);
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
     * Creates a test book list type into the database for testing.
     * @param id The id of the book list type to create.
     * @param name The name of the book list type to create.
     * @param image The image of the book list type to create.
     * @return The created Book list type.
     */
    protected BookListType initTestEntityMySQL(int id, String name, String image) {
        this.testedMySQL = true;

        this.manager.createMySQL(new BookListType(id, name, image));

        return this.manager.loadMySQL(id);
    }

    /**
     * Creates a test book list type according to the constants defined.
     * @return The created book list type.
     */
    protected BookListType initTestEntityMySQL() {
        return this.initTestEntityMySQL(MYSQL_TEST_ID, TEST_NAME, TEST_IMAGE);
    }

    /**
     * Deletes all the test entities from MySQL database.
     */
    protected void deleteMySQLTestEntities() {
        this.manager.deleteMySQLTestEntities();
    }
}