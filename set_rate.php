<?php

include 'config.php';

try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	
	if ($setrate = $db->prepare("SELECT rating, num_rat FROM pic WHERE image_id=?")){
		$b_id = $_POST['image_id']; 
		$setrate->bindValue(1, $b_id, PDO::PARAM_INT);
		$setrate->execute();
		$row=$setrate->fetch();
		$counter=$row['num_rat']+1;
		$grade=$_POST['rating1'];
		$sin_rating=$row['rating']+$grade;
		$output=$sin_rating/$counter;
 
		$k=round($output,1);

        echo $k;
	}	
	else{
		echo "wrong";
		$setrate=null;
	}
	
	
	if($rate = $db->prepare("UPDATE pic SET final_rating='$k' , rating='$sin_rating',  num_rat='$counter'  WHERE image_id=?")){
		$rate->bindValue(1, $b_id, PDO::PARAM_INT);
		$rate->execute();
		echo "trexei";
		
	
			}
	else{
		echo "wrong";
		$rate=null;
	}
	
		
		
	
	
	
} catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;





?>