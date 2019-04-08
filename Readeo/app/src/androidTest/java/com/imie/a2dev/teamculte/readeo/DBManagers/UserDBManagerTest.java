package com.imie.a2dev.teamculte.readeo.DBManagers;

import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.City;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Country;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Profile;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.imie.a2dev.teamculte.readeo.DBManagers.DBManager.MYSQL_TEST_ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.CITY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.COUNTRY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.EMAIL;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PASSWORD;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PROFILE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PSEUDO;
import static org.junit.Assert.*;

public class UserDBManagerTest extends CommonDBManagerTest {
    /**
     * Stores the default user pseudo given for tests.
     */
    private final String TEST_PSEUDO = "testPseudo";

    /**
     * Stores the default user pseudo used for SQLite load.
     */
    private final String TEST_LOAD_PSEUDO = "pseudo1";

    /**
     * Stores the default user id used for SQLite load.
     */
    private final int TEST_LOAD_ID = 1;

    /**
     * Stores the default user password given for tests.
     */
    private final String TEST_PASSWORD = "testPassword";

    /**
     * Stores the default user pseudo given for tests.
     */
    private final String TEST_EMAIL = "testemail@test.fr";

    /**
     * Stores the default user city given for tests.
     */
    private final int TEST_PROFILE = 1;


    /**
     * Stores the associated manager used to interact with the database.
     */
    private UserDBManager manager = new UserDBManager(this.context);

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
        PublicUser toCreate = new PublicUser(MYSQL_TEST_ID, TEST_PSEUDO,
                new ProfileDBManager(this.context).loadSQLite(TEST_PROFILE));

