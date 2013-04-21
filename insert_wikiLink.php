<?php

include 'config.php';


try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	
	if($insertLink = $db->prepare("UPDATE pic SET pic.wiki_link=? , pic.era=?, pic.type=?  WHERE image_id=?")){
		$link=$_POST['wiki_link'];
		$imaid=$_POST['image_id'];
		$erap=$_POST['era'];
		$tip=$_POST['type'];
		
		
		$insertLink->bindValue(1, $link, PDO::PARAM_STR);
		$insertLink->bindValue(2, $erap, PDO::PARAM_STR);
		$insertLink->bindValue(3, $tip, PDO::PARAM_STR);
		$insertLink->bindValue(4, $imaid, PDO::PARAM_INT);
		$insertLink->execute();
	
	}
	else{
		echo "wrong";
	}
	
	
	
	}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;





				 
?>