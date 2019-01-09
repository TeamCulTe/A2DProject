<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

require_once "../common_config.php";

$dbManager = new ReviewDbManager();

$idUser = ReviewDbManager::FIELDS[0];
$idBook = ReviewDbManager::FIELDS[1];
$review = ReviewDbManager::FIELDS[2];
$shared = ReviewDbManager::FIELDS[3];