package com.imie.a2dev.teamculte.readeo.Utils;

import com.imie.a2dev.teamculte.readeo.Providers.AuthorContentProvider;
import com.imie.a2dev.teamculte.readeo.Providers.BookContentProvider;
import com.imie.a2dev.teamculte.readeo.Providers.BookListTypeContentProvider;
import com.imie.a2dev.teamculte.readeo.Providers.CategoryContentProvider;
import com.imie.a2dev.teamculte.readeo.Providers.CityContentProvider;
import com.imie.a2dev.teamculte.readeo.Providers.CountryContentProvider;
import com.imie.a2dev.teamculte.readeo.Providers.ProfileContentProvider;
import com.imie.a2dev.teamculte.readeo.Providers.QuoteContentProvider;
import com.imie.a2dev.teamculte.readeo.Providers.ReadeoContentProvider;
import com.imie.a2dev.teamculte.readeo.Providers.ReviewContentProvider;
import com.imie.a2dev.teamculte.readeo.Providers.UserContentProvider;
import com.imie.a2dev.teamculte.readeo.Providers.WriterContentProvider;

/**
 * Factory class used to return a desired instance of ReadeoContentProvider (depending on the desired entities).
 */
public abstract class ProviderFactory {
    /**
     * Returns the desired ReadeoContentProvider.
     * @param type The type in order to initializes the content provider.s
     * @return The generated and initialized ReadeoContentProvider.
     */
    public static ReadeoContentProvider getProvider(EntityType type) {
        ReadeoContentProvider provider;
        
        switch (type) {
            case AUTHOR:
                provider = new AuthorContentProvider();
                
                break;
            case BOOK:
                provider = new BookContentProvider();

                break;
            case BOOK_LIST_TYPE:
                provider = new BookListTypeContentProvider();

                break;
            case CATEGORY:
                provider = new CategoryContentProvider();

                break;
            case CITY:
                provider = new CityContentProvider();

                break;
            case COUNTRY:
                provider = new CountryContentProvider();

                break;
            case PROFILE:
                provider = new ProfileContentProvider();

                break;
            case QUOTE:
                provider = new QuoteContentProvider();

                break;
            case REVIEW:
                provider = new ReviewContentProvider();

                break;
            case USER:
                provider = new UserContentProvider();

                break;
            case WRITER:
                provider = new WriterContentProvider();
                
                break;
            default:
                provider = null;
        }

        return provider;
    }
}
