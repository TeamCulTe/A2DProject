<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";


if (isset($_PUT[$idAuthor]) && isset($_PUT[$idBook])) {
    $response_code = ($dbManager->restoreSoftDeleted($_PUT[$idUser], $_PUT[$idBook])) ? 200 : 404;
} elseif (isset($_PUT[$idAuthor])) {
    $response_code = ($dbManager->restoreSoftDeletedAuthor($_PUT[$idAuthor])) ? 200 : 404;
} elseif (isset($_PUT[$idBook])) {
    $response_code = ($dbManager->restoreSoftDeletedBook($_PUT[$idBook])) ? 200 : 404;
} else {
    $response_code = 400;
}

http_response_code($response_code);