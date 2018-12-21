//
// Created by coley jeremy on 16/12/2018.
//

#ifndef READEOCPPLIB_USER_H
#define READEOCPPLIB_USER_H


#include "Entity.h"
#include "Profile.h"
#include "BookList.h"
#include <string>
#include <map>

/**
 * Entity class representing a user of the application.
 */
class User : Entity {
public :
    /**
     * User's default constructor.
     */
    User();

    /**
     * User's nearly full filled constructor.
     */
    User(const std::string &newPseudo,
         const std::string &newPassword,
         const std::string &newEmail,
         const Profile &newProfile,
         const std::string &newCountry,
         const std::string &newCity,
         const std::map<const std::string, BookList> &newBookLists,
         const std::vector<Review> &newReviews);

    /**
     * User's full filled constructor.
     */
    User(const long &id,
         const std::string &newPseudo,
         const std::string &newPassword,
         const std::string &newEmail,
         const Profile &newProfile,
         const std::string &newCountry,
         const std::string &newCity,
         const std::map<const std::string, BookList> &newBookLists,
         const std::vector<Review> &newReviews);

    /**
     * Gets the value of the pseudo attribute.
     * @return The string value of the attribute.
     */
    const std::string &getPseudo();

    /**
     * Gets the value of the password attribute.
     * @return The string value of the attribute.
     */
    const std::string &getPassword();

    /**
     * Gets the value of the email attribute.
     * @return The string value of the attribute.
     */
    const std::string &getEmail();

    /**
     * Gets the value of the profile attribute.
     * @return The Profile value of the attribute.
     */
    const Profile &getProfile();

    /**
     * Gets the value of the country attribute.
     * @return The string value of the attribute.
     */
    const std::string &getCountry();

    /**
     * Gets the value of the city attribute.
     * @return The string value of the attribute.
     */
    const std::string &getCity();

    /**
     * Gets the value of the bookLists attribute.
     * @return The map value of the attribute.
     */
    const std::map<const std::string, BookList> &getBookLists();

    /**
     * Gets the value of the reviews attribute.
     * @return The vector value of the attribute.
     */
    const std::vector<Review> &getReviews();

    /**
     * Sets the value of the pseudo attribute.
     * @param newPseudo The string to set.
     */
    void setPseudo(const std::string &newPseudo);

    /**
     * Sets the value of the password attribute.
     * @param newPassword The string to set.
     */
    void setPassword(const std::string &newPassword);

    /**
     * Sets the value of the email attribute.
     * @param newEmail The string to set.
     */
    void setEmail(const std::string &newEmail);

    /**
     * Sets the value of the profile attribute.
     * @param newProfile The Profile to set.
     */
    void setProfile(const Profile &newProfile);

    /**
     * Sets the value of the country attribute.
     * @param newEmail The string to set.
     */
    void setCountry(const std::string &newCountry);

    /**
     * Sets the value of the city attribute.
     * @param newEmail The string to set.
     */
    void setCity(const std::string &newCity);

    /**
     * Sets the value of the bookLists attribute.
     * @param newBookLists The map to set.
     */
    void setBookLists(const std::map<const std::string, BookList> &newBookLists);

    /**
     * Sets the value of the reviews attribute.
     * @param newReviews The vector to set.
     */
    void setReviews(const std::vector<Review> &newReviews);

private:
    /**
     * Stores the user's pseudo.
     */
    std::string pseudo;

    /**
     * Stores the user's password.
     */
    std::string password;

    /**
     * Stores the user's email address.
     */
    std::string email;

    /**
     * Stores the user's profile.
     */
    Profile profile;

    /**
     * Stores the user's country.
     */
    std::string country;

    /**
     * Stores the user's city.
     */
    std::string city;

    /**
     * Stores the user's book lists.
     */
    std::map<const std::string, BookList> bookLists;

    /**
     * Stores the user's reviews.
     * TODO: See if really needed.
     */
    std::vector<Review> reviews;
};


#endif //READEOCPPLIB_USER_H
