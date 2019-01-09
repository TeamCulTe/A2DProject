<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

require_once "../common_config.php";

$dbManager = new UserDbManager();

$idUser = UserDbManager::FIELDS[0];
$pseudo = UserDbManager::FIELDS[1];
$password = UserDbManager::FIELDS[2];
$email = UserDbManager::FIELDS[3];
$idProfile = UserDbManager::FIELDS[4];
$idCity = UserDbManager::FIELDS[5];
$idCountry = UserDbManager::FIELDS[6];
$publicMode = "public";