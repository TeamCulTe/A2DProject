<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:07
 */

/**
 * DAO Class BookListDbManager, used to manage book lists in database (CRUD).
 */
class BookListDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    const FIELDS = ["id_book", "id_user", "type", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    const PLACE_HOLDERS = [":idBook", ":idUser", ":type", ":deleted"];

    /**
     * Stores the associated table name.
     */
    const TABLE = "Category";
}