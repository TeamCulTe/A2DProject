<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:08
 */

/**
 * DAO Class CategoryDbManager, used to manage categories in database (CRUD).
 */
class CategoryDbManager extends DbManager
{
    /**
     * Stores the associated database fields.
     */
    const FIELDS = ["id_category", "name", "deleted"];

    /**
     * Stores the placeholders for prepared queries.
     */
    const PLACE_HOLDERS = [":idCategory", ":name", ":deleted"];

    /**
     * Stores the associated table name.
     */
    const TABLE = "Category";
}