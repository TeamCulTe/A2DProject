package com.imie.a2dev.teamculte.readeo.DBManagers;

import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Category;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.imie.a2dev.teamculte.readeo.DBManagers.DBManager.MYSQL_TEST_ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public final class BookDBManagerTest extends CommonDBManagerTest {
    /**
     * Stores the default title given for tests.
     */
    private final String TEST_TITLE = "testTitle";

    /**
     * Stores the default cover given for tests.
     */
    private final String TEST_COVER = "http://www.testCover.fr";

    /**
     * Stores the default cover given for tests.
     */
    private final String TEST_SUMMARY = "testSummary";

    /**
     * Stores the default date published given for tests.
     */
    private final int TEST_DATE = 3000;

    /**
     * Stores the default category id given for tests.
     */
    private final int TEST_CATEGORY = 1;

    /**
     * Stores the associated manager used to interact with the database.
     */
    private BookDBManager manager = new BookDBManager(this.context);

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
        Category category = new CategoryDBManager(this.context).loadSQLite(TEST_CATEGORY);
        Book toCreate = new Book(MYSQL_TEST_ID, TEST_TITLE, null, TEST_COVER, TEST_SUMMARY, TEST_DATE, category, null, null);

        assertTrue(this.manager.createSQLite(toCreate));
        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());

        toCreate = this.manager.loadSQLite(MYSQL_TEST_ID);

        assertEquals(TEST_TITLE, toCreate.getTitle());
        assertEquals(TEST_COVER, toCreate.getCover());
        assertEquals(TEST_SUMMARY, toCreate.getSummary());
        assertEquals(TEST_DATE, toCreate.getDatePublished());
        assertEquals(TEST_CATEGORY, toCreate.getCategory().getId());
    }

    @Test
    public void testJSONCreateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, MYSQL_TEST_ID);
        jsonObject.put(TITLE, TEST_TITLE);
        jsonObject.put(COVER, TEST_COVER);
        jsonObject.put(SUMMARY, TEST_SUMMARY);
        jsonObject.put(DATE, TEST_DATE);
        jsonObject.put(CATEGORY, TEST_CATEGORY);

        this.manager.createSQLite(jsonObject);

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());

        Book toCreate = this.manager.loadSQLite(MYSQL_TEST_ID);

        assertEquals(TEST_TITLE, toCreate.getTitle());
        assertEquals(TEST_COVER, toCreate.getCover());
        assertEquals(TEST_SUMMARY, toCreate.getSummary());
        assertEquals(TEST_DATE, toCreate.getDatePublished());
        assertEquals(TEST_CATEGORY, toCreate.getCategory().getId());
    }

    @Test
    public void testEntityUpdateSQLite() {
        Book updated = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(updated);

        updated.setTitle(TEST_TITLE);
        updated.setCover(TEST_COVER);
        updated.setSummary(TEST_SUMMARY);
        updated.setDatePublished(TEST_DATE);
        updated.setCategory(new CategoryDBManager(this.context).loadSQLite(TEST_CATEGORY));
        this.manager.updateSQLite(updated);

        updated = this.manager.loadSQLite(ENTITY_NB);

        assertEquals(TEST_TITLE, updated.getTitle());
        assertEquals(TEST_COVER, updated.getCover());
        assertEquals(TEST_SUMMARY, updated.getSummary());
        assertEquals(TEST_DATE, updated.getDatePublished());
        assertEquals(TEST_CATEGORY, updated.getCategory().getId());
    }

    @Test
    public void testJSONUpdateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(ID, ENTITY_NB);
        jsonObject.put(TITLE, TEST_TITLE);
        jsonObject.put(COVER, TEST_COVER);
        jsonObject.put(SUMMARY, TEST_SUMMARY);
        jsonObject.put(DATE, TEST_DATE);
        jsonObject.put(CATEGORY, TEST_CATEGORY);

        this.manager.updateSQLite(jsonObject);

        Book updated = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(updated);
        assertEquals(TEST_TITLE, updated.getTitle());
        assertEquals(TEST_COVER, updated.getCover());
        assertEquals(TEST_SUMMARY, updated.getSummary());
        assertEquals(TEST_DATE, updated.getDatePublished());
        assertEquals(TEST_CATEGORY, updated.getCategory().getId());
    }

    @Test
    public void testLoadSQLite() {
        Book loaded = this.manager.loadSQLite(ENTITY_NB);

        assertNotNull(loaded);
    }

    @Test
    public void testLoadFieldFilteredSQLite() {
        // TODO : Refactor to avoid code duplication.
        String categoryFilter = "1";
        int categoryFilterResults = 2;
        String dateFilter = "19";
        int dateFilterResults = 2;
        // All filters below should match to all entries.
        String titleFilter = "tit";
        String coverFilter = "cov";
        String summaryFilter = "sum";
        List<Book> results = this.manager.loadFieldFilteredSQLite(CATEGORY, categoryFilter);

        assertNotNull(results);
        assertEquals(categoryFilterResults, results.size());

        results = this.manager.loadFieldFilteredSQLite(DATE, dateFilter);

        assertNotNull(results);
        assertEquals(dateFilterResults, results.size());

        results = this.manager.loadFieldFilteredSQLite(TITLE, titleFilter);

        assertNotNull(results);
        assertEquals(ENTITY_NB, results.size());

        results = this.manager.loadFieldFilteredSQLite(COVER, coverFilter);

        assertNotNull(results);
        assertEquals(ENTITY_NB, results.size());

        results = this.manager.loadFieldFilteredSQLite(SUMMARY, summaryFilter);

        assertNotNull(results);
        assertEquals(ENTITY_NB, results.size());
    }

    @Test
    public void testLoadCategoryNameFilteredSQLite() {
        String categoryNameFilter = "category";
        List<Book> results = this.manager.loadCategoryNameFilteredSQLite(categoryNameFilter);

        assertNotNull(results);
        assertEquals(ENTITY_NB, results.size());
    }

    @Test
    public void testLoadAuthorNameFilteredSQLite() {
        String authorNameFilter = "author";
        List<Book> results = this.manager.loadAuthorNameFilteredSQLite(authorNameFilter);

        assertNotNull(results);
        assertEquals(ENTITY_NB, results.size());
    }

    @Test
    public void testLoadCategorySQLite() {
        int categoryId = 1;
        int categoryFilterResults = 2;

        List<Book> results = this.manager.loadCategorySQLite(categoryId);

        assertNotNull(results);
        assertEquals(categoryFilterResults, results.size());
    }

    @Test
    public void testQueryAllSQLite() {
        assertEquals(ENTITY_NB, this.manager.queryAllSQLite().size());
    }

    @Test
    public void testImportFromMySQL() {
        Book book = this.initTestEntityMySQL();

        CategoryDBManager categoryDBManager = new CategoryDBManager(this.context);

        categoryDBManager.importFromMySQL(APIManager.API_URL + APIManager.CATEGORIES +
                APIManager.READ + CategoryDBSchema.ID + "=" + book.getCategory().getId());
        categoryDBManager.waitForResponse();

        this.manager.importFromMySQL(APIManager.API_URL + APIManager.BOOKS + APIManager.READ + ID + "=" +
                MYSQL_TEST_ID);
        this.manager.waitForResponse();

        Book imported = this.manager.loadSQLite(MYSQL_TEST_ID);

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());
        assertNotNull(imported);
        assertEquals(book.getId(), imported.getId());
        assertEquals(book.getTitle(), imported.getTitle());
        assertEquals(book.getCover(), imported.getCover());
        assertEquals(book.getSummary(), imported.getSummary());
        assertEquals(book.getDatePublished(), imported.getDatePublished());
        assertEquals(book.getCategory().getId(), imported.getCategory().getId());
    }

    @Test
    public void testCreateMySQL() {
        Book created = this.initTestEntityMySQL();

        assertNotNull(created);
        assertEquals(MYSQL_TEST_ID, created.getId());
        assertEquals(TEST_TITLE, created.getTitle());
        assertEquals(TEST_COVER, created.getCover());
        assertEquals(TEST_SUMMARY, created.getSummary());
        assertEquals(TEST_DATE, created.getDatePublished());
        assertEquals(MYSQL_TEST_ID, created.getCategory().getId());
    }

    @Test
    public void testLoadMySQL() {
        Book created = this.initTestEntityMySQL();
        Book loaded = this.manager.loadMySQL(created.getId());

        assertNotNull(created);
        assertNotNull(loaded);
        assertEquals(created.getId(), loaded.getId());
        assertEquals(created.getTitle(), loaded.getTitle());
        assertEquals(created.getCover(), loaded.getCover());
        assertEquals(created.getSummary(), loaded.getSummary());
        assertEquals(created.getDatePublished(), loaded.getDatePublished());
        assertEquals(created.getCategory().getId(), loaded.getCategory().getId());
    }

    @Test
    public void testGetFieldSQLite() {
        Book book = this.manager.loadSQLite(ENTITY_NB);
        String loadedTitle = this.manager.getFieldSQLite(TITLE, ENTITY_NB);
        String loadedCover = this.manager.getFieldSQLite(COVER, ENTITY_NB);
        String loadedSummary = this.manager.getFieldSQLite(SUMMARY, ENTITY_NB);
        int loadedDate = Integer.parseInt(this.manager.getFieldSQLite(DATE, ENTITY_NB));
        int loadedCategoryId = Integer.parseInt(this.manager.getFieldSQLite(CATEGORY, ENTITY_NB));

        assertNotNull(book);
        assertEquals(book.getTitle(), loadedTitle);
        assertEquals(book.getCover(), loadedCover);
        assertEquals(book.getSummary(), loadedSummary);
        assertEquals(book.getDatePublished(), loadedDate);
        assertEquals(book.getCategory().getId(), loadedCategoryId);
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
     * Creates a test book into the database for testing.
     * @param id The id of the book to create.
     * @param title The name of the book to create.
     * @param cover The cover of the book.
     * @param summary The summary of the book.
     * @param datePublished The date published of the book.
     * @param category The category the book belong to.
     * @return The created book.
     */
    protected Book initTestEntityMySQL(int id, String title, String cover, String summary, int datePublished,
                                       Category category) {
        this.testedMySQL = true;

        this.manager.createMySQL(new Book(id, title, null, cover, summary, datePublished, category, null, null));

        return this.manager.loadMySQL(id);
    }

    /**
     * Creates a test book according to the constants defined.
     * @return The created book.
     */
    protected Book initTestEntityMySQL() {
        Category category = new CategoryDBManagerTest().initTestEntityMySQL();

        return this.initTestEntityMySQL(MYSQL_TEST_ID, TEST_TITLE, TEST_COVER, TEST_SUMMARY, TEST_DATE, category);
    }

    /**
     * Deletes all the test entities from MySQL database.
     */
    protected void deleteMySQLTestEntities() {
        this.manager.deleteMySQLTestEntities();
        new CategoryDBManagerTest().deleteMySQLTestEntities();
    }
}