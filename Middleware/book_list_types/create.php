<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$id]) && isset($_POST[$name]) && isset($_POST[$image])) {
    $response_code = ($dbManager->fullCreate($_POST[$id], $_POST[$name], $_POST[$image])) ? 201 : 404;
} else if (isset($_POST[$name]) && isset($_POST[$image])) {
    $createdId = $dbManager->create($_POST[$name], $_POST[$image]);

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