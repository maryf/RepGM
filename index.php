<?php

include 'config.php';

$username = $_REQUEST['username'];
$password = $_REQUEST['pass']; 
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");


$query = mysql_query("SELECT username,pass FROM user1 WHERE username='$username' AND pass='$password'");
$num = mysql_num_rows($query);

if($num == 1){

	while ($list=mysql_fetch_array($query)){
	
		$output=$list;
        echo json_encode($output);
		
	}
	
    mysql_close();
	}
	
?>