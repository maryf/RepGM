<?php
error_reporting(~E_NOTICE);
       
include 'config.php';		

$latitude = $_POST['lat'];     
$longitude = $_POST['long']; 
$username= $_POST['username']; 
$imaid=$_POST['image_id'];
		
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");


$result =mysql_query("UPDATE pic SET latitude='$latitude' , longitude='$longitude' WHERE image_id='$imaid'");
$query1=mysql_fetch_array($result);

		if ($query1!="")
         {echo ($query1);
              }
              	
				 
?>