<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

function autoload($nomClass)
{
    include "../lib/" . $nomClass . '.php';
}

spl_autoload_register('autoload');
header("Content-Type: application/json; charset=UTF-8");

$dbManager = new BookListTypeDbManager();

$id = BookListTypeDbManager::FIELDS[0];
$label = BookListTypeDbManager::FIELDS[1];