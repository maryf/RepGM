<?php

include 'config.php';
$lat = $_REQUEST['latitude'];
 

 $conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");


$query = mysql_query("SELECT image_id FROM pic WHERE pic.latitude='$lat'");
					
$num = mysql_num_rows($query);

if($num == 1){

	while ($list=mysql_fetch_array($query)){
	
		$output=$list['image_id'];
        echo ($output);
		
	}
	
    mysql_close();
	}
	
?>