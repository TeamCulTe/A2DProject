<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$idBook]))
{
    $response = $dbManager->getBook($_POST[$idBook]);
}
elseif (isset($_POST[$idCategory]))
{
    $response = $dbManager->getCategoryBooks($_POST[$idCategory]);
}
elseif (isset($_POST[$title]) && isset($_POST[$datePublished]))
{
    $response = $dbManager->getBookId($_POST[$title], $_POST[$datePublished]);
}
elseif (isset($_POST["start"]) && isset($_POST["end"]))
{
    $response = $dbManager->queryAllPaginated($_POST["start"], $_POST["end"]);
}
else
{
    $response = $dbManager->queryAll();
}

$response_code = ($response != null) ? 200 : 404;

echo $response;
http_response_code($response_code);