<?php
error_reporting(~E_NOTICE);

include 'config.php';

try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	
	if ($getr = $db->prepare("SELECT * FROM path")){
	$getr->execute();
	
	
	$posts = array();
	while($post=$getr->fetch()){
		$posts[] = array('post'=>$post);
	}
	header('Content-type: application/json');
	echo json_encode(array('posts'=>$posts));
	}
	else{
	echo "wrong";
	$getr = null;
	}
	
	
	}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;

	


?>