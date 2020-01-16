<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:14
 */

require_once "common_header.php";

if (isset($_PUT[$id]) && $_PUT[$id] < 0) {
    $response_code = ($dbManager->delete($_PUT[$id])) ? 200 : 404;
} else if (isset($_PUT[$test])) {
    $dbManager->deleteTestEntities();
    $response_code = 200;
} else if (isset($_PUT[$id]) && (isset($_PUT[$apiMasterKey]) || (isset($_PUT[$apiKey]) && isset($_PUT[$idUser])))) {
    $response_code = ($dbManager->softDelete($_PUT[$id])) ? 200 : 404;
} elseif (isset($_PUT[$idUser]) && (isset($_PUT[$apiMasterKey]) || isset($_PUT[$apiKey]))) {
    $response_code = ($dbManager->softDeleteUserQuotes($_PUT[$idUser])) ? 200 : 404;
} elseif (isset($_PUT[$idBook]) && (isset($_PUT[$apiMasterKey]) || isset($_PUT[$apiKey]) && isset($_PUT[$idUser]))) {
    $response_code = ($dbManager->softDeleteBooksQuotes($_PUT[$idBook])) ? 200 : 404;
} else {
    $response_code = 400;
}

http_response_code($response_code);