<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 11:19
 */

class DbConnector
{
    private $socket;
    private $userName;
    private $password;
    private $dbName;
    private $port;

    public function __construct()
    {
        $this->socket = "/Applications/MAMP/tmp/mysql/mysql.sock";
        $this->userName = "root";
        $this->password = "root";
        $this->dbName = "Readeo";
        $this->port = "8889";
    }

    public functIon getConnector()
    {
        $connectStr = "mysql:dbname=" . $this->dbName . ";unix_socket=" . $this->socket  . ";port=" . $this->port . ";charset=utf8";

        try
        {
            $db = new PDO($connectStr, $this->userName, $this->password);

            return $db;
        }
        catch (PDOException $error)
        {
            echo "An error occurred during the db connection.\n" . $error->getMessage();

            return null;
        }
    }
}