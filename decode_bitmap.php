<?php  


include 'config.php';
		

   
try {		
	$db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	
	if ($decbit = $db->prepare("SELECT  type ,bitmap , era , wiki_link , final_rating FROM pic WHERE image_id=?")){
	$b_id = $_POST['image_id']; 
	$decbit->bindValue(1, $b_id, PDO::PARAM_INT);
	$decbit->execute();
	$list=$decbit->fetch();
	echo json_encode($list);
	}
	else{
	echo "wrong";
	$decbit = null;
	}
	
	}catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;


?>
