//
// Created by coley jeremy on 16/12/2018.
//

#include "User.h"

User::User() : pseudo(""), password(""), email(""), country(""), city("") {
    Entity::Entity();
}

User::User(const std::string &newPseudo,
     const std::string &newPassword,
     const std::string &newEmail,
     const Profile &newProfile,
     const std::string &newCountry,
     const std::string &newCity,
     const std::map<const std::string, BookList> &newBookLists,
     const std::vector<Review> &newReviews) : pseudo(newPseudo),
                                              password(newPassword),
                                              email(newEmail),
                                              profile(newProfile),
                                              country(newCountry),
                                              city(newCity),
                                              bookLists(newBookLists),
                                              reviews(newReviews) {
    Entity::Entity();
}

User::User(const long &newId,
           const std::string &newPseudo,
           const std::string &newPassword,
           const std::string &newEmail,
           const Profile &newProfile,
           const std::string &newCountry,
           const std::string &newCity,
           const std::map<const std::string, BookList> &newBookLists,
           const std::vector<Review> &newReviews) : pseudo(newPseudo),
                                                    password(newPassword),
                                                    email(newEmail),
                                                    profile(newProfile),
                                                    country(newCountry),
                                                    city(newCity),
                                                    bookLists(newBookLists),
                                                    reviews(newReviews) {
    Entity::Entity(id);
}


const std::string &User::getPseudo() {
    return this->pseudo;
}

const std::string &User::getPassword() {
    return this->password;
}

const std::string &User::getEmail() {
    return this->email;
}

const Profile &User::getProfile() {
    return this->profile;
}

const std::string &User::getCountry() {
    return this->country;
}

const std::string &User::getCity() {
    return this->city;
}

const std::map<const std::string, BookList> &User::getBookLists() {
    return this->bookLists;
}

const std::vector<Review> &User::getReviews() {
    return this->reviews;
}


void User::setPseudo(const std::string &newPseudo) {
    this->pseudo = newPseudo;
}

void User::setPassword(const std::string &newPassword) {
    this->password = newPassword;
}

void User::setEmail(const std::string &newEmail) {
    this->email = newEmail;
}

void User::setProfile(const Profile &newProfile) {
    this->profile = newProfile;
}

void User::setCountry(const std::string &newCountry) {
    this->country = newCountry;
}

void User::setCity(const std::string &newCity) {
    this->city = newCity;
}

void User::setBookLists(const std::map<const std::string, BookList> &newBookLists) {
    this->bookLists = newBookLists;
}

void User::setReviews(const std::vector<Review> &newReviews) {
    this->reviews = newReviews;
}