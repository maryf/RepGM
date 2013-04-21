<?php
error_reporting(~E_NOTICE);
       
include 'config.php';		



		
try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	if ($geop = $db->prepare("insert into pic (username,latitude,longitude) values (?,?,?)")){
	$latitude = $_POST['latitude'];     
	$longitude = $_POST['longitude']; 
	$username= $_POST['username']; 
	$geop->bindValue(1, $username, PDO::PARAM_STR);
	$geop->bindValue(2, $latitude, PDO::PARAM_INT);
	$geop->bindValue(3, $longitude, PDO::PARAM_INT);
	$geop->execute();
	}
	else{
	echo "wrong";
	$geop = null;
	}
	
	
	if ($geop2 = $db->prepare("SELECT image_id FROM pic WHERE username=? AND latitude=?")){
	$username= $_POST['username']; 
	$latitude = $_POST['latitude'];
	$geop2->bindValue(1, $username, PDO::PARAM_STR);
	$geop2->bindValue(2, $latitude, PDO::PARAM_INT);
	$geop2->execute();
	$imid=$geop2->fetch();
	echo $imid['image_id'];
	}
	else{
	echo "wrong";
	$geop2 = null;
	}
	
	
}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;



?>