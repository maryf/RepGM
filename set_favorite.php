<?php
error_reporting(~E_NOTICE);
       
include 'config.php';		

try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	if ($setfav2 = $db->prepare("SELECT * FROM favorites")){
		$setfav2->execute();
		$imagid = $_POST['image_id'];
		$username= $_POST['username'];
		$flag=false;
		while ($list=$setfav2->fetch()){
			if ($list['image_id']==$imagid && $list['username']==$username){
				$flag=true;
				break;
			}
		}
		if ($flag==false){
			if ($setfav1 = $db->prepare("SELECT username FROM pic WHERE image_id=?")){
				$setfav1->bindValue(1, $imagid, PDO::PARAM_INT);
				$setfav1->execute();
				$usern=$setfav1->fetch();
				if ($usern['username']==$username){
					echo "You have uploaded this picture, you can set as your favorite only pictures from other users";
				}
				else{
					if ($setfav = $db->prepare("insert into favorites (image_id,username) values (?,?)")){
						$setfav->bindValue(1, $imagid, PDO::PARAM_INT);	
						$setfav->bindValue(2, $username, PDO::PARAM_STR);
						$setfav->execute();
						echo "ok";
						}
					else{
						echo "wrong";
						$setfav = null;
					}
				}
			}
			else{
				echo "wrong";
				$setfav1 = null;
			}
		
		}
		else{
			echo "already favorite";
		}
	}
	else{
		echo "wrong";
		$setfav2 = null;
	}
	
	
	
	
	
	
	
}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;



?>