//
// Created by coley jeremy on 15/12/2018.
//

#include "Entity.h"

Entity::Entity() : id(0) {

}

Entity::Entity(const long &newId) : id(newId) {

}

const long &Entity::getId() const {
    return this->id;
}

void Entity::setId(const long &newId) {
    this->id = newId;
}
