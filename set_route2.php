<?php
error_reporting(~E_NOTICE);

include 'config.php';

$username = $_POST['username'];
$lat = $_POST['lat'];  
$lon = $_POST['lon'];         
		
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");


          

$query2=mysql_query("insert into pic (username,latitude,longitude)values('".$username."','".$lat."','".$lon."')");
$query3=mysql_query($query2);



$result1 =mysql_query("SELECT image_id FROM pic WHERE pic.username='$username' AND pic.latitude='$lat'");
$num = mysql_fetch_array($result1);



echo ($num['image_id']);

 

mysql_close($conn);
				
				 
?>