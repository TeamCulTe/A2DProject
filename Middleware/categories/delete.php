<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 14/12/2018
 * Time: 09:14
 */

require_once "common_header.php";

if (isset($_POST[$id]))
{
    $response_code = ($dbManager->softDelete($_POST[$id])) ? 200 : 404;
}
else
{
    $response_code = 400;
}

http_response_code($response_code);