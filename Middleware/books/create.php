<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$id]) && isset($_POST[$idCategory]) && isset($_POST[$title]) && isset($_POST[$cover]) && isset($_POST[$summary]) &&
    isset($_POST[$datePublished])) {
        $response_code = ($dbManager->fullCreate($_POST[$id], $_POST[$idCategory], $_POST[$title], $_POST[$cover], $_POST[$summary],
            $_POST[$datePublished])) ? 201 : 404;
} else if (isset($_POST[$idCategory]) && isset($_POST[$title]) && isset($_POST[$cover]) && isset($_POST[$summary]) &&
    isset($_POST[$datePublished])) {
    $createdId = $dbManager->create($_POST[$idCategory], $_POST[$title], $_POST[$cover], $_POST[$summary],
        $_POST[$datePublished]);

    if ($createdId != "") {
        $response_code = 201;
    } else {
        $response_code = 404;
    }

    echo $createdId;
} else {
    $response_code = 400;
}

http_response_code($response_code);