<?php  

include 'config.php';

try {
    $db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	if ($selpic = $db->prepare("SELECT latitude,longitude,type,image_id FROM pic")){
	$selpic->execute();
	$posts = array();
	while($post=$selpic->fetch()){
		$posts[] = array('post'=>$post);
	}
	header('Content-type: application/json');
	echo json_encode(array('posts'=>$posts));
	}
	else{
	echo "wrong";
	$selpic = null;
	}
} catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;
		




?>