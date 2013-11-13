<?php  

include 'config.php';

$typ=$_POST['type'];
		
$conn = mysql_connect("localhost", $dbuser, $dbpass) or die("connection error");
         
mysql_select_db("mobiledb1",$conn)or die("database selection error");

$query = "SELECT image_id, username, type, bitmap  FROM pic WHERE pic.type='palace'";
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