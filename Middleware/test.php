<?php

/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 15:30
 */

include "DbConnector.php";
include "AuthorDbManager.php";

$dbConnector = new DbConnector();
$a = new AuthorDbManager($dbConnector->getConnector());
$r = $a->getName(1);
var_dump($r);