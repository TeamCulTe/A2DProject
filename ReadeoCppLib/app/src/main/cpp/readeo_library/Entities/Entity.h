//
// Created by coley jeremy on 15/12/2018.
//

#ifndef READEOCPPLIB_ENTITY_H
#define READEOCPPLIB_ENTITY_H


class Entity {
public:
    Entity();
    Entity(int setId);
    Entity(int setId, bool setDeleted);

    int getId();
    bool getDeleted();

    void setId(int setId);
    void setDeleted(bool setDeleted);
private:
    int id;
    bool deleted;
};


#endif //READEOCPPLIB_ENTITY_H
