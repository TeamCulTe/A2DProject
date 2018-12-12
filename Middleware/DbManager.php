<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:15
 */

/**
 * Abstract class DbManager extended by all manager classes.
 */
abstract class DbManager
{
    /**
     * @var PDO Stores the connector object.
     */
    protected $db;

    /**
     * DbManager constructor.
     * @param PDO $db The connector to set.
     */
    public function __construct(PDO $db)
    {
        $this->db = $db;
    }
}