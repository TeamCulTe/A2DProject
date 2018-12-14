<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$id]) && isset($_POST[$avatar]) && isset($_POST[$description]))
{
    $response_code = ($dbManager->update($_POST[$id], $_POST[$avatar], $_POST[$description])) ? 200 : 404;
}
elseif (isset($_POST[$id]) && isset($_POST[$avatar]))
{
    $response_code = ($dbManager->update($_POST[$id], $_POST[$avatar])) ? 200 : 404;
}
elseif (isset($_POST[$id]) && isset($_POST[$description]))
{
    $response_code = ($dbManager->update($_POST[$id], $_POST[$description])) ? 200 : 404;
}
else
{
    $response_code = 400;
}

http_response_code($response_code);