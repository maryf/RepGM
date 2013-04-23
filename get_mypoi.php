<?php
//session_start();

include 'config.php';

 

 try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	if ($getfav = $db->prepare("SELECT image_id FROM favorites WHERE username=?")){
	$username= $_POST['username'];
	$getfav->bindValue(1, $username, PDO::PARAM_STR);
	$getfav->execute();
	$posts1 = array();
	while ($list=$getfav->fetch()){
		if ($selpic = $db->prepare("SELECT latitude,longitude,type FROM pic WHERE image_id=?")){
			$imid=$list['image_id'];
			$selpic->bindValue(1, $imid, PDO::PARAM_INT);
			$selpic->execute();
			while($post=$selpic->fetch()){
				$posts1[] = array('post'=>$post);
			}
			
			
		}		
		else{
			echo "wrong";
			$selpic = null;
		}
	
	
	}
	header('Content-type: application/json');
	echo json_encode(array('posts'=>$posts1));
	}
	else{
	echo "wrong";
	$getfav = null;
	}
	
	
	
	
	}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
	}

$db = null;

?>