<?php
error_reporting(~E_NOTICE);

include 'config.php';

$username = $_POST['username'];
$stlat = $_POST['start_lat'];  
$stlon = $_POST['start_lon'];         
		
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");


          

$query2=mysql_query("insert into pic (username,latitude,longitude)values('".$username."','".$stlat."','".$stlon."')");
$query3=mysql_query($query2);



$result1 =mysql_query("SELECT image_id FROM pic WHERE pic.username='$username' AND pic.latitude='$stlat'");



while ($list=mysql_fetch_array($result1)){
	$id=$list['image_id'];
		
        }
echo ($id);
 
mysql_close($conn);
				
				 
?>