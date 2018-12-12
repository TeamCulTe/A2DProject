<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 15:30
 */

include "DbConnector.php";
include "CategoryDbManager.php";

$dbConnector = new DbConnector();
$a = new CategoryDbManager($dbConnector->getConnector());
$r = $a->getCategory(7);
var_dump($r);
