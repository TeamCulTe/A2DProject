<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:14
 */

require_once "common_header.php";

if (isset($_PUT[$idUser]) && isset($_PUT[$idBook]) && $_PUT[$idUser] < 0 && $_PUT[$idBook] < 0) {
    $response_code = ($dbManager->delete($_PUT[$idUser], $_PUT[$idBook])) ? 200 : 404;
} else if (isset($_PUT[$test])) {
    $dbManager->deleteTestEntities();
    $response_code = 200;
} else if (isset($_PUT[$idUser]) && isset($_PUT[$idBook]) && (isset($_PUT[$apiMasterKey]) || isset($_PUT[$apiKey]))) {
    $response_code = ($dbManager->softDelete($_PUT[$idUser], $_PUT[$idBook])) ? 200 : 404;
} elseif (isset($_PUT[$idUser]) && (isset($_PUT[$apiMasterKey]) || isset($_PUT[$apiKey]))) {
    $response_code = ($dbManager->softDeleteUserReviews($_PUT[$idUser])) ? 200 : 404;
} elseif (isset($_PUT[$idBook]) && (isset($_PUT[$apiMasterKey]) || (isset($_PUT[$apiKey]) && isset($_PUT[$idUser])))) {
    $response_code = ($dbManager->softDeleteBooksReviews($_PUT[$idBook])) ? 200 : 404;
} else {
    $response_code = 400;
}

http_response_code($response_code);