        assertTrue(this.manager.createSQLite(toCreate));
        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());

        toCreate = this.manager.loadSQLite(MYSQL_TEST_ID);

        assertEquals(TEST_PSEUDO, toCreate.getPseudo());
        assertEquals(TEST_PROFILE, toCreate.getProfile().getId());
    }

    @Test
    public void testJSONCreateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, MYSQL_TEST_ID);
        jsonObject.put(PSEUDO, TEST_PSEUDO);
        jsonObject.put(PROFILE, TEST_PROFILE);

        this.manager.createSQLite(jsonObject);

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());

        PublicUser toCreate = this.manager.loadSQLite(MYSQL_TEST_ID);

        assertEquals(TEST_PSEUDO, toCreate.getPseudo());
        assertEquals(TEST_PROFILE, toCreate.getProfile().getId());
    }

    @Test
    public void testEntityUpdateSQLite() {
        PublicUser updated = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(updated);
        updated.setPseudo(TEST_PSEUDO);

        this.manager.updateSQLite(updated);

        updated = this.manager.loadSQLite(ENTITY_NB);

        assertEquals(TEST_PSEUDO, updated.getPseudo());
    }

    @Test
    public void testJSONUpdateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, ENTITY_NB);
        jsonObject.put(PSEUDO, TEST_PSEUDO);

        this.manager.updateSQLite(jsonObject);

        PublicUser updated = this.manager.loadSQLite(ENTITY_NB);

        assertEquals(TEST_PSEUDO, updated.getPseudo());
    }

    @Test
    public void testIdLoadSQLite() {
        PublicUser loaded = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(loaded);
    }

    @Test
    public void testPseudoLoadSQLite() {
        PublicUser loaded = this.manager.loadSQLite(TEST_LOAD_PSEUDO);

        assertNotNull(loaded);
    }

    @Test
    public void testQueryAllSQLite() {
        assertEquals(ENTITY_NB, this.manager.queryAllSQLite().size());
    }

    @Test
    public void testImportFromMySQL() {
        this.initTestEntityMySQL();

        ProfileDBManager profileDBManager = new ProfileDBManager(this.context);

        profileDBManager.importFromMySQL(APIManager.API_URL + APIManager.PROFILES +
                APIManager.READ + ProfileDBSchema.ID + "=" + MYSQL_TEST_ID);
        profileDBManager.waitForResponse();

        this.manager.importFromMySQL(APIManager.API_URL + APIManager.USERS + APIManager.READ + ID + "=" +
                MYSQL_TEST_ID);
        this.manager.waitForResponse();

        PublicUser created = this.manager.loadMySQL(MYSQL_TEST_ID);

        assertNotNull(created);
        assertEquals(MYSQL_TEST_ID, created.getId());
        assertEquals(TEST_PSEUDO, created.getPseudo());
        assertEquals(MYSQL_TEST_ID, created.getProfile().getId());
    }

    @Test
    public void testCreateMySQL() {
        PrivateUser created = this.initTestEntityMySQL();

        assertNotNull(created);
        assertEquals(TEST_PSEUDO, created.getPseudo());
        assertEquals(TEST_PASSWORD, created.getPassword());
        assertEquals(TEST_EMAIL, created.getEmail());
        assertEquals(MYSQL_TEST_ID, created.getProfile().getId());
        assertEquals(MYSQL_TEST_ID, created.getCity().getId());
        assertEquals(MYSQL_TEST_ID, created.getCountry().getId());
    }

    @Test
    public void testAuthLoadMySQL() {
        PrivateUser created = this.initTestEntityMySQL();

        assertNotNull(created);

        PrivateUser loaded = this.manager.loadMySQL(created.getEmail(), created.getPassword());

        assertNotNull(loaded);
        assertEquals(created.getId(), loaded.getId());
        assertEquals(created.getPseudo(), loaded.getPseudo());
        assertEquals(created.getPassword(), loaded.getPassword());
        assertEquals(created.getEmail(), loaded.getEmail());
        assertEquals(created.getProfile().getId(), loaded.getProfile().getId());
        assertEquals(created.getCity().getId(), loaded.getCity().getId());
        assertEquals(created.getCountry().getId(), loaded.getCountry().getId());
    }

    @Test
    public void testUpdateMySQL() {
        PrivateUser user = this.initTestEntityMySQL();

        assertNotNull(user);

        int newId = -667;
        String newPseudo = "newPseudo";
        String newPassword = "newPassword";
        String newEmail = "newEmail@new.fr";
        String newCity = "newCity";
        String newCountry = "newCountry";

        CityDBManagerTest cityDBManagerTest = new CityDBManagerTest();
        CountryDBManagerTest countryDBManagerTest = new CountryDBManagerTest();
        City city = cityDBManagerTest.initTestEntityMySQL(newId, newCity);
        Country country = countryDBManagerTest.initTestEntityMySQL(newId, newCountry);

        assertNotNull(city);
        assertNotNull(country);

        user.setPseudo(newPseudo);
        user.setPassword(newPassword);
        user.setEmail(newEmail);
        user.setCity(city);
        user.setCountry(country);

        this.manager.updateMySQL(user);
        this.manager.waitForResponse();

        user = this.manager.loadMySQL(user.getId());

        assertNotNull(user);
        assertEquals(newPseudo, user.getPseudo());
        assertEquals(newPassword, user.getPassword());
        assertEquals(newEmail, user.getEmail());
        assertEquals(newId, user.getCity().getId());
        assertEquals(newId, user.getCountry().getId());
    }

    @Test
    public void testUpdateFieldMySQL() {
        PrivateUser user = this.initTestEntityMySQL();

        assertNotNull(user);

        int newId = -667;
        String newPseudo = "newPseudo";
        String newPassword = "newPassword";
        String newEmail = "newEmail@new.fr";
        String newCity = "newCity";
        String newCountry = "newCountry";

        CityDBManagerTest cityDBManagerTest = new CityDBManagerTest();
        CountryDBManagerTest countryDBManagerTest = new CountryDBManagerTest();

        assertNotNull(cityDBManagerTest.initTestEntityMySQL(newId, newCity));
        assertNotNull(countryDBManagerTest.initTestEntityMySQL(newId, newCountry));

        this.manager.updateFieldMySQL(user.getId(), PSEUDO, newPseudo);
        this.manager.updateFieldMySQL(user.getId(), PASSWORD, newPassword);
        this.manager.updateFieldMySQL(user.getId(), EMAIL, newEmail);
        this.manager.updateFieldMySQL(user.getId(), CITY, String.valueOf(newId));
        this.manager.updateFieldMySQL(user.getId(), COUNTRY, String.valueOf(newId));
        this.manager.waitForResponse();

        user = this.manager.loadMySQL(user.getId());

        assertNotNull(user);
        assertEquals(newPseudo, user.getPseudo());
        assertEquals(newPassword, user.getPassword());
        assertEquals(newEmail, user.getEmail());
        assertEquals(newId, user.getCity().getId());
        assertEquals(newId, user.getCountry().getId());
    }

    @Test
    public void testAuthDeleteMySQL() {
        PrivateUser toDelete = this.initTestEntityMySQL();

        assertNotNull(toDelete);

        this.manager.deleteMySQL(toDelete.getEmail(), toDelete.getPassword());
        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(toDelete.getId()));
    }

    @Test
    public void testEntityDeleteMySQL() {
        PrivateUser user = this.initTestEntityMySQL();

        assertNotNull(user);

        this.manager.deleteMySQL(user);
        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(user.getId()));
    }

    @Test
    public void testIdRestoreMySQL() {
        PrivateUser toRestore = this.initTestEntityMySQL();

        assertNotNull(toRestore);

        this.manager.deleteMySQL(toRestore.getEmail(), toRestore.getPassword());
        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(toRestore.getId()));

        this.manager.restoreMySQL(toRestore.getId());
        this.manager.waitForResponse();

        assertNotNull(this.manager.loadMySQL(toRestore.getId()));
    }

    @Test
    public void testAuthRestoreMySQL() {
        PrivateUser toRestore = this.initTestEntityMySQL();

        assertNotNull(toRestore);

        this.manager.deleteMySQL(toRestore.getEmail(), toRestore.getPassword());
        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(toRestore.getId()));

        this.manager.restoreMySQL(toRestore.getEmail(), toRestore.getPassword());
        this.manager.waitForResponse();

        assertNotNull(this.manager.loadMySQL(toRestore.getId()));
    }

    @Test
    public void testGetFieldFromPseudoSQLite() {
        int loadedProfile = Integer.parseInt(this.manager.getFieldFromPseudoSQLite(PROFILE, TEST_LOAD_PSEUDO));

        assertEquals(TEST_LOAD_ID, loadedProfile);
    }

    @Test
    public void TestSQLiteGetId() {
        int loadedId = this.manager.SQLiteGetId(TEST_LOAD_PSEUDO);

        assertEquals(TEST_LOAD_ID, loadedId);
    }

    @Test
    public void testLoadFilteredSQLite() {
        List<PublicUser> users = this.manager.loadFilteredSQLite(PSEUDO, "pseudo");

        assertNotNull(users);
        assertEquals(ENTITY_NB, users.size());
    }

    @Test
    public void testGetFieldSQLite() {
        PublicUser user = this.manager.loadSQLite(ENTITY_NB);
        String loadedPseudo = this.manager.getFieldSQLite(PSEUDO, ENTITY_NB);
        int loadedProfile = Integer.parseInt(this.manager.getFieldSQLite(PROFILE, ENTITY_NB));

        assertNotNull(user);
        assertEquals(user.getPseudo(), loadedPseudo);
        assertEquals(user.getProfile().getId(), loadedProfile);
    }

    @Test
    public void deleteSQLite() {
        this.manager.deleteSQLite(ENTITY_NB);

        assertEquals(ENTITY_NB - 1, this.manager.countSQLite());
    }

    @Test
    public void countSQLite() {
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
     * Initializes a test user into the MySQL database.
     * @param id The id of the user.
     * @param pseudo The id of the associated book.
     * @param password The text of the review.
     * @param email The email of the user.
     * @return The created user.
     */
    protected PrivateUser initTestEntityMySQL(int id, String pseudo, String password, String email, Profile profile,
                                       Country country, City city) {
        this.testedMySQL = true;

        PrivateUser user = new PrivateUser(id, pseudo, password, email, profile, country, city, null, null);

        this.manager.createMySQL(user);
        this.manager.waitForResponse();

        return this.manager.loadMySQL(id);
    }

    /**
     * Initializes a test user according to the constants defined.
     * @return The created user.
     */
    protected PrivateUser initTestEntityMySQL() {
        City city = new CityDBManagerTest().initTestEntityMySQL();
        Profile profile = new ProfileDBManagerTest().initTestEntityMySQL();
        Country country = new CountryDBManagerTest().initTestEntityMySQL();


        return this.initTestEntityMySQL(MYSQL_TEST_ID, TEST_PSEUDO, TEST_PASSWORD, TEST_EMAIL, profile, country, city);
    }

    /**
     * Deletes all the test entities from MySQL database.
     */
    protected void deleteMySQLTestEntities() {
        this.manager.deleteMySQLTestEntities();
        new ProfileDBManagerTest().deleteMySQLTestEntities();
        new CountryDBManagerTest().deleteMySQLTestEntities();
        new CityDBManagerTest().deleteMySQLTestEntities();
    }
}