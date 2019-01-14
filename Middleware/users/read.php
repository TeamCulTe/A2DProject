<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$email]) && isset($_POST[$publicMode]))
{
    if ($_POST[$publicMode] == true)
    {
        $response = $dbManager->getPublicUserFromEmail($_POST[$email]);
    }
    elseif ($_POST[$publicMode] == false)
    {
        $response = $dbManager->getUserFromEmail($_POST[$email]);
    }
}
elseif (isset($_POST[$idUser]))
{
    $response = $dbManager->getUser($_POST[$idUser]);
}
elseif (isset($_POST[$pseudo]) && isset($_POST[$publicMode]))
{
    if ($_POST[$publicMode] == true)
    {
        $response = $dbManager->getPublicUserFromPseudo($_POST[$pseudo]);
    }
    elseif ($_POST[$publicMode] == false)
    {
        $response = $dbManager->getUserFromPseudo($_POST[$pseudo]);
    }
}
elseif (isset($_POST[$email]) && isset($_POST[$password]))
{
    $response = $dbManager->getUserFromAuth($_POST[$email], $_POST[$password]);
}
elseif (isset($_POST[$pseudo]))
{
    $response = $dbManager->getUserId($_POST[$pseudo]);
}
elseif (isset($_POST["start"]) && isset($_POST["end"]) && isset($_POST[$publicMode]))
{
    $response = $dbManager->queryAllPublicPaginated($_POST["start"], $_POST["end"]);
}
elseif (isset($_POST[$publicMode]) && $_POST[$publicMode] == true)
{
    $response = $dbManager->queryAllPublic();
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