<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

require_once "../common_config.php";

$dbManager = new CountryDbManager();

$id = CountryDbManager::FIELDS[0];
$name = CountryDbManager::FIELDS[1];
$update = CountryDbManager::FIELDS[2];