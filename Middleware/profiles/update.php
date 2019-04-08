<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_PUT[$id]) && isset($_PUT[$avatar]) && isset($_PUT[$description])) {
    $response_code = ($dbManager->update($_PUT[$id], $_PUT[$avatar], $_PUT[$description])) ? 200 : 404;
} elseif (isset($_PUT[$id]) && isset($_PUT[$avatar])) {
    $response_code = ($dbManager->updateAvatar($_PUT[$id], $_PUT[$avatar])) ? 200 : 404;
} elseif (isset($_PUT[$id]) && isset($_PUT[$description])) {
    $response_code = ($dbManager->updateDescription($_PUT[$id], $_PUT[$description])) ? 200 : 404;
} else {
    $response_code = 400;
}

http_response_code($response_code);