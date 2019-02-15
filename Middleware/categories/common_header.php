<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

require_once "../common_config.php";

$dbManager = new CategoryDbManager();

$id = CategoryDbManager::FIELDS[0];
$name = CategoryDbManager::FIELDS[1];
$update = CategoryDbManager::FIELDS[2];