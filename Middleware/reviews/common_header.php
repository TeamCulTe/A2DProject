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

$dbManager = new ReviewDbManager();

$idUser = ReviewDbManager::FIELDS[0];
$idBook = ReviewDbManager::FIELDS[1];
$review = ReviewDbManager::FIELDS[2];
$shared = ReviewDbManager::FIELDS[3];