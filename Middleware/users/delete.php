<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:14
 */

require_once "common_header.php";

if (isset($_PUT[$id]) && isset($_PUT[$test]) && $_PUT[$id] < 0) {
    $response_code = ($dbManager->delete($_PUT[$id])) ? 200 : 404;
} else if (isset($_PUT[$test])) {
    $dbManager->deleteTestEntities();
    $response_code = 200;
} else if (isset($_PUT[$id])) {
    $response_code = ($dbManager->softDelete($_PUT[$id])) ? 200 : 404;
} elseif (isset($_PUT[$email]) && isset($_PUT[$password])) {
    $response_code = ($dbManager->softDeleteFromAuth($_PUT[$email], $_PUT[$password])) ? 200 : 404;
} else {
    $response_code = 400;
}
http_response_code($response_code);