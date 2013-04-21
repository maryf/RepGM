<?php
//session_start();

include 'config.php';

 

 try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	if ($getp = $db->prepare("SELECT path_id FROM path WHERE path.start_lat=?")){
	$lat = $_REQUEST['lat'];
	$getp->bindValue(1, $lat, PDO::PARAM_STR);
	$getp->execute();
	$list=$getp->fetch();
	}
	else{
	echo "wrong";
	$getp = null;
	}
	
	
	if ($getp2 = $db->prepare("SELECT lat,lon FROM route WHERE route.path_id=?")){
	$path_id=$list['path_id'];
	$getp2->bindValue(1, $path_id, PDO::PARAM_INT);
	$getp2->execute();
	
	
	$posts = array();
	while($post=$getp2->fetch()){
		$posts[] = array('post'=>$post);
	}
	header('Content-type: application/json');
	echo json_encode(array('posts'=>$posts));
	}
	else{
	echo "wrong";
	$getp2 = null;
	}
	
	}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
	}

$db = null;

?>