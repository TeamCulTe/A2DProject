<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$idUser]) && isset($_POST[$idBookListType]) && isset($_POST[$idBook]))
{
    $response_code = ($dbManager->restoreSoftDeletedBookList($_POST[$idUser], $_POST[$idBookListType], $_POST[$idBook])) ? 200 : 404;
}
elseif (isset($_POST[$idUser]) && isset($_POST[$idBook]))
{
    $response_code = ($dbManager->restoreSoftDeletedUserBookListBook($_POST[$idUser], $_POST[$idBook])) ? 200 : 404;
}
elseif (isset($_POST[$idUser]))
{
    $response_code = ($dbManager->restoreSoftDeletedUserBookLists($_POST[$idUser])) ? 200 : 404;
}
elseif (isset($_POST[$idBookListType]))
{
    $response_code = ($dbManager->restoreSoftDeletedTypedBookLists($_POST[$idBookListType])) ? 200 : 404;
}
elseif (isset($_POST[$idBook]))
{
    $response_code = ($dbManager->restoreSoftDeletedBookBookLists($_POST[$idBook])) ? 200 : 404;
}
else
{
    $response_code = 400;
}

http_response_code($response_code);