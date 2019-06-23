<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

require_once "../common_config.php";

$dbManager = new BookListTypeDbManager();

$id = BookListTypeDbManager::FIELDS[0];
$name = BookListTypeDbManager::FIELDS[1];
$image = BookListTypeDbManager::FIELDS[2];
$update = BookListTypeDbManager::FIELDS[3];