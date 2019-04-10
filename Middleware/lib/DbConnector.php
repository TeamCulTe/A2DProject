<?php
/**
 * Created by PhpStorm.
 * User: mahsyaj
 * Date: 10/12/2018
 * Time: 11:19
 */

/**
 * Class DbConnector, used to generate the connection object in order to use the database.
 */
class DbConnector
{
    /**
     * @var string Stores the mysql socket path.
     */
    private $socket;

    /**
     * @var string Stores the name of the host to connect.
     */
    private $host;

    /**
     * @var string Stores the name of the user trying to connect.
     */
    private $userName;

    /**
     * @var string Stores the password of the user.
     */
    private $password;

    /**
     * @var string Stores the name of the database.
     */
    private $dbName;

    /**
     * @var string Stores the port of the database.
     */
    private $port;

    /**
     * DbConnector constructor, initializes the attributes by default.
     */
    public function __construct()
    {
        //$this->socket = "/Applications/MAMP/tmp/mysql/mysql.sock";
        $this->host = "localhost";
        $this->userName = "root";
        $this->password = "root";
        $this->dbName = "Readeo";
        $this->port = "8889";
    }

    /**
     * Gets a mysql connector object (PDO).
     * @return null|PDO The connector if success else null.
     */
    public function getConnector()
    {
        $baseStr = "mysql:dbname=%s;port=%s;charset=utf8";

        if ($this->socket != null) {
            $baseStr .= ";unix_socket=" . $this->socket;
        } elseif ($this->host != null) {
            $baseStr .= ";host=" . $this->host;
        }

        $connectStr = sprintf($baseStr, $this->dbName, $this->port);

        try {
            $db = new PDO($connectStr, $this->userName, $this->password);

            $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);

            return $db;
        } catch (PDOException $error) {
            echo "An error occurred during the db connection.\n" . $error->getMessage();

            return null;
        }
    }
}