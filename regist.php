<?php
error_reporting(~E_NOTICE);

include 'config.php';     

try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	
	if ($signin = $db->prepare("SELECT username FROM user1 WHERE username=?")){
	$username = $_POST['username'];
	$signin->execute(array($username));
	$log = $signin->fetch();
	//$json=json_encode($log);
	//echo $json;
	
	
	if ($log!=null)
	echo "already_exists";
	else {
	if ($reg = $db->prepare("INSERT INTO user1 (username,pass,email) values (?, ?,?)")){
	$pass = $_POST['pass'];
	$email = $_POST['email'];
	$reg->execute(array($username, $pass,$email));
	echo "ok";
	}
	else{
	echo "wrong";
	$reg = null;
	}
	}
	
	
    $signin = null;
	}
	else{
		echo "wrong";
		$signin = null;
	}
	
	
	
	
	
} catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;




?>