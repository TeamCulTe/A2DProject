<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$idUser]) && isset($_POST[$idBook]))
{
    $response = $dbManager->getReview($_POST[$idUser], $_POST[$idBook]);
}
elseif (isset($_POST[$idUser]))
{
    $response = $dbManager->getUserReviews($_POST[$idUser]);
}
elseif (isset($_POST[$idBook]))
{
    $response = $dbManager->getBookReviews($_POST[$idBook]);
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