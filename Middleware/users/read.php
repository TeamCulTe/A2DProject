<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_GET[$email]) && isset($_GET[$publicMode])) {
    if ($_GET[$publicMode] == true) {
        $response = $dbManager->getPublicUserFromEmail($_GET[$email]);
    } elseif ($_GET[$publicMode] == false) {
        $response = $dbManager->getUserFromEmail($_GET[$email]);
    }
} elseif (isset($_GET[$idUser])) {
    $response = $dbManager->getUser($_GET[$idUser]);
} elseif (isset($_GET[$pseudo]) && isset($_GET[$publicMode])) {
    if ($_GET[$publicMode] == true) {
        $response = $dbManager->getPublicUserFromPseudo($_GET[$pseudo]);
    } elseif ($_GET[$publicMode] == false) {
        $response = $dbManager->getUserFromPseudo($_GET[$pseudo]);
    }
} elseif (isset($_GET[$email]) && isset($_GET[$password])) {
    $response = $dbManager->getUserFromAuth($_GET[$email], $_GET[$password]);
} elseif (isset($_GET[$pseudo])) {
    $response = $dbManager->getUserId($_GET[$pseudo]);
} elseif (isset($_GET[$count])) {
    $response = $dbManager->count();
} elseif (isset($_GET[$start]) && isset($_GET[$end]) && isset($_GET[$publicMode])) {
    $response = $dbManager->queryAllPublicPaginated($_GET[$start], $_GET[$end]);
} elseif (isset($_GET[$publicMode]) && $_GET[$publicMode] == true) {
    $response = $dbManager->queryAllPublic();
} elseif (isset($_GET[$start]) && isset($_GET[$end])) {
    $response = $dbManager->queryAllPaginated($_GET[$start], $_GET[$end]);
} else {
    $response = $dbManager->queryAll();
}

$response_code = ($response != null) ? 200 : 404;

echo $response;
http_response_code($response_code);