<?php
error_reporting(~E_NOTICE);

include 'config.php';

        
		
try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	
	
	
	
	if ($pathuser2 = $db->prepare("insert into pic (username,latitude,longitude)values(?,?,?)")){
	$username = $_POST['username'];
	$stlat = $_POST['start_lat'];  
	$stlon = $_POST['start_lon']; 
	$pathuser2->bindValue(1, $username, PDO::PARAM_STR);
	$pathuser2->bindValue(2, $stlat, PDO::PARAM_INT);
	$pathuser2->bindValue(3, $stlon, PDO::PARAM_INT);
	$pathuser2->execute();
	}
	else{
	echo "wrong";
	$pathuser2 = null;
	}
	
	
	if ($getp2 = $db->prepare("SELECT image_id FROM pic WHERE username=? AND latitude=? ORDER BY image_id DESC")){
	$username = $_POST['username'];
	$stlat = $_POST['start_lat'];  
	$getp2->bindValue(1, $username, PDO::PARAM_STR);
	$getp2->bindValue(2, $stlat, PDO::PARAM_INT);
	$getp2->execute();
	$last1=$getp2->fetch();
	
	
	}
	else{
	echo "wrong";
	$getp2 = null;
	}
	
	
	if ($pathuser = $db->prepare("insert into path (username,start_lat,start_lon)values(?,?,?)")){
	$username = $_POST['username'];
	$stlat = $_POST['start_lat'];  
	$stlon = $_POST['start_lon']; 
	$pathuser->bindValue(1, $username, PDO::PARAM_STR);
	$pathuser->bindValue(2, $stlat, PDO::PARAM_INT);
	$pathuser->bindValue(3, $stlon, PDO::PARAM_INT);
	$pathuser->execute();
	}
	else{
	echo "wrong";
	$pathuser = null;
	}
	
	
	if ($getp = $db->prepare("SELECT path_id FROM path WHERE path.username=? ORDER BY path_id DESC")){
	$getp->bindValue(1, $username, PDO::PARAM_STR);
	$getp->execute();
	$last=$getp->fetch();
	$posts1 = array('posts'=>$last,'posts1'=>$last1);
	echo json_encode($posts1);
	
	
	}
	else{
	echo "wrong";
	$getp = null;
	}
	
	
	}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}


          





?>