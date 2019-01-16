<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (!isset($_GET[$shared])) {
    $_GET[$shared] = false;
}

if (isset($_GET[$idUser]) && isset($_GET[$idBook])) {
    if ($_GET[$shared]) {
        $response = $dbManager->getSharedReview($_GET[$idUser], $_GET[$idBook]);
    } else {
        $response = $dbManager->getReview($_GET[$idUser], $_GET[$idBook]);
    }

} elseif (isset($_GET[$idUser])) {
    if ($_GET[$shared]) {
        $response = $dbManager->getUserSharedReviews($_GET[$idUser]);
    } else {
        $response = $dbManager->getUserReviews($_GET[$idUser]);
    }

} elseif (isset($_GET[$idBook])) {
    if ($_GET[$shared]) {
        $response = $dbManager->getBookSharedReviews($_GET[$idBook]);
    } else {
        $response = $dbManager->getBookReviews($_GET[$idBook]);
    }

} elseif (isset($_GET[$count])) {
    if ($_GET[$shared]) {
        $response = $dbManager->countShared();
    } else {
        $response = $dbManager->count();
    }

} elseif (isset($_GET[$start]) && isset($_GET[$end])) {
    if ($_GET[$shared]) {
        $response = $dbManager->queryAllSharedPaginated($_GET[$start], $_GET[$end]);
    } else {
        $response = $dbManager->queryAllPaginated($_GET[$start], $_GET[$end]);
    }

} elseif (isset($_GET[$shared])) {
    $response = $dbManager->queryAllShared();
} else {
    $response = $dbManager->queryAll();
}

$response_code = ($response != null) ? 200 : 404;

echo $response;
http_response_code($response_code);