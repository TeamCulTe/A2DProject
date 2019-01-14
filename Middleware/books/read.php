<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_GET[$idBook])) {
    $response = $dbManager->getBook($_GET[$idBook]);
} elseif (isset($_GET[$idCategory])) {
    $response = $dbManager->getCategoryBooks($_GET[$idCategory]);
} elseif (isset($_GET[$title]) && isset($_GET[$datePublished])) {
    $response = $dbManager->getBookId($_GET[$title], $_GET[$datePublished]);
} elseif (isset($_GET["start"]) && isset($_GET["end"])) {
    $response = $dbManager->queryAllPaginated($_GET["start"], $_GET["end"]);
} else {
    $response = $dbManager->queryAll();
}

$response_code = ($response != null) ? 200 : 404;

echo $response;
http_response_code($response_code);