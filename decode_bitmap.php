<?php  

$dbhost = "localhost";
$dbuser = "root";
$dbpass = "";
$dbdb = "mobiledb1";
		
$b_id = $_POST['image_id'];    
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");

 
$query =mysql_query("SELECT bitmap , type , era , wiki_link , final_rating FROM pic WHERE pic.image_id='$b_id'");

$num = mysql_num_rows($query);


if($num == 1){

	while ($list=mysql_fetch_array($query)){
	
		$output=$list;
        echo json_encode($output);
		
	}
	
    mysql_close();
	}



?>
