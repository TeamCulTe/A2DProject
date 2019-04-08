<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:14
 */

require_once "common_header.php";

if (isset($_PUT[$idAuthor]) && isset($_PUT[$idBook]) && isset($_PUT[$test]) && $_PUT[$idAuthor] < 0 && $_PUT[$idBook] < 0) {
    $response_code = ($dbManager->delete($_PUT[$idAuthor], $_PUT[$idBook])) ? 200 : 404;
} else if (isset($_PUT[$test])) {
    $dbManager->deleteTestEntities();
    $response_code = 200;
} else if (isset($_PUT[$idAuthor]) && isset($_PUT[$idBook])) {
    $response_code = ($dbManager->softDelete($_PUT[$idAuthor], $_PUT[$idBook])) ? 200 : 404;
} elseif (isset($_PUT[$idAuthor])) {
    $response_code = ($dbManager->softDeleteAuthor($_PUT[$idAuthor])) ? 200 : 404;
} elseif (isset($_PUT[$idBook])) {
    $response_code = ($dbManager->softDeleteBook($_PUT[$idBook])) ? 200 : 404;
} else {
    $response_code = 400;
}

http_response_code($response_code);