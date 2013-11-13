<?php  

//$dbhost = "localhost";
//$dbuser = "root";
//$dbpass = "";
//$dbdb = "mobiledb1";
include 'config.php';

$user = $_POST['username'];  
  
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");

mysql_select_db($dbdb,$conn)or die("database selection error");

 
$query =mysql_query("SELECT longitude, latitude FROM pic WHERE pic.username='$user'");


  
$posts = array();
if(mysql_num_rows($query)) {
      while($post = mysql_fetch_assoc($query)) {
      $posts[] = array('post'=>$post);
    }
  }
   
header('Content-type: application/json');
echo json_encode(array('posts'=>$posts));
  
 
mysql_close();

?>