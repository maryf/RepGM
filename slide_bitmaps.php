<?php  

include 'config.php';

$erareq=$_POST['era'];
		
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");

$query = "SELECT bitmap FROM pic WHERE pic.era='$erareq'";
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