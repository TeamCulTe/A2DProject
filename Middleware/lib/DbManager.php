<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:15
 */

require_once "DbConnector.php";

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
     * Defines the count alias.
     */
    protected const COUNT = "count";

    /**
     * DbManager default constructor.
     */
    public function __construct()
    {
        $connector = new DbConnector();
        $this->db = $connector->getConnector();
    }
}