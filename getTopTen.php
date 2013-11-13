<?php
//session_start();

include 'config.php';

 

 try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
		if ($getTop = $db->prepare("SELECT * FROM pic ORDER BY final_rating DESC ")){
	$getTop->execute();
	$posts1 = array();
	while($post=$getTop->fetch()){
				$posts1[] = array('post'=>$post);
			}
	//$list=$getTop->fetch();
	$output = array_slice($posts1, 0, 5);
	header('Content-type: application/json');
	echo json_encode(array('posts'=>$output));
	}
	else{
	echo "wrong";
	$getTop = null;
	}
	
	
	
	
	}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
	}

$db = null;

?>