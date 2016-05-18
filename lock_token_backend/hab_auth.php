<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$servername = "188.166.44.160";
$username = "onmyway_admin";
$password = "";

try {
    $conn = new PDO("mysql:host=$servername;dbname=onmyway", $username, $password);
    // set the PDO error mode to exception
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    //echo "Connected successfully"; 
    }
catch(PDOException $e)
    {
    echo "Connection failed: " . $e->getMessage();
    }

    function generateRandomString($length = 10) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
    }

    function generateToken($conn){
        $token = generateRandomString(20);
        $timestamp = time () + 120;
        $sql = "INSERT INTO hba (token,end) VALUES (:token,:end)";
                $query = $conn->prepare($sql);
                $query->execute(array(  ':token'=>$token,
                                        ':end'=> $timestamp));
                $resultArray = array('MessageCode' => '200' , 'token' => $token);
                echo json_encode($resultArray);
    }

    function isValid($conn){
        $sql = 'SELECT * FROM hba WHERE token = :token';
            $stmt = $conn->prepare($sql);
            $stmt->execute(array(':token' => $_GET['token']));
            $resultQuery = $stmt->fetchAll(PDO::FETCH_ASSOC);
            if($resultQuery[0]['end']>=time()){
                echo '{"MessageCode":"200"}';
            }else{
                echo '{"MessageCode":"503"}';
            }
    }

    if(isset($_GET['action']) && function_exists($_GET['action'])) {
    $action = $_GET['action'];
    $action($conn);
    }
    $conn = null;

?>