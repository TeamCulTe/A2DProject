<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$name])) {
    $response_code = ($dbManager->create($_POST[$name])) ? 201 : 404;
} else {
    $response_code = 400;
}

http_response_code($response_code);