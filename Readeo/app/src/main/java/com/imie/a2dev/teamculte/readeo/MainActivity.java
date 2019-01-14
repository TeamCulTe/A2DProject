package com.imie.a2dev.teamculte.readeo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.RequestQueue;
import com.imie.a2dev.teamculte.readeo.DBManagers.AuthorDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListTypeDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CategoryDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CityDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CountryDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ProfileDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.QuoteDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ReviewDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.WriterDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Category;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.City;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Country;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Profile;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Quote;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Review;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HTTPRequestQueueSingleton.HTTPRequestQueueListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.test();
    }

    private void test() {
        AuthorDBManager authorDBManager = new AuthorDBManager(this);
        BookDBManager bookDBManager = new BookDBManager(this);
        BookListDBManager bookListDBManager = new BookListDBManager(this);
        BookListTypeDBManager bookListTypeDBManager = new BookListTypeDBManager(this);
        CategoryDBManager categoryDBManager = new CategoryDBManager(this);
        CityDBManager cityDBManager = new CityDBManager(this);
        CountryDBManager countryDBManager = new CountryDBManager(this);
        ProfileDBManager profileDBManager = new ProfileDBManager(this);
        QuoteDBManager quoteDBManager = new QuoteDBManager(this);
        ReviewDBManager reviewDBManager = new ReviewDBManager(this);
        UserDBManager userDBManager = new UserDBManager(this);
        WriterDBManager writerDBManager = new WriterDBManager(this);

        Author author = new Author();
        Book book = new Book();
        BookList bookList = new BookList();
        BookListType bookListType = new BookListType();
        Category category = new Category();
        City city = new City();
        Country country = new Country();
        PrivateUser privateUser = new PrivateUser();
        Profile profile = new Profile();
        PublicUser publicUser = new PrivateUser();
        Quote quote = new Quote();
        Review review = new Review();

        DBManager.importMySQLDatabase();

//        List<Author> authors = authorDBManager.queryAllSQLite();
//        List<Category> categories = categoryDBManager.queryAllSQLite();
//        List<City> cities = cityDBManager.queryAllSQLite();
//        List<Country> countries = countryDBManager.queryAllSQLite();
//        List<Profile> profiles = profileDBManager.queryAllSQLite();
//        List<Book> books = bookDBManager.queryAllSQLite();
//        List<BookListType> bookListTypes = bookListTypeDBManager.queryAllSQLite();
//        List<PublicUser> publicUsers = userDBManager.queryAllSQLite();
//        List<Quote> quotes = quoteDBManager.queryAllSQLite();
//        List<Review> reviews = reviewDBManager.queryAllSQLite();

        HTTPRequestQueueSingleton r = HTTPRequestQueueSingleton.getInstance(this);
        r.setListener(this);
    }

    @Override
    public void onRequestsFinished() {
        AuthorDBManager authorDBManager = new AuthorDBManager(this);
        BookDBManager bookDBManager = new BookDBManager(this);
        BookListDBManager bookListDBManager = new BookListDBManager(this);
        BookListTypeDBManager bookListTypeDBManager = new BookListTypeDBManager(this);
        CategoryDBManager categoryDBManager = new CategoryDBManager(this);
        CityDBManager cityDBManager = new CityDBManager(this);
        CountryDBManager countryDBManager = new CountryDBManager(this);
        ProfileDBManager profileDBManager = new ProfileDBManager(this);
        QuoteDBManager quoteDBManager = new QuoteDBManager(this);
        ReviewDBManager reviewDBManager = new ReviewDBManager(this);
        UserDBManager userDBManager = new UserDBManager(this);
        WriterDBManager writerDBManager = new WriterDBManager(this);

        List<Author> authors = authorDBManager.queryAllSQLite();
        List<Category> categories = categoryDBManager.queryAllSQLite();
        List<City> cities = cityDBManager.queryAllSQLite();
        List<Country> countries = countryDBManager.queryAllSQLite();
        List<Profile> profiles = profileDBManager.queryAllSQLite();
        List<Book> books = bookDBManager.queryAllSQLite();
        List<BookListType> bookListTypes = bookListTypeDBManager.queryAllSQLite();
        List<PublicUser> publicUsers = userDBManager.queryAllSQLite();
        List<Quote> quotes = quoteDBManager.queryAllSQLite();
        List<Review> reviews = reviewDBManager.queryAllSQLite();
    }
}

