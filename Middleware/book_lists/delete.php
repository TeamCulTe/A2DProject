<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:14
 */

require_once "common_header.php";

if (isset($_PUT[$idUser]) && isset($_PUT[$idBookListType]) && isset($_PUT[$idBook]) && isset($_PUT[$test]) &&
    $_PUT[$idUser] < 0 && $_PUT[$idBookListType] < 0 && $_PUT[$idBook] < 0) {
    $response_code = ($dbManager->delete($_PUT[$idUser], $_PUT[$idBookListType], $_PUT[$idBook])) ? 200 : 404;
} else if (isset($_PUT[$idUser]) && isset($_PUT[$test]) && $_PUT[$idUser] < 0) {
    $response_code = ($dbManager->deleteUserBookLists($_PUT[$idUser])) ? 200 : 404;
} else if (isset($_PUT[$idBookListType]) && isset($_PUT[$test]) && $_PUT[$idBookListType] < 0) {
    $response_code = ($dbManager->deleteTypedBookLists($_PUT[$idBookListType])) ? 200 : 404;
} else if (isset($_PUT[$idBook]) && isset($_PUT[$test]) && $_PUT[$idBook] < 0) {
    $response_code = ($dbManager->deleteBookBookLists($_PUT[$idBook])) ? 200 : 404;
} else if (isset($_PUT[$test])) {
    $dbManager->deleteTestEntities();
    $response_code = 200;
} else if (isset($_PUT[$idUser]) && isset($_PUT[$idBookListType]) && isset($_PUT[$idBook])) {
    $response_code = ($dbManager->softDeleteBookList($_PUT[$idUser], $_PUT[$idBookListType], $_PUT[$idBook])) ? 200 : 404;
} elseif (isset($_PUT[$idUser]) && isset($_PUT[$idBook])) {
    $response_code = ($dbManager->softDeleteUserBookListBook($_PUT[$idUser], $_PUT[$idBook])) ? 200 : 404;
} elseif (isset($_PUT[$idUser])) {
    $response_code = ($dbManager->softDeleteUserBookLists($_PUT[$idUser])) ? 200 : 404;
} elseif (isset($_PUT[$idBookListType])) {
    $response_code = ($dbManager->softDeleteTypedBookLists($_PUT[$idBookListType])) ? 200 : 404;
} elseif (isset($_PUT[$idBook])) {
    $response_code = ($dbManager->softDeleteBookBookLists($_PUT[$idBook])) ? 200 : 404;
} else {
    $response_code = 400;
}

http_response_code($response_code);