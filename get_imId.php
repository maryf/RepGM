<?php
//session_start();
include 'config.php';

 

try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	$getimid = $db->prepare("SELECT image_id FROM pic WHERE latitude=? ORDER BY image_id DESC");
	$lat = $_REQUEST['latitude'];
	$getimid->bindValue(1, $lat, PDO::PARAM_INT);
	$getimid->execute();
	$last=$getimid->fetch();
	echo $last['image_id'];
	
	
}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;
	


	
	
	
?>