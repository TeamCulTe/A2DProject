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
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Review;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.imie.a2dev.teamculte.readeo.DBManagers.DBManager.MYSQL_TEST_ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema.BOOK;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema.REVIEW;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema.SHARED;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.ReviewDBSchema.USER;
import static org.junit.Assert.*;

public class ReviewDBManagerTest extends CommonDBManagerTest {
    /**
     * Stores the default associated user given for tests.
     */
    private final int TEST_USER = 1;

    /**
     * Stores the default associated user given for create tests.
     */
    private final int TEST_CREATE_USER = 5;

    /**
     * Stores the default associated book given for tests.
     */
    private final int TEST_BOOK = 1;

    /**
     * Stores the default review given for tests.
     */
    private final String TEST_REVIEW = "review";

    /**
     * Stores the default review given for tests.
     */
    private final boolean TEST_SHARED = true;

    /**
     * Stores the associated manager used to interact with the database.
     */
    private ReviewDBManager manager = new ReviewDBManager(this.context);

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
        Review toCreate = new Review(TEST_BOOK, TEST_CREATE_USER, TEST_REVIEW, TEST_SHARED);

        assertTrue(this.manager.createSQLite(toCreate));
        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());

        toCreate = this.manager.loadSQLite(TEST_CREATE_USER, TEST_BOOK);

        assertEquals(TEST_BOOK, toCreate.getId());
        assertEquals(TEST_CREATE_USER, toCreate.getUserId());
        assertEquals(TEST_REVIEW, toCreate.getReview());
    }

    @Test
    public void testJSONCreateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(BOOK, TEST_BOOK);
        jsonObject.put(USER, TEST_CREATE_USER);
        jsonObject.put(REVIEW, TEST_REVIEW);
        jsonObject.put(SHARED, TEST_SHARED);

        this.manager.createSQLite(jsonObject);

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());

        Review toCreate = this.manager.loadSQLite(TEST_CREATE_USER, TEST_BOOK);

        assertEquals(TEST_BOOK, toCreate.getId());
        assertEquals(TEST_CREATE_USER, toCreate.getUserId());
        assertEquals(TEST_REVIEW, toCreate.getReview());
    }

    @Test
    public void testEntityUpdateSQLite() {
        Review updated = this.manager.loadSQLite(TEST_USER, TEST_BOOK);

        assertNotNull(updated);

        updated.setReview(TEST_REVIEW);

        this.manager.updateSQLite(updated);

        updated = this.manager.loadSQLite(TEST_USER, TEST_BOOK);

        assertEquals(TEST_REVIEW, updated.getReview());
    }

    @Test
    public void testJSONUpdateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(BOOK, TEST_BOOK);
        jsonObject.put(USER, TEST_USER);
        jsonObject.put(REVIEW, TEST_REVIEW);

        this.manager.updateSQLite(jsonObject);

        Review updated = this.manager.loadSQLite(TEST_USER, TEST_BOOK);

        assertEquals(TEST_REVIEW, updated.getReview());
    }

    @Test
    public void testLoadSQLite() {
        Review loaded = this.manager.loadSQLite(TEST_USER, TEST_BOOK);

        assertNotNull(loaded);
    }
    @Test
    public void testLoadBookSQLite() {
        int bookId = 1;
        int bookResults = 2;
        List<Review> loaded = this.manager.loadBookSQLite(bookId);

        assertNotNull(loaded);
        assertEquals(bookResults, loaded.size());
    }

    @Test
    public void testLoadUserSQLite() {
        int userId = 2;
        int userResults = 3;
        List<Review> loaded = this.manager.loadUserSQLite(userId);

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
        Review review = this.initTestEntityMySQL();

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
                APIManager.READ + BookDBSchema.ID + "=" + review.getId());
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
                APIManager.READ + UserDBSchema.ID + "=" + review.getUserId());
        userDBManager.waitForResponse();

        this.manager.importFromMySQL(APIManager.API_URL + APIManager.REVIEWS + APIManager.READ + BOOK + "=" +
                review.getId() + "&" + USER + "=" + review.getUserId());
        this.manager.waitForResponse();

        Review imported = this.manager.loadSQLite(MYSQL_TEST_ID, MYSQL_TEST_ID);

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());
        assertNotNull(imported);
        assertEquals(review.getId(), imported.getId());
        assertEquals(review.getUserId(), imported.getUserId());
        assertEquals(review.getReview(), imported.getReview());
        assertEquals(review.isShared(), imported.isShared());
    }

    @Test
    public void testCreateMySQL() {
        Review created = this.initTestEntityMySQL();

        assertNotNull(created);
        assertEquals(MYSQL_TEST_ID, created.getId());
        assertEquals(MYSQL_TEST_ID, created.getUserId());
        assertEquals(TEST_REVIEW, created.getReview());
        assertEquals(TEST_SHARED, created.isShared());
    }

    @Test
    public void testUpdateMySQL() {
        String newReview = "newReview";
        Review toUpdate = this.initTestEntityMySQL();

        assertNotNull(toUpdate);

        toUpdate.setReview(newReview);

        this.manager.updateMySQL(toUpdate);
        this.manager.waitForResponse();

        toUpdate = this.manager.loadMySQL(toUpdate.getUserId(), toUpdate.getId());

        assertNotNull(toUpdate);
        assertEquals(newReview, toUpdate.getReview());
    }

    @Test
    public void testIdDeleteMySQL() {
        Review toDelete = this.initTestEntityMySQL();

        assertNotNull(toDelete);

        this.manager.deleteMySQL(toDelete.getUserId(), toDelete.getId());
        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(toDelete.getUserId(), toDelete.getId()));
    }

    @Test
    public void testEntityDeleteMySQL() {
        Review deleted = this.initTestEntityMySQL();

        assertNotNull(deleted);

        this.manager.deleteMySQL(deleted);
        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(deleted.getUserId(), deleted.getId()));
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
            assertNotNull(this.initTestEntityMySQL(user.getId(), testId, TEST_REVIEW, TEST_SHARED));
        }

        this.manager.deleteUserMySQL(user.getId());
        this.manager.waitForResponse();

        Review deleted;

        for (int i = 0; i < generatedTestEntities; i++) {
            testId = MYSQL_TEST_ID - i;
            deleted = this.manager.loadMySQL(user.getId(), testId);

            assertNull(deleted);
        }
    }

    @Test
    public void testRestoreMySQL() {
        Review toRestore = this.initTestEntityMySQL();

        assertNotNull(toRestore);

        this.manager.deleteMySQL(toRestore.getUserId(), toRestore.getId());
        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(toRestore.getUserId(), toRestore.getId()));

        this.manager.restoreMySQL(toRestore.getUserId(), toRestore.getId());
        this.manager.waitForResponse();

        assertNotNull(this.manager.loadMySQL(toRestore.getUserId(), toRestore.getId()));
    }

    @Test
    public void testRestoreUserMySQL() {
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
            assertNotNull(this.initTestEntityMySQL(user.getId(), testId, TEST_REVIEW, TEST_SHARED));
        }

        this.manager.deleteUserMySQL(user.getId());
        this.manager.waitForResponse();

        for (int i = 0; i < generatedTestEntities; i++) {
            testId = MYSQL_TEST_ID - i;

            assertNull(this.manager.loadMySQL(user.getId(), testId));
        }

        this.manager.restoreUserMySQL(user.getId());
        this.manager.waitForResponse();

        Review restored;

        for (int i = 0; i < generatedTestEntities; i++) {
            testId = MYSQL_TEST_ID - i;
            restored = this.manager.loadMySQL(user.getId(), testId);

            assertNotNull(restored);
        }
    }

    @Test
    public void testDeleteSQLite() {
        this.manager.deleteSQLite(TEST_USER, TEST_BOOK);

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
     * Initializes a test review into the MySQL database.
     * @param idAuthor The id of the associated user.
     * @param idBook The id of the associated book.
     * @param review The text of the review.
     * @param shared The visibility of the review.
     * @return The created review.
     */
    protected Review initTestEntityMySQL(int idAuthor, int idBook, String review, boolean shared) {
        this.testedMySQL = true;

        this.manager.createMySQL(new Review(idBook, idAuthor, review, shared));
        this.manager.waitForResponse();

        return this.manager.loadMySQL(idAuthor, idBook);
    }

    /**
     * Creates a test review according to the constants defined.
     * @return The created review.
     */
    protected Review initTestEntityMySQL() {
        new BookDBManagerTest().initTestEntityMySQL();
        new UserDBManagerTest().initTestEntityMySQL();

        return this.initTestEntityMySQL(MYSQL_TEST_ID, MYSQL_TEST_ID, TEST_REVIEW, TEST_SHARED);
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