<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_GET[$updateQuery])) {
    $response = $dbManager->queryUpdateFields();
} elseif (isset($_GET[$above]) && isset($_GET[$id])) {
    $response = $dbManager->queryAbove(($_GET[$id]));
} elseif (isset($_GET[$new]) && isset($_GET[$update])) {
    $response = $dbManager->queryNewer(($_GET[$update]));
} elseif (isset($_GET[$id])) {
    $response = $dbManager->getAuthor($_GET[$id]);
} elseif (isset($_GET[$name])) {
    $response = $dbManager->getAuthorId($_GET[$name]);
} elseif (isset($_GET[$count])) {
    $response = $dbManager->count();
} elseif (isset($_GET[$start]) && isset($_GET[$end])) {
    $response = $dbManager->queryAllPaginated($_GET[$start], $_GET[$end]);
} else {
    $response = $dbManager->queryAll();
}

$response_code = ($response != null) ? 200 : 404;

echo $response;
http_response_code($response_code);