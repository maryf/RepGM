<?php

include 'config.php';



try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	
	if ($base = $db->prepare("SELECT image_id FROM pic WHERE username=? AND pic.bitmap=?")){
		$usname=$_REQUEST['username'];
		$bit=$_REQUEST['bitmap'];
		$base->execute(array($usname, $bit));
		$count = $base->rowCount();
		if ($count==0){
	
			if($baseup = $db->prepare("UPDATE pic SET bitmap=? WHERE image_id=?")){
			$bit=$_REQUEST['bitmap'];
			$imagei=$_REQUEST['image_id'];

			$baseup->execute(array($bit, $imagei));
			echo "ok";
	
			}
			else{
			echo "wrong";
			}
	
		}
		
	else{
	echo "exists";
	}
	
	
}
} catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;




?>