<?php  

$dbhost = "localhost";
$dbuser = "root";
$dbpass = "";
$dbdb = "mobiledb1";

		
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");

$query = "SELECT * FROM pic";
$result = mysql_query($query);
  
$posts = array();
if(mysql_num_rows($result)) {
      while($post = mysql_fetch_assoc($result)) {
      $posts[] = array('post'=>$post);
    }
  }
   
header('Content-type: application/json');
echo json_encode(array('posts'=>$posts));
  
 
mysql_close($conn);

?>