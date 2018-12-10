<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 11:19
 */

class DbConnector
{
    private $host;
    private $userName;
    private $password;
    private $dbName;
    private $port;

    public function __construct()
    {
        $this->host = "localhost";
        $this->userName = "root";
        $this->password = "root";
        $this->dbName = "Readeo";
        $this->port = "8889";
    }

    public functIon getConnector()
    {
        $connectStr = "mysql:dbname=" . $this->dbName . ";host=" . $this->host  . ";port=" . $this->port . ";charset=utf-8";

        try
        {
            $db = new PDO($connectStr, $this->userName, $this->password);

            return db;
        }
        catch (PDOException $error)
        {
            echo "An error occurred during the db connection.\n" . $error->getMessage();

            return null;
        }
    }
}