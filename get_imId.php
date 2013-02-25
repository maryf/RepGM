<?php
//session_start();

$lat = $_REQUEST['latitude'];
 

$con = mysql_connect("localhost","root","");
mysql_select_db("mobiledb1", $con);


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