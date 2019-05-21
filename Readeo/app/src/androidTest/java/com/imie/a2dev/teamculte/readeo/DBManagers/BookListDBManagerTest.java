package com.imie.a2dev.teamculte.readeo.DBManagers;

import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Category;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.imie.a2dev.teamculte.readeo.DBManagers.DBManager.MYSQL_TEST_ID;
import static org.junit.Assert.*;

public final class BookListDBManagerTest extends CommonDBManagerTest {
    /**
     * Defines the default number of books for each types.
     */
    private final int TEST_BOOK_PER_TYPE = 3;

    /**
     * Defines the default number of different book list types.
     */
    private final int TEST_TYPES_NUMBER = 3;

    /**
     * Stores the associated manager used to interact with the database.
     */
    private BookListDBManager manager = new BookListDBManager(this.context);

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
    public void testCreateEntityMySQL() {
        this.testedMySQL = true;

        BookDBManagerTest bookDBManagerTest = new BookDBManagerTest();
        UserDBManagerTest userDBManagerTest = new UserDBManagerTest();
        BookListTypeDBManagerTest bookListTypeDBManagerTest = new BookListTypeDBManagerTest();
        List<Book> books = new ArrayList<>();
        PrivateUser user = userDBManagerTest.initTestEntityMySQL();
        BookListType type = bookListTypeDBManagerTest.initTestEntityMySQL();
        books.add(bookDBManagerTest.initTestEntityMySQL());

        this.manager.createMySQL(new BookList(user.getId(), type, books));
        this.manager.waitForResponse();

        BookList bookList = this.manager.loadMySQL(user.getId(), type.getId());

        assertNotNull(bookList);
        assertEquals(user.getId(), bookList.getId());
        assertEquals(type.getId(), bookList.getType().getId());
        assertEquals(books.get(0).getId(), bookList.getBooks().get(0).getId());
    }

    @Test
    public void testCreateDataMySQL() {
        this.testedMySQL = true;

        Book book = new BookDBManagerTest().initTestEntityMySQL();
        PrivateUser user = new UserDBManagerTest().initTestEntityMySQL();
        BookListType type = new BookListTypeDBManagerTest().initTestEntityMySQL();

        this.manager.createMySQL(user.getId(), book.getId(), type.getId());
        this.manager.waitForResponse();

        BookList bookList = this.manager.loadMySQL(user.getId(), type.getId());

        assertNotNull(bookList);
        assertEquals(user.getId(), bookList.getId());
        assertEquals(type.getId(), bookList.getType().getId());
        assertEquals(book.getId(), bookList.getBooks().get(0).getId());
    }

    @Test
    public void testLoadMySQL() {
        BookList bookList = this.initTestEntityMySQL();

        assertNotNull(bookList);
        assertEquals(MYSQL_TEST_ID, bookList.getId());
        assertEquals(MYSQL_TEST_ID, bookList.getType().getId());

        for (int i = 0; i < TEST_BOOK_PER_TYPE; i++) {
            assertEquals((MYSQL_TEST_ID - TEST_BOOK_PER_TYPE + 1) + i, bookList.getBooks().get(i).getId());
        }
    }

