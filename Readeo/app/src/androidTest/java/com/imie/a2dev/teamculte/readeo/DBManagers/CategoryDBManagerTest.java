package com.imie.a2dev.teamculte.readeo.DBManagers;

import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Category;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.imie.a2dev.teamculte.readeo.DBManagers.DBManager.MYSQL_TEST_ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema.NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public final class CategoryDBManagerTest extends CommonDBManagerTest {
    /**
     * Stores the default name given for tests.
     */
    private final String TEST_NAME = "testName";

    /**
     * Stores the associated manager used to interact with the database.
     */
    private CategoryDBManager manager = new CategoryDBManager(this.context);

    @Test
    public void testEntityCreateSQLite() {
        Category toCreate = new Category(MYSQL_TEST_ID, TEST_NAME);

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
        Category updated = this.manager.loadSQLite(ENTITY_NB);

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
        Category loaded = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(loaded);
    }

    @Test
    public void testQueryAllSQLite() {
        assertEquals(ENTITY_NB, this.manager.queryAllSQLite().size());
    }

    @Test
    public void testImportFromMySQL() {
        Category category = this.initTestEntityMySQL();

        this.manager.importFromMySQL(APIManager.API_URL + APIManager.CATEGORIES + APIManager.READ + APIManager.START
                                     + "=" + TEST_START + "&" + APIManager.END + "=" + TEST_END);

        this.manager.waitForResponse();

        Category imported = this.manager.loadSQLite(category.getId());

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());
        assertNotNull(imported);
        assertEquals(category.getId(), imported.getId());
        assertEquals(category.getName(), imported.getName());
    }

    @Test
    public void testCreateMySQL() {
        Category created = this.initTestEntityMySQL();

        assertNotNull(created);
        assertEquals(MYSQL_TEST_ID, created.getId());
        assertEquals(TEST_NAME, created.getName());
    }

    @Test
    public void testLoadMySQL() {
        Category created = this.initTestEntityMySQL();
        Category loaded = this.manager.loadMySQL(created.getId());

        assertNotNull(created);
        assertNotNull(loaded);
        assertEquals(created.getId(), loaded.getId());
        assertEquals(created.getName(), loaded.getName());
    }

    @Test
    public void testGetFieldSQLite() {
        Category category = this.manager.loadSQLite(ENTITY_NB);
        String loadedName = this.manager.getFieldSQLite(NAME, ENTITY_NB);

        assertNotNull(category);
        assertEquals(category.getName(), loadedName);
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
     * Creates a test category into the database for testing.
     * @param id The id of the category to create.
     * @param name The name of the category to create.
     * @return The created category.
     */
    protected Category initTestEntityMySQL(int id, String name) {
        this.testedMySQL = true;

        this.manager.createMySQL(new Category(id, name));

        return this.manager.loadMySQL(id);
    }

    /**
     * Creates a test category according to the constants defined.
     * @return The created city.
     */
    protected Category initTestEntityMySQL() {
        return this.initTestEntityMySQL(MYSQL_TEST_ID, TEST_NAME);
    }

    /**
     * Deletes all the test entities from MySQL database.
     */
    protected void deleteMySQLTestEntities() {
        this.manager.deleteMySQLTestEntities();
    }
}