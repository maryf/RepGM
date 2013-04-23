<?php
error_reporting(~E_NOTICE);

include 'config.php';

try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	
	if ($getmypaths = $db->prepare("SELECT start_lat,start_lon FROM path WHERE username=?")){
	$user = $_POST['username']; 
	$getmypaths->bindValue(1, $user, PDO::PARAM_STR);
	$getmypaths->execute();
	
	
	$posts = array();
	while($post=$getmypaths->fetch()){
		$posts[] = array('post'=>$post);
	}
	header('Content-type: application/json');
	echo json_encode(array('posts'=>$posts));
	}
	else{
	echo "wrong";
	$getmypaths = null;
	}
	
	
	}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;

	


?>