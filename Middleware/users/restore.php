<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$idUser]))
{
    $response_code = ($dbManager->restoreSoftDeleted($_POST[$idUser])) ? 200 : 404;
}
elseif (isset($_POST[$email]) && isset($_POST[$password])) {
    $response_code = ($dbManager->restoreSoftDeletedFromAuth($_POST[$email], $_POST[$password])) ? 200 : 404;
}
else
{
    $response_code = 400;
}

http_response_code($response_code);