<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

require_once "../common_config.php";

$dbManager = new BookListDbManager();

$idUser = BookListDbManager::FIELDS[0];
$idBookListType = BookListDbManager::FIELDS[1];
$idBook = BookListDbManager::FIELDS[2];