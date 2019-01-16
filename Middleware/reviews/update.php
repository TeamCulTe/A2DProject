<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:13
 */

require_once "common_header.php";

if (isset($_PUT[$idUser]) && isset($_PUT[$idBook]) && isset($_PUT[$review])) {
    $response_code = ($dbManager->updateReview($_PUT[$idUser], $_PUT[$idBook], $_PUT[$review])) ? 200 : 404;
} elseif (isset($_PUT[$idUser]) && isset($_PUT[$idBook]) && isset($_PUT[$shared])) {
    $response_code = ($dbManager->updateReviewSharing($_PUT[$idUser], $_PUT[$idBook], $_PUT[$shared])) ? 200 : 404;
} else {
    $response_code = 400;
}

http_response_code($response_code);