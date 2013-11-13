<?php
//session_start();

include 'config.php';

try {
    $db = new PDO('mysql:host=localhost;dbname=mobiledb1', $dbuser, $dbpass);
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	
	if ($signin = $db->prepare("SELECT username,pass FROM user1 WHERE username=? AND pass=?")){
	$username = $_REQUEST['username'];
	$password = $_REQUEST['pass']; 
	$signin->execute(array($username, $password));
	$log = $signin->fetch();
	//$json=json_encode($log);
	//echo $json;
	
	
	if ($log['username']==$username && $log['pass']==$password)
	echo "success";
	else 
	echo "fail";
	
	
    $signin = null;
	}
	else{
		echo "wrong";
		$signin = null;
	}
} catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}

$db = null;







	

?>