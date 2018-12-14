<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$idBook]) && isset($_POST[$idCategory]) && isset($_POST[$title]) && isset($_POST[$cover]) &&
    isset($_POST[$summary]) && isset($_POST[$datePublished]))
{
    $response_code = ($dbManager->update($_POST[$idBook], $_POST[$idCategory], $_POST[$title], $_POST[$cover],
        $_POST[$summary])) ? 201 : 404;
}
else
{
    $response_code = 400;
}

http_response_code($response_code);