<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 14:15
 */

abstract class DbManager
{
    protected $db;

    public function __construct(PDO $db)
    {
        $this->db = $db;
    }
}