    @Test
    public void testDeleteMySQL() {
        BookList toDelete = this.initTestEntityMySQL();

        assertNotNull(toDelete);

        for (Book book : toDelete.getBooks()) {
            this.manager.deleteMySQL(toDelete.getId(), book.getId(), toDelete.getType().getId());
        }

        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(toDelete.getId(), toDelete.getType().getId()));
    }

    @Test
    public void testRestoreMySQL() {
        BookList toRestore = this.initTestEntityMySQL();

        assertNotNull(toRestore);

        for (Book book : toRestore.getBooks()) {
            this.manager.deleteMySQL(toRestore.getId(), book.getId(), toRestore.getType().getId());
        }

        this.manager.waitForResponse();

        assertNull(this.manager.loadMySQL(toRestore.getId(), toRestore.getType().getId()));

        for (Book book : toRestore.getBooks()) {
            this.manager.restoreMySQL(toRestore.getId(), book.getId(), toRestore.getType().getId());
        }

        this.manager.waitForResponse();

        assertNotNull(this.manager.loadMySQL(toRestore.getId(), toRestore.getType().getId()));
    }

    @Test
    public void testLoadUserMySQL() {
        Map<String, BookList> bookLists = this.initUserTestEntitiesMySQL();

        assertNotNull(bookLists);
        assertEquals(TEST_TYPES_NUMBER, bookLists.size());

        for (String key : bookLists.keySet()) {
            assertEquals(MYSQL_TEST_ID, bookLists.get(key).getId());
            assertEquals(TEST_BOOK_PER_TYPE, bookLists.get(key).getBooks().size());
        }
    }

    @Test
    public void testDeleteUserMySQL() {
        assertNotNull(this.initUserTestEntitiesMySQL());

        this.manager.deleteUserMySQL(MYSQL_TEST_ID);
        Map<String, BookList> bookLists = this.manager.loadUserMySQL(MYSQL_TEST_ID);

        assertNull(bookLists);
    }

    @Test
    public void testRestoreUserMySQL() {
        assertNotNull(this.initUserTestEntitiesMySQL());

        this.manager.deleteUserMySQL(MYSQL_TEST_ID);
        this.manager.waitForResponse();

        assertNull(this.manager.loadUserMySQL(MYSQL_TEST_ID));

        this.manager.restoreUserMySQL(MYSQL_TEST_ID);
        this.manager.waitForResponse();

        assertNotNull(this.manager.loadUserMySQL(MYSQL_TEST_ID));
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
     * Initializes a test book list into the MySQL database.
     * @param id The id of the book list.
     * @param type The type of the book list.
     * @param books The books who belong to the book list.
     * @return The created book list.
     */
    protected BookList initTestEntityMySQL(int id, BookListType type, List<Book> books) {
        this.testedMySQL = true;

        this.manager.createMySQL(new BookList(id, type, books));
        this.manager.waitForResponse();

        return this.manager.loadMySQL(id, type.getId());
    }

    /**
     * Initializes a test book list according to the constants defined.
     * @return The created book list.
     */
    protected BookList initTestEntityMySQL() {
        PrivateUser user = new UserDBManagerTest().initTestEntityMySQL();
        BookListType type = new BookListTypeDBManagerTest().initTestEntityMySQL();
        Category category = new CategoryDBManagerTest().initTestEntityMySQL();
        List<Book> books = new ArrayList<>();
        BookDBManagerTest bookDBManagerTest = new BookDBManagerTest();

        for (int i = 0; i < TEST_BOOK_PER_TYPE; i++) {
            int testId = MYSQL_TEST_ID - i;
            String testString = "testString" + String.valueOf(i);

            books.add(bookDBManagerTest.initTestEntityMySQL(testId, testString, testString, testString, 3000,
                    category));
        }

        return this.initTestEntityMySQL(user.getId(), type, books);
    }

    /**
     * Initializes test book lists associated to a user.
     * @return The created book lists.
     */
    protected Map<String, BookList> initUserTestEntitiesMySQL() {
        BookListType type;
        List<Book> books;

        BookDBManagerTest bookDBManagerTest = new BookDBManagerTest();
        PrivateUser user = new UserDBManagerTest().initTestEntityMySQL();
        Category category = new CategoryDBManagerTest().initTestEntityMySQL();

        for (int i = 0; i < TEST_TYPES_NUMBER; i++) {
            int testTypeId = MYSQL_TEST_ID - i;
            String testTypeName = "testString" + String.valueOf(i);
            type = new BookListTypeDBManagerTest().initTestEntityMySQL(testTypeId, testTypeName);

            books = new ArrayList<>();

            for (int j = 0; j < TEST_BOOK_PER_TYPE; j++) {
                int testId = testTypeId - j;
                String testString = testTypeName + String.valueOf(j);

                books.add(bookDBManagerTest.initTestEntityMySQL(testId, testString, testString, testString, 3000,
                        category));
            }

            this.initTestEntityMySQL(user.getId(), type, books);
        }

        return this.manager.loadUserMySQL(user.getId());
    }

    /**
     * Deletes all the test entities from MySQL database.
     */
    protected void deleteMySQLTestEntities() {
        this.manager.deleteMySQLTestEntities();
        new UserDBManagerTest().deleteMySQLTestEntities();
        new BookDBManagerTest().deleteMySQLTestEntities();
        new BookListTypeDBManagerTest().deleteMySQLTestEntities();
    }
}