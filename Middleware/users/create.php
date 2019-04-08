<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$id]) && isset($_POST[$pseudo]) && isset($_POST[$password]) && isset($_POST[$email]) &&
    isset($_POST[$idProfile]) && isset($_POST[$idCity]) && isset($_POST[$idCountry])) {
    $response_code = ($dbManager->fullCreate($_POST[$id], $_POST[$pseudo], $_POST[$password], $_POST[$email],
        $_POST[$idProfile], $_POST[$idCity], $_POST[$idCountry])) ? 201 : 404;
} else if (isset($_POST[$pseudo]) && isset($_POST[$password]) && isset($_POST[$email]) &&
    isset($_POST[$idProfile]) && isset($_POST[$idCity]) && isset($_POST[$idCountry])) {
    $createdId = $dbManager->create($_POST[$pseudo], $_POST[$password], $_POST[$email], $_POST[$idProfile],
        $_POST[$idCity], $_POST[$idCountry]);

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