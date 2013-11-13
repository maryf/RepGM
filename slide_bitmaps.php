<?php  

include 'config.php';
		

		
		
try {
    $db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	if ($slide = $db->prepare("SELECT bitmap FROM pic WHERE era=? ORDER BY final_rating DESC")){
	$erareq=$_POST['era']; 
	$slide->bindValue(1, $erareq, PDO::PARAM_STR);
	$slide->execute();
	$posts = array();
	while($post=$slide->fetch()){
		$posts[] = array('post'=>$post);
	}
	$selten=array_slice($posts,0,5);
	header('Content-type: application/json');
	echo json_encode(array('posts'=>$selten));
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