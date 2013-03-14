<?php

include 'config.php';

$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");


$b_id = $_POST['image_id']; 
$grade=$_POST['rating1'];


 $result =mysql_query("SELECT rating, num_rat FROM pic WHERE image_id='$b_id'");

 $row = mysql_fetch_array($result);
 
 $counter=$row['num_rat']+1;
 
 $sin_rating=$row['rating']+$grade;
 $output=$sin_rating/$counter;
 
 $k=round($output,1);

        echo json_encode($k);

$p=mysql_query("UPDATE pic SET final_rating='$k' , rating='$sin_rating',  num_rat='$counter'  WHERE image_id='$b_id'");
 
mysql_close($con);

?>