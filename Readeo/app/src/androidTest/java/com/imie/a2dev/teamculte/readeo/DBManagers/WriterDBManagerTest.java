package com.imie.a2dev.teamculte.readeo.DBManagers;

import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.imie.a2dev.teamculte.readeo.DBManagers.DBManager.MYSQL_TEST_ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema.AUTHOR;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.WriterDBSchema.BOOK;
import static org.junit.Assert.*;

public class WriterDBManagerTest extends CommonDBManagerTest {
    /**
     * Stores the default associated book id given for create tests.
     */
    private final int TEST_CREATE_BOOK = 2;

    /**
     * Stores the default associated book id given for load tests.
     */
    private final int TEST_LOAD_BOOK = 1;

    /**
     * Stores the default associated author id given for load tests.
     */
    private final int TEST_LOAD_AUTHOR = 3;

    /**
     * Stores the default associated book or author number for load tests.
     */
    private final int LOADS_RESULTS = 2;

    /**
     * Stores the associated manager used to interact with the database.
     */
    private WriterDBManager manager = new WriterDBManager(this.context);

    /**
     * Stores the books manager used to interact with the database.
     */
    private BookDBManager bookDBManager = new BookDBManager(this.context);

    /**
     * Stores the author manager used to interact with the database.
     */
    private AuthorDBManager authorDBManager = new AuthorDBManager(this.context);

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
    public void testJSONCreateSQLite() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(AUTHOR, TEST_LOAD_AUTHOR);
        jsonObject.put(BOOK, TEST_CREATE_BOOK);

        this.manager.createSQLite(jsonObject);

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());
    }

    @Test
    public void testCreateSQLiteBook() {
        Book created = this.bookDBManager.loadSQLite(TEST_CREATE_BOOK);
        List<Author> authors = this.authorDBManager.queryAllSQLite();
        assertNotNull(created);
        assertNotNull(authors);

        created.setAuthors(authors);
        this.manager.createSQLiteBook(created);

        assertEquals(ENTITY_NB + authors.size(), this.manager.countSQLite());
    }

    @Test
    public void testLoadAuthorsSQLite() {
        List<Author> authors = this.manager.loadAuthorsSQLite(TEST_LOAD_BOOK);

        assertNotNull(authors);
        assertEquals(LOADS_RESULTS, authors.size());
    }

    @Test
    public void testLoadBooksSQLite() {
        List<Book> books = this.manager.loadBooksSQLite(TEST_LOAD_AUTHOR);

        assertNotNull(books);
        assertEquals(LOADS_RESULTS, books.size());
    }

    @Test
    public void testDeleteSQLite() {
        this.manager.deleteSQLite(ENTITY_NB, ENTITY_NB);

        assertEquals(ENTITY_NB - 1, this.manager.countSQLite());
    }

    @Test
    public void testDeleteSQLiteAuthor() {
        this.manager.deleteAuthorSQLite(TEST_LOAD_AUTHOR);

        assertEquals(ENTITY_NB - LOADS_RESULTS, this.manager.countSQLite());
    }

    @Test
    public void testDeleteBookSQLite() {
        this.manager.deleteBookSQLite(TEST_LOAD_BOOK);

        assertEquals(ENTITY_NB - LOADS_RESULTS, this.manager.countSQLite());
    }

    @Test
    public void testImportFromMySQL() {
        this.initTestEntityMySQL();

        CategoryDBManager categoryDBManager = new CategoryDBManager(this.context);
        BookDBManager bookDBManager = new BookDBManager(this.context);
        AuthorDBManager authorDBManager = new AuthorDBManager(this.context);

        categoryDBManager.importFromMySQL(APIManager.API_URL + APIManager.CATEGORIES +
                APIManager.READ + CategoryDBSchema.ID + "=" + MYSQL_TEST_ID);
        categoryDBManager.waitForResponse();

        bookDBManager.importFromMySQL(APIManager.API_URL + APIManager.BOOKS +
                APIManager.READ + BookDBSchema.ID + "=" + MYSQL_TEST_ID);
        bookDBManager.waitForResponse();

        authorDBManager.importFromMySQL(APIManager.API_URL + APIManager.AUTHORS +
                APIManager.READ + AuthorDBSchema.ID + "=" + MYSQL_TEST_ID);
        authorDBManager.waitForResponse();

        this.manager.importFromMySQL(APIManager.API_URL + APIManager.WRITERS + APIManager.READ + BOOK + "=" +
                MYSQL_TEST_ID + "&" + AUTHOR + "=" + MYSQL_TEST_ID);
        this.manager.waitForResponse();

        assertEquals(ENTITY_NB + 1, this.manager.countSQLite());
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
     * Initializes a test writer into the MySQL database.
     * @param idAuthor The id of the associated author.
     * @param idBook The id of the associated book.
     */
    protected void initTestEntityMySQL(int idAuthor, int idBook) {
        this.testedMySQL = true;

        this.manager.createMySQL(idAuthor, idBook);
        this.manager.waitForResponse();
    }

    /**
     * Creates a test review according to the constants defined.
     */
    protected void initTestEntityMySQL() {
        new BookDBManagerTest().initTestEntityMySQL();
        new AuthorDBManagerTest().initTestEntityMySQL();
        this.initTestEntityMySQL(MYSQL_TEST_ID, MYSQL_TEST_ID);
    }

    /**
     * Deletes all the test entities from MySQL database.
     */
    protected void deleteMySQLTestEntities() {
        this.manager.deleteMySQLTestEntities();
        new BookDBManagerTest().deleteMySQLTestEntities();
        new AuthorDBManagerTest().deleteMySQLTestEntities();
    }
}