package com.imie.a2dev.teamculte.readeo.Utils;

import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.AuthorDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListTypeDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CategoryDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CityDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CountryDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ProfileDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.QuoteDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ReviewDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.WriterDBManager;

/**
 * Class used to hold all the manager classes.
 */
public final class ManagerHolderUtils {

    /**
     * Stores the instance (using singleton pattern).
     */
    private static ManagerHolderUtils instance;

    /**
     * Stores the AuthorDBManager.
     */
    private final AuthorDBManager authorDBManager;

    /**
     * Stores the BookDBManager.
     */
    private final BookDBManager bookDBManager;

    /**
     * Stores the BookListDBManager.
     */
    private final BookListDBManager bookListDBManager;

    /**
     * Stores the BookListTypeDBManager.
     */
    private final BookListTypeDBManager bookListTypeDBManager;

    /**
     * Stores the CategoryDBManager.
     */
    private final CategoryDBManager categoryDBManager;

    /**
     * Stores the CityDBManager.
     */
    private final CityDBManager cityDBManager;

    /**
     * Stores the CountryDBManager.
     */
    private final CountryDBManager countryDBManager;

    /**
     * Stores the ProfileDBManager.
     */
    private final ProfileDBManager profileDBManager;

    /**
     * Stores the QuoteDBManager.
     */
    private final QuoteDBManager quoteDBManager;

    /**
     * Stores the ReviewDBManager.
     */
    private final ReviewDBManager reviewDBManager;

    /**
     * Stores the UserDBManager.
     */
    private final UserDBManager userDBManager;

    /**
     * Stores the WriterDBManager.
     */
    private final WriterDBManager writerDBManager;

    /**
     * Default constructor.
     */
    private ManagerHolderUtils() {
        this.authorDBManager = new AuthorDBManager(App.getAppContext());
        this.bookDBManager = new BookDBManager(App.getAppContext());
        this.bookListDBManager = new BookListDBManager(App.getAppContext());
        this.bookListTypeDBManager = new BookListTypeDBManager(App.getAppContext());
        this.categoryDBManager = new CategoryDBManager(App.getAppContext());
        this.cityDBManager = new CityDBManager(App.getAppContext());
        this.countryDBManager = new CountryDBManager(App.getAppContext());
        this.profileDBManager = new ProfileDBManager(App.getAppContext());
        this.quoteDBManager = new QuoteDBManager(App.getAppContext());
        this.reviewDBManager = new ReviewDBManager(App.getAppContext());
        this.userDBManager = new UserDBManager(App.getAppContext());
        this.writerDBManager = new WriterDBManager(App.getAppContext());
    }

    /**
     * Singleton pattern method used to init and get the instance if not initialized else just get.
     * @return The ManagerHolderUtils instance.
     */
    public static ManagerHolderUtils getInstance() {
        if (ManagerHolderUtils.instance == null) {
            ManagerHolderUtils.instance = new ManagerHolderUtils();
        }

        return ManagerHolderUtils.instance;
    }

    /**
     * Gets the authorDBManager attribute.
     * @return The AuthorDBManager value of authorDBManager attribute.
     */
    public AuthorDBManager getAuthorDBManager() {
        return this.authorDBManager;
    }

    /**
     * Gets the bookDBManager attribute.
     * @return The BookDBManager value of bookDBManager attribute.
     */
    public BookDBManager getBookDBManager() {
        return this.bookDBManager;
    }

    /**
     * Gets the bookListDBManager attribute.
     * @return The BookListDBManager value of bookListDBManager attribute.
     */
    public BookListDBManager getBookListDBManager() {
        return this.bookListDBManager;
    }

    /**
     * Gets the bookListTypeDBManager attribute.
     * @return The BookListTypeDBManager value of bookListTypeDBManager attribute.
     */
    public BookListTypeDBManager getBookListTypeDBManager() {
        return this.bookListTypeDBManager;
    }

    /**
     * Gets the categoryDBManager attribute.
     * @return The CategoryDBManager value of categoryDBManager attribute.
     */
    public CategoryDBManager getCategoryDBManager() {
        return this.categoryDBManager;
    }

    /**
     * Gets the cityDBManager attribute.
     * @return The CityDBManager value of cityDBManager attribute.
     */
    public CityDBManager getCityDBManager() {
        return this.cityDBManager;
    }

    /**
     * Gets the countryDBManager attribute.
     * @return The CountryDBManager value of countryDBManager attribute.
     */
    public CountryDBManager getCountryDBManager() {
        return this.countryDBManager;
    }

    /**
     * Gets the profileDBManager attribute.
     * @return The ProfileDBManager value of profileDBManager attribute.
     */
    public ProfileDBManager getProfileDBManager() {
        return this.profileDBManager;
    }

    /**
     * Gets the quoteDBManager attribute.
     * @return The QuoteDBManager value of quoteDBManager attribute.
     */
    public QuoteDBManager getQuoteDBManager() {
        return this.quoteDBManager;
    }

    /**
     * Gets the reviewDBManager attribute.
     * @return The ReviewDBManager value of reviewDBManager attribute.
     */
    public ReviewDBManager getReviewDBManager() {
        return this.reviewDBManager;
    }

    /**
     * Gets the userDBManager attribute.
     * @return The UserDBManager value of userDBManager attribute.
     */
    public UserDBManager getUserDBManager() {
        return this.userDBManager;
    }

    /**
     * Gets the writerDBManager attribute.
     * @return The WriterDBManager value of writerDBManager attribute.
     */
    public WriterDBManager getWriterDBManager() {
        return this.writerDBManager;
    }

}
