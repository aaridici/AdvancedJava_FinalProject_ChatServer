<?php
include('functions.php');
if(isset($_GET)){
	if(isset($_GET["from"])&&isset($_GET["to"])&&isset($_GET["text"])){
		connect();
		$result = sendChat(mysql_real_escape_string($_GET["from"]),mysql_real_escape_string($_GET["to"]),mysql_real_escape_string($_GET["text"]));
		disconnect();
		echo($result);
	}else{
		echo("Missing GET");
	}
}else{
	echo("No GET");
}
?>