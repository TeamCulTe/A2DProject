<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$idQuote]))
{
    $response_code = ($dbManager->restoreSoftDeleted($_POST[$idQuote])) ? 200 : 404;
}
elseif (isset($_POST[$idUser]))
{
    $response_code = ($dbManager->restoreSoftDeletedUserQuotes($_POST[$idUser])) ? 200 : 404;
}
elseif (isset($_POST[$idBook]))
{
    $response_code = ($dbManager->restoreSoftDeletedBookQuotes($_POST[$idBook])) ? 200 : 404;
}
else
{
    $response_code = 400;
}

http_response_code($response_code);