<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 07/01/2019
 * Time: 13:06
 */

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
ini_set('memory_limit','1G');
error_reporting(E_ALL);

function autoload($nomClass)
{
    include "lib/" . $nomClass . '.php';
}

spl_autoload_register('autoload');
header("Content-Type: application/json; charset=UTF-8");