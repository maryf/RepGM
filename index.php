<?php
//session_start();

$username = $_REQUEST['username'];
$password = $_REQUEST['pass']; 

$con = mysql_connect("localhost","root","");
mysql_select_db("mobiledb1", $con);


$query = mysql_query("SELECT * FROM user1 WHERE username='$username' AND pass='$password'");
$num = mysql_num_rows($query);

if($num == 1){

	while ($list=mysql_fetch_array($query)){
	
		$output=$list;
        echo json_encode($output);
		
	}
	
    mysql_close();
	}
	
?>