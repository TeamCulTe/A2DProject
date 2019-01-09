<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

require_once "../common_config.php";

$dbManager = new BookDbManager();

$idBook = BookDbManager::FIELDS[0];
$idCategory = BookDbManager::FIELDS[1];
$title = BookDbManager::FIELDS[2];
$cover = BookDbManager::FIELDS[3];
$summary = BookDbManager::FIELDS[4];
$datePublished = BookDbManager::FIELDS[5];