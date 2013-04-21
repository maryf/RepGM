<?php
error_reporting(~E_NOTICE);

include 'config.php';     

try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	
	if ($reg = $db->prepare("INSERT INTO user1 (username,pass) values (?, ?)")){
	$username = $_POST['username']; 
	$pass = $_POST['pass'];
	$reg->execute(array($username, $pass));
	echo "ok";
	}
	else{
	echo "wrong";
	$reg = null;
	}
	
	
} catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;




?>