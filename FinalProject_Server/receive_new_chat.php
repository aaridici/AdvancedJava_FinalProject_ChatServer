<?php
include('functions.php');
if(isset($_GET)){
	if(isset($_GET["from"])&&isset($_GET["to"])){
		connect();
		$result = stripslashes(recevieNewChat(mysql_real_escape_string($_GET["from"]),mysql_real_escape_string($_GET["to"])));
		disconnect();
		echo($result);
	}else{
		echo("Missing GET");
	}
}else{
	echo("No GET");
}
?>