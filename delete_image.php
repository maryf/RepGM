<?php
error_reporting(~E_NOTICE);
       
include 'config.php';		

try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);

			if ($select = $db->prepare("SELECT username,image_id FROM pic WHERE username=? AND image_id=?")){
				$imagid = $_POST['image_id'];
				$username= $_POST['username'];
				$select->bindValue(1, $username, PDO::PARAM_STR);
				$select->bindValue(2, $imagid, PDO::PARAM_INT);
				$select->execute();
				$usern=$select->fetch();
				if ($usern==true){
					if ($delete = $db->prepare("delete from pic WHERE image_id=? AND username=?")){
						$delete->bindValue(1, $imagid, PDO::PARAM_INT);	
						$delete->bindValue(2, $username, PDO::PARAM_STR);
						$delete->execute();
						}
					else{
						echo "wrong";
						$delete = null;
					}
					echo "Image Deleted";
				}
				else {
					echo "You can delete a picture only if you have upload it!";
				}
				
				
				
			}
			else{
				echo "wrong";
				$select = null;
			}
			
}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;



?>