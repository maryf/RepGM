<?php
error_reporting(~E_NOTICE);
        
session_start();
$dbhost = "localhost";
$dbuser = "root";
$dbpass = "";
$dbdb = "mobiledb1";
$username = $_POST['username'];       
		
$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die("connection error");
         
mysql_select_db($dbdb,$conn)or die("database selection error");


          
$query=mysql_query("insert into user1 (username,pass)values('".$username."','".$_POST['pass']."')");
$query1=mysql_query($query);
if ($query1="")
         {echo "unsuccessfull";
              }
              else            
			   {$list=mysql_fetch_array($query);
				 
                }
				
				 
?>