<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

require_once "../common_config.php";

$dbManager = new WriterDbManager();

$idAuthor = WriterDbManager::FIELDS[0];
$idBook = WriterDbManager::FIELDS[1];
$update = WriterDbManager::FIELDS[3];