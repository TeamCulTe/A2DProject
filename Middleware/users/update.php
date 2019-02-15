<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_PUT[$id]) && isset($_PUT[$pseudo]) && isset($_PUT[$password]) && isset($_PUT[$email]) &&
    isset($_PUT[$idProfile]) && isset($_PUT[$idCity]) && isset($_PUT[$idCountry])) {
    $response_code = ($dbManager->update($_PUT[$id], $_PUT[$pseudo], $_PUT[$password], $_PUT[$email],
        $_PUT[$idProfile], $_PUT[$idCity], $_PUT[$idCountry])) ? 201 : 404;
} elseif (isset($_PUT[$id]) && isset($_PUT[$pseudo])) {
    $response_code = ($dbManager->updatePseudo($_PUT[$id], $_PUT[$pseudo]));
} elseif (isset($_PUT[$id]) && isset($_PUT[$password])) {
    $response_code = ($dbManager->updatePassword($_PUT[$id], $_PUT[$password]));
} elseif (isset($_PUT[$id]) && isset($_PUT[$email])) {
    $response_code = ($dbManager->updateEmail($_PUT[$id], $_PUT[$email]));
} elseif (isset($_PUT[$id]) && isset($_PUT[$idCity])) {
    $response_code = ($dbManager->updateCity($_PUT[$id], $_PUT[$idCity]));
} elseif (isset($_PUT[$id]) && isset($_PUT[$idCountry])) {
    $response_code = ($dbManager->updateCountry($_PUT[$id], $_PUT[$idCountry]));
} else {
    $response_code = 400;
}

http_response_code($response_code);