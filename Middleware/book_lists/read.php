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
} elseif (isset($_GET[$idUser]) && isset($_GET[$idBookListType])) {
    $response = $dbManager->getBookList($_GET[$idUser], $_GET[$idBookListType]);
} elseif (isset($_GET[$idUser])) {
    $response = $dbManager->getUserBookLists($_GET[$idUser]);
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