<?php

include 'config.php';

$usname=$_REQUEST['username'];
$bit=$_REQUEST['bitmap'];

$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");

$result2 =mysql_query("SELECT image_id FROM pic WHERE pic.username='$usname' AND pic.bitmap='$bit'");
$query2=mysql_fetch_array($result2);
if ($query2==""){
   $query=mysql_query("insert into pic (username,bitmap) values ('".$usname."','".$bit."')");


	$result =mysql_query("SELECT image_id FROM pic WHERE pic.username='$usname' AND pic.bitmap='$bit'");
	$query1=mysql_fetch_array($result);

	echo ($query1['image_id']);
}
if ($query2!=""){

echo "exists";}
	

?>