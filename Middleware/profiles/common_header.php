<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

require_once "../common_config.php";

$dbManager = new ProfileDbManager();

$id = ProfileDbManager::FIELDS[0];
$avatar = ProfileDbManager::FIELDS[1];
$description = ProfileDbManager::FIELDS[2];