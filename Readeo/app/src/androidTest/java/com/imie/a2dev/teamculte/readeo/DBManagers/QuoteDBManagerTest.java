package com.imie.a2dev.teamculte.readeo.DBManagers;

import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CityDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CountryDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.ProfileDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Category;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Quote;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.imie.a2dev.teamculte.readeo.DBManagers.DBManager.MYSQL_TEST_ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.BOOK;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.USER;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.QuoteDBSchema.QUOTE;
import static org.junit.Assert.*;

public class QuoteDBManagerTest extends CommonDBManagerTest {
    /**
     * Stores the default associated user given for tests.
     */
    private final int TEST_USER = 5;

    /**
     * Stores the default associated book given for tests.
     */
    private final int TEST_BOOK = 1;

    /**
     * Stores the default quote given for tests.
     */
    private final String TEST_QUOTE = "testQuote";

    /**
     * Stores the associated manager used to interact with the database.
     */
    private QuoteDBManager manager = new QuoteDBManager(this.context);

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
        Quote toCreate = new Quote(MYSQL_TEST_ID, TEST_BOOK, TEST_USER, TEST_QUOTE);

        assertTrue(this.manager.createSQLite(toCreate));
        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());

        toCreate = this.manager.loadSQLite(MYSQL_TEST_ID);

        assertEquals(TEST_BOOK, toCreate.getBookId());
        assertEquals(TEST_USER, toCreate.getUserId());
        assertEquals(TEST_QUOTE, toCreate.getQuote());
    }

    @Test
    public void testJSONCreateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, MYSQL_TEST_ID);
        jsonObject.put(BOOK, TEST_BOOK);
        jsonObject.put(USER, TEST_USER);
        jsonObject.put(QUOTE, TEST_QUOTE);

        this.manager.createSQLite(jsonObject);

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());

        Quote toCreate = this.manager.loadSQLite(MYSQL_TEST_ID);

        assertEquals(TEST_BOOK, toCreate.getBookId());
        assertEquals(TEST_USER, toCreate.getUserId());
        assertEquals(TEST_QUOTE, toCreate.getQuote());
    }

    @Test
    public void testEntityUpdateSQLite() {
        Quote updated = this.manager.loadSQLite(ENTITY_NB);
        String newQuote = "newQuote";

        assertNotNull(updated);

        updated.setQuote(newQuote);

        this.manager.updateSQLite(updated);

        updated = this.manager.loadSQLite(ENTITY_NB);

        assertEquals(newQuote, updated.getQuote());
    }

    @Test
    public void testJSONUpdateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, ENTITY_NB);
        jsonObject.put(QUOTE, TEST_QUOTE);

        this.manager.updateSQLite(jsonObject);

        Quote updated = this.manager.loadSQLite(ENTITY_NB);

        assertEquals(TEST_QUOTE, updated.getQuote());
    }

    @Test
    public void testLoadSQLite() {
        Quote loaded = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(loaded);
    }

    @Test
    public void testLoadBookSQLite() {
        int bookId = 1;
        int bookResults = 2;
        List<Quote> loaded = this.manager.loadBookSQLite(bookId);

        assertNotNull(loaded);
        assertEquals(bookResults, loaded.size());
    }

    @Test
    public void testLoadUserSQLite() {
        int userId = 2;
        int userResults = 3;
        List<Quote> loaded = this.manager.loadUserSQLite(userId);

        assertNotNull(loaded);
        assertEquals(userResults, loaded.size());
    }

    @Test
    public void testDeleteUserSQLite() {
        int userId = 2;
        int userResults = 3;

        this.manager.deleteUserSQLite(userId);

        assertEquals(ENTITY_NB - userResults, this.manager.countSQLite());
    }

    @Test
    public void testDeleteBookSQLite() {
        int bookId = 1;
        int bookResults = 2;

        this.manager.deleteBookSQLite(bookId);

        assertEquals(ENTITY_NB - bookResults, this.manager.countSQLite());
    }

    @Test
    public void testQueryAllSQLite() {
        assertEquals(ENTITY_NB, this.manager.queryAllSQLite().size());
    }

    @Test
    public void testImportFromMySQL() {
        this.initTestEntityMySQL();

        CategoryDBManager categoryDBManager = new CategoryDBManager(this.context);
        BookDBManager bookDBManager = new BookDBManager(this.context);
        CountryDBManager countryDBManager = new CountryDBManager(this.context);
        CityDBManager cityDBManager = new CityDBManager(this.context);
        ProfileDBManager profileDBManager = new ProfileDBManager(this.context);
        UserDBManager userDBManager = new UserDBManager(this.context);

        categoryDBManager.importFromMySQL(APIManager.API_URL + APIManager.CATEGORIES +
                APIManager.READ + CategoryDBSchema.ID + "=" + MYSQL_TEST_ID);
        categoryDBManager.waitForResponse();

        bookDBManager.importFromMySQL(APIManager.API_URL + APIManager.BOOKS +
                APIManager.READ + BookDBSchema.ID + "=" + MYSQL_TEST_ID);
        bookDBManager.waitForResponse();

        countryDBManager.importFromMySQL(APIManager.API_URL + APIManager.COUNTRIES +
                APIManager.READ + CountryDBSchema.ID + "=" + MYSQL_TEST_ID);
        countryDBManager.waitForResponse();

        cityDBManager.importFromMySQL(APIManager.API_URL + APIManager.CITIES +
                APIManager.READ + CityDBSchema.ID + "=" + MYSQL_TEST_ID);
        cityDBManager.waitForResponse();

        profileDBManager.importFromMySQL(APIManager.API_URL + APIManager.PROFILES +
                APIManager.READ + ProfileDBSchema.ID + "=" + MYSQL_TEST_ID);
        profileDBManager.waitForResponse();

        userDBManager.importFromMySQL(APIManager.API_URL + APIManager.USERS +
                APIManager.READ + UserDBSchema.ID + "=" + MYSQL_TEST_ID);
        userDBManager.waitForResponse();

        this.manager.importFromMySQL(APIManager.API_URL + APIManager.QUOTES + APIManager.READ + ID + "=" +
                MYSQL_TEST_ID);
        this.manager.waitForResponse();

        Quote created = this.manager.loadSQLite(MYSQL_TEST_ID);

        assertNotNull(created);
        assertEquals(MYSQL_TEST_ID, created.getId());
        assertEquals(MYSQL_TEST_ID, created.getBookId());
        assertEquals(MYSQL_TEST_ID, created.getUserId());
        assertEquals(TEST_QUOTE, created.getQuote());
    }

    @Test
    public void testCreateMySQL() {
        Quote created = this.initTestEntityMySQL();

        assertNotNull(created);
        assertEquals(MYSQL_TEST_ID, created.getId());
        assertEquals(MYSQL_TEST_ID, created.getBookId());
        assertEquals(MYSQL_TEST_ID, created.getUserId());
        assertEquals(TEST_QUOTE, created.getQuote());
    }

    @Test
    public void testUpdateMySQL() {
        String newQuote = "newQuote";
        Quote toUpdate = this.initTestEntityMySQL();

        assertNotNull(toUpdate);

        toUpdate.setQuote(newQuote);

        this.manager.updateMySQL(toUpdate);
        this.manager.waitForResponse();

        toUpdate = this.manager.loadMySQL(toUpdate.getId());

        assertNotNull(toUpdate);
        assertEquals(newQuote, toUpdate.getQuote());
    }

    @Test
    public void testIdDeleteMySQL() {
        Quote toDelete = this.initTestEntityMySQL();

        assertNotNull(toDelete);

        this.manager.deleteMySQL(toDelete.getId());
        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(toDelete.getId()));
    }

    @Test
    public void testEntityDeleteMySQL() {
        Quote deleted = this.initTestEntityMySQL();

        assertNotNull(deleted);

        this.manager.deleteMySQL(deleted);
        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(deleted.getId()));
    }

    @Test
    public void testDeleteUserMySQL() {
        CategoryDBManagerTest categoryDBManagerTest = new CategoryDBManagerTest();
        BookDBManagerTest bookDBManagerTest = new BookDBManagerTest();
        UserDBManagerTest userDBManagerTest = new UserDBManagerTest();

        Category category = categoryDBManagerTest.initTestEntityMySQL();
        PrivateUser user = userDBManagerTest.initTestEntityMySQL();

        assertNotNull(category);
        assertNotNull(user);

        int testId;
        String testString;

        int generatedTestEntities = 5;

        for (int i = 0; i < generatedTestEntities; i++) {
            testId = MYSQL_TEST_ID - i;
            testString = "testString" + String.valueOf(testId);

            assertNotNull(bookDBManagerTest.initTestEntityMySQL(testId, testString, testString, testString, 2000, category));
            assertNotNull(this.initTestEntityMySQL(testId, user.getId(), testId, TEST_QUOTE));
        }

        this.manager.deleteUserMySQL(user.getId());
        this.manager.waitForResponse();

        Quote deleted;

        for (int i = 0; i < generatedTestEntities; i++) {
            testId = MYSQL_TEST_ID - i;
            deleted = this.manager.loadMySQL(testId);

            assertNull(deleted);
        }
    }

    @Test
    public void testRestoreMySQL() {
        Quote toRestore = this.initTestEntityMySQL();

        assertNotNull(toRestore);

        this.manager.deleteMySQL(toRestore.getId());
        this.manager.waitForResponse();


        assertNull(this.manager.loadMySQL(toRestore.getId()));

        this.manager.restoreMySQL(toRestore.getId());
        this.manager.waitForResponse();

        assertNotNull(this.manager.loadMySQL(toRestore.getId()));
    }

    @Test
    public void testRestoreUserMySQL() {
        // TODO : See how to refactor in order to avoid code duplication.
        CategoryDBManagerTest categoryDBManagerTest = new CategoryDBManagerTest();
        BookDBManagerTest bookDBManagerTest = new BookDBManagerTest();
        UserDBManagerTest userDBManagerTest = new UserDBManagerTest();

        Category category = categoryDBManagerTest.initTestEntityMySQL();
        PrivateUser user = userDBManagerTest.initTestEntityMySQL();

        assertNotNull(category);
        assertNotNull(user);

        int testId;
        String testString;

        int generatedTestEntities = 5;

        for (int i = 0; i < generatedTestEntities; i++) {
            testId = MYSQL_TEST_ID - i;
            testString = "testString" + String.valueOf(testId);

            assertNotNull(bookDBManagerTest.initTestEntityMySQL(testId, testString, testString, testString, 2000, category));
            assertNotNull(this.initTestEntityMySQL(testId, user.getId(), testId, TEST_QUOTE));
        }

        this.manager.deleteUserMySQL(user.getId());
        this.manager.waitForResponse();

        for (int i = 0; i < generatedTestEntities; i++) {
            testId = MYSQL_TEST_ID - i;

            assertNull(this.manager.loadMySQL(testId));
        }

        this.manager.restoreUserMySQL(user.getId());
        this.manager.waitForResponse();

        Quote restored;

        for (int i = 0; i < generatedTestEntities; i++) {
            testId = MYSQL_TEST_ID - i;
            restored = this.manager.loadMySQL(testId);

            assertNotNull(restored);
        }
    }

    @Test
    public void testGetFieldSQLite() {
        Quote quote = this.manager.loadSQLite(ENTITY_NB);
        int loadedBook = Integer.parseInt(this.manager.getFieldSQLite(BOOK, ENTITY_NB));
        int loadedUser = Integer.parseInt(this.manager.getFieldSQLite(USER, ENTITY_NB));
        String loadedQuote = this.manager.getFieldSQLite(QUOTE, ENTITY_NB);

        assertNotNull(quote);
        assertEquals(quote.getBookId(), loadedBook);
        assertEquals(quote.getUserId(), loadedUser);
        assertEquals(quote.getQuote(), loadedQuote);
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
     * Initializes a test entity into the MySQL database.
     * @param id The id of the quote.
     * @param idAuthor The id of the associated user.
     * @param idBook The id of the associated book.
     * @param quote The text of the quote.
     * @return The created quote.
     */
    protected Quote initTestEntityMySQL(int id, int idAuthor, int idBook, String quote) {
        this.testedMySQL = true;

        this.manager.createMySQL(new Quote(id, idBook, idAuthor, quote));
        this.manager.waitForResponse();

        return this.manager.loadMySQL(id);
    }

    /**
     * Creates a test quote according to the constants defined.
     * @return The created quote.
     */
    protected Quote initTestEntityMySQL() {
        new BookDBManagerTest().initTestEntityMySQL();
        new UserDBManagerTest().initTestEntityMySQL();

        return this.initTestEntityMySQL(MYSQL_TEST_ID, MYSQL_TEST_ID, MYSQL_TEST_ID, TEST_QUOTE);
    }

    /**
     * Deletes all the test entities from MySQL database.
     */
    protected void deleteMySQLTestEntities() {
        this.manager.deleteMySQLTestEntities();
        new BookDBManagerTest().deleteMySQLTestEntities();
        new UserDBManagerTest().deleteMySQLTestEntities();
    }
}