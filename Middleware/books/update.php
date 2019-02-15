<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_PUT[$id]) && isset($_PUT[$idCategory]) && isset($_PUT[$title]) && isset($_PUT[$cover]) &&
    isset($_PUT[$summary]) && isset($_PUT[$datePublished])) {
    $response_code = ($dbManager->update($_PUT[$id], $_PUT[$idCategory], $_PUT[$title], $_PUT[$cover],
        $_PUT[$summary])) ? 201 : 404;
} else {
    $response_code = 400;
}

http_response_code($response_code);