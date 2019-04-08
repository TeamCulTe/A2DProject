package com.imie.a2dev.teamculte.readeo.DBManagers;

import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Profile;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.imie.a2dev.teamculte.readeo.DBManagers.DBManager.MYSQL_TEST_ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.AVATAR;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema.DESCRIPTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ProfileDBManagerTest extends CommonDBManagerTest {
    /**
     * Stores the default avatar given for tests.
     */
    private final String TEST_AVATAR = "testAvatar";

    /**
     * Stores the default description given for tests.
     */
    private final String TEST_DESC = "testDescription";

    /**
     * Stores the associated manager used to interact with the database.
     */
    private ProfileDBManager manager = new ProfileDBManager(this.context);

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
        Profile toCreate = new Profile(MYSQL_TEST_ID, TEST_AVATAR, TEST_DESC);

        assertTrue(this.manager.createSQLite(toCreate));
        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());

        toCreate = this.manager.loadSQLite(MYSQL_TEST_ID);

        assertEquals(TEST_AVATAR, toCreate.getAvatar());
        assertEquals(TEST_DESC, toCreate.getDescription());
    }

    @Test
    public void testJSONCreateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, MYSQL_TEST_ID);
        jsonObject.put(AVATAR, TEST_AVATAR);
        jsonObject.put(DESCRIPTION, TEST_DESC);

        this.manager.createSQLite(jsonObject);

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());

        Profile toCreate = this.manager.loadSQLite(MYSQL_TEST_ID);

        assertEquals(TEST_AVATAR, toCreate.getAvatar());
        assertEquals(TEST_DESC, toCreate.getDescription());
    }

    @Test
    public void testEntityUpdateSQLite() {
        Profile updated = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(updated);
        updated.setAvatar(TEST_AVATAR);
        updated.setDescription(TEST_DESC);
        this.manager.updateSQLite(updated);

        updated = this.manager.loadSQLite(ENTITY_NB);

        assertEquals(TEST_AVATAR, updated.getAvatar());
        assertEquals(TEST_DESC, updated.getDescription());
    }

    @Test
    public void testJSONUpdateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, ENTITY_NB);
        jsonObject.put(AVATAR, TEST_AVATAR);
        jsonObject.put(DESCRIPTION, TEST_DESC);

        this.manager.updateSQLite(jsonObject);

        Profile updated = this.manager.loadSQLite(ENTITY_NB);

        assertEquals(TEST_AVATAR, updated.getAvatar());
        assertEquals(TEST_DESC, updated.getDescription());
    }

    @Test
    public void testLoadSQLite() {
        Profile loaded = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(loaded);
    }

    @Test
    public void testQueryAllSQLite() {
        assertEquals(ENTITY_NB, this.manager.queryAllSQLite().size());
    }

    @Test
    public void testImportFromMySQL() {
        String parametersValue = String.valueOf(ENTITY_NB);

        this.manager.importFromMySQL(APIManager.API_URL + APIManager.PROFILES + APIManager.READ + APIManager.START +
                "=" + parametersValue + "&" + APIManager.END + "=" + parametersValue);

        this.manager.waitForResponse();

        assertEquals(ENTITY_NB * 2, this.manager.countSQLite());
    }

    @Test
    public void testCreateMySQL() {
        Profile created = this.initTestEntityMySQL();

        assertNotNull(created);
        assertEquals(MYSQL_TEST_ID, created.getId());
        assertEquals(TEST_AVATAR, created.getAvatar());
        assertEquals(TEST_DESC, created.getDescription());
    }

    @Test
    public void testUpdateMySQL() {
        Profile toUpdate = this.initTestEntityMySQL();
        String newAvatar = "newAvatar";
        String newDescription = "newDescription";

        assertNotNull(toUpdate);
        toUpdate.setAvatar(newAvatar);
        toUpdate.setDescription(newDescription);

        this.manager.updateMySQL(toUpdate);
        this.manager.waitForResponse();

        toUpdate = this.manager.loadMySQL(toUpdate.getId());

        assertNotNull(toUpdate);
        assertEquals(newAvatar, toUpdate.getAvatar());
        assertEquals(newDescription, toUpdate.getDescription());
    }

    @Test
    public void testUpdateFieldMySQL() {
        Profile toUpdate = this.initTestEntityMySQL();

        assertNotNull(toUpdate);

        String newAvatarField = "newAvatarField";
        String newDescriptionField = "newDescriptionField";

        this.manager.updateFieldMySQL(toUpdate.getId(), AVATAR, newAvatarField);
        this.manager.updateFieldMySQL(toUpdate.getId(), DESCRIPTION, newDescriptionField);
        this.manager.waitForResponse();

        toUpdate = this.manager.loadMySQL(toUpdate.getId());

        assertNotNull(toUpdate);
        assertEquals(newAvatarField, toUpdate.getAvatar());
        assertEquals(newDescriptionField, toUpdate.getDescription());
    }

    @Test
    public void testIdDeleteMySQL() {
        Profile toDelete = this.initTestEntityMySQL();

        assertNotNull(toDelete);

        this.manager.deleteMySQL(toDelete.getId());
        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(toDelete.getId()));
    }

    @Test
    public void testEntityDeleteMySQL() {
        Profile deleted = this.initTestEntityMySQL();

        assertNotNull(deleted);

        this.manager.deleteMySQL(deleted);
        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(deleted.getId()));
    }

    @Test
    public void testRestoreMySQL() {
        Profile toRestore = this.initTestEntityMySQL();

        assertNotNull(toRestore);

        this.manager.deleteMySQL(toRestore.getId());
        this.manager.waitForResponse();
        
        assertNull(this.manager.loadMySQL(toRestore.getId()));

        this.manager.restoreMySQL(toRestore.getId());
        this.manager.waitForResponse();

        assertNotNull(this.manager.loadMySQL(toRestore.getId()));
    }

    @Test
    public void testGetFieldSQLite() {
        Profile profile = this.manager.loadSQLite(ENTITY_NB);
        String loadedAvatar = this.manager.getFieldSQLite(AVATAR, ENTITY_NB);
        String loadedDescription = this.manager.getFieldSQLite(DESCRIPTION, ENTITY_NB);

        assertNotNull(profile);
        assertEquals(profile.getAvatar(), loadedAvatar);
        assertEquals(profile.getDescription(), loadedDescription);
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
     * Creates a test profile into the database for testing.
     * @param id The id of the book to create.
     * @param avatar The avatar of the profile to create.
     * @param description The description of the profile.
     * @return The created profile.
     */
    protected Profile initTestEntityMySQL(int id, String avatar, String description) {
        this.testedMySQL = true;

        this.manager.createMySQL(new Profile(id, avatar, description));
        this.manager.waitForResponse();

        return this.manager.loadMySQL(id);
    }

    /**
     * Creates a test profile according to the constants defined.
     * @return The created profile.
     */
    protected Profile initTestEntityMySQL() {
        return this.initTestEntityMySQL(MYSQL_TEST_ID, TEST_AVATAR, TEST_DESC);
    }

    /**
     * Deletes all the test entities from MySQL database.
     */
    protected void deleteMySQLTestEntities() {
        this.manager.deleteMySQLTestEntities();
    }
}