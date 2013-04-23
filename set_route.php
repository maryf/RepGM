<?php
error_reporting(~E_NOTICE);

include 'config.php';


try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	

	
	
	
	if ($setroute2 = $db->prepare("insert into pic (username,latitude,longitude)values(?,?,?)")){
		$username = $_POST['username'];
		$routelat = $_POST['lat'];
		$routelon = $_POST['lon']; 
		$setroute2->bindValue(1, $username, PDO::PARAM_STR);
		$setroute2->bindValue(2, $routelat, PDO::PARAM_INT);
		$setroute2->bindValue(3, $routelon, PDO::PARAM_INT);
		$setroute2->execute();
	}
	else{
	echo "wrong";
	$setroute2 = null;
	}
	
	
		if ($setroute = $db->prepare("insert into route (path_id,lat,lon)values(?,?,?)")){
		$pathid = $_POST['path_id'];       
		$routelat = $_POST['lat'];
		$routelon = $_POST['lon'];  
		$setroute->bindValue(1, $pathid, PDO::PARAM_INT);
		$setroute->bindValue(2, $routelat, PDO::PARAM_INT);
		$setroute->bindValue(3, $routelon, PDO::PARAM_INT);
		$setroute->execute();
	}
	else{
	echo "wrong";
	$setroute = null;
	}
	
	
	if ($setroute3 = $db->prepare("SELECT image_id FROM pic WHERE username=? AND latitude=? ORDER BY image_id DESC")){
		
		$setroute3->bindValue(1, $username, PDO::PARAM_STR);
		$setroute3->bindValue(2, $routelat, PDO::PARAM_INT);
		$setroute3->execute();
		$last=$setroute3->fetch();
		echo $last['image_id'];
	
	}
	else{
	echo "wrong";
	$getp2 = null;
	}
	
	
	}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}
		









?>