<?php
include('functions.php');
if(isset($_GET)){
	if(isset($_GET["username"])){
		connect();
		$result = getProfileImage(mysql_real_escape_string($_GET["username"]));
		disconnect();
		echo($result);
	}else{
		echo("Missing GET");
	}
}else{
	echo("No GET");
}
?>