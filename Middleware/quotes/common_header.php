<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:17
 */

require_once "../common_config.php";

$dbManager = new QuoteDbManager();

$idQuote = QuoteDbManager::FIELDS[0];
$idUser = QuoteDbManager::FIELDS[1];
$idBook = QuoteDbManager::FIELDS[2];
$quote = QuoteDbManager::FIELDS[3];