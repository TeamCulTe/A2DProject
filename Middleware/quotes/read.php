<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_GET[$idUser]) && isset($_GET[$idBook])) {
    $response = $dbManager->getUserBookQuotes($_GET[$idUser], $_GET[$idBook]);
} elseif (isset($_GET[$idQuote])) {
    $response = $dbManager->getQuote($_GET[$idQuote]);
} elseif (isset($_GET[$idUser])) {
    $response = $dbManager->getUserQuotes($_GET[$idUser]);
} elseif (isset($_GET[$idBook])) {
    $response = $dbManager->getBookQuotes($_GET[$idBook]);
} elseif (isset($_GET["start"]) && isset($_GET["end"])) {
    $response = $dbManager->queryAllPaginated($_GET["start"], $_GET["end"]);
} else {
    $response = $dbManager->queryAll();
}

$response_code = ($response != null) ? 200 : 404;

echo $response;
http_response_code($response_code);