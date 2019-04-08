<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 07/01/2019
 * Time: 13:06
 */

// Initializes the error displaying.
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
ini_set('memory_limit', '1G');
error_reporting(E_ALL);

// Setting an autoload in order to load all classes from lib directory.
function autoload($nomClass)
{
    include "lib/" . $nomClass . '.php';
}

spl_autoload_register('autoload');

header("Content-Type: application/json; charset=UTF-8");

$end = "end";
$start = "start";
$count = "count";
$max = "max";
$above = "above";
$new = "new";
$updateQuery = "update_query";
$test = "test";

// Gets the parameters inside a $_PUT array if PUT method.
$_PUT = [];

if ($_SERVER['REQUEST_METHOD'] == 'PUT')
{
    parse_str(file_get_contents("php://input"), $_PUT);

    foreach ($_PUT as $key => $value)
    {
        unset($_PUT[$key]);

        $_PUT[str_replace('amp;', '', $key)] = $value;
    }

    $_REQUEST = array_merge($_REQUEST, $_PUT);
}

// Gets the parameters inside a $_DELETE array if DELETE method.
$_DELETE = [];

if ($_SERVER['REQUEST_METHOD'] == 'DELETE')
{
    parse_str(file_get_contents("php://input"), $_DELETE);

    foreach ($_DELETE as $key => $value)
    {
        unset($_DELETE[$key]);

        $_DELETE[str_replace('amp;', '', $key)] = $value;
    }

    $_REQUEST = array_merge($_REQUEST, $_DELETE);
}