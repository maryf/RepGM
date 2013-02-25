<?php

$dbhost = "localhost";
$dbuser = "root";
$dbpass = "";
$dbdb = "mobiledb1";
$link=$_POST['wiki_link'];
$imaid=$_POST['image_id'];
$erap=$_POST['era'];
$tip=$_POST['type'];
		
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
mysql_select_db($dbdb,$conn)or die("database selection error");



$result =mysql_query("UPDATE pic SET pic.wiki_link='$link' , pic.era='$erap', pic.type='$tip'  WHERE image_id='$imaid'");
$query1=mysql_fetch_array($result);

		
              	
				 
?>