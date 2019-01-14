<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

require_once "../common_config.php";

$dbManager = new CityDbManager();

$id = CityDbManager::FIELDS[0];
$name = CityDbManager::FIELDS[1];