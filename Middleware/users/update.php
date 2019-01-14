<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_POST[$idUser]) && isset($_POST[$pseudo]) && isset($_POST[$password]) && isset($_POST[$email]) &&
    isset($_POST[$idProfile]) && isset($_POST[$idCity]) && isset($_POST[$idCountry]))
{
    $response_code = ($dbManager->update($_POST[$idUser], $_POST[$pseudo], $_POST[$password], $_POST[$email],
        $_POST[$idProfile], $_POST[$idCity], $_POST[$idCountry])) ? 201 : 404;
}
elseif (isset($_POST[$idUser]) && isset($_POST[$pseudo]))
{
    $response_code = ($dbManager->updatePseudo($_POST[$idUser], $_POST[$pseudo]));
}
elseif (isset($_POST[$idUser]) && isset($_POST[$password]))
{
    $response_code = ($dbManager->updatePassword($_POST[$idUser], $_POST[$password]));
}
elseif (isset($_POST[$idUser]) && isset($_POST[$email]))
{
    $response_code = ($dbManager->updateEmail($_POST[$idUser], $_POST[$email]));
}
elseif (isset($_POST[$idUser]) && isset($_POST[$idCity]))
{
    $response_code = ($dbManager->updateCity($_POST[$idUser], $_POST[$idCity]));
}
elseif (isset($_POST[$idUser]) && isset($_POST[$idCountry]))
{
    $response_code = ($dbManager->updateCountry($_POST[$idUser], $_POST[$idCountry]));
}
else
{
    $response_code = 400;
}

http_response_code($response_code);