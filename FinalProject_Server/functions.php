<?php 
function connect(){
	$link = mysql_connect('lab1.fatcowmysql.com', 'chat', 'PASSWORD HERE'); 
	if (!$link) { 
		die('Could not connect: ' . mysql_error()); 
	} 
	mysql_select_db('chat'); 
}
function disconnect(){
	mysql_close();
}
function sendChat($from, $to, $text){
	connect();
	$result = mysql_query("INSERT INTO `chat`(`from`, `to`, `text`, `when`, `read`) VALUES ('$from','$to','$text',now(),'unread')") or die("Error while sending chat: ".mysql_error());
	return($result);
}
function recevieNewChat($from, $to){
	connect();
	$result = mysql_query("SELECT * FROM `chat` WHERE `from`='".$from."' and `to`='".$to."' and `read`='unread'") or die("Error while reading new chat: ".mysql_error());
	$output = "";
	while($row = mysql_fetch_array($result))
	{
		mysql_query("UPDATE `chat` SET `read`='read' WHERE id='".$row["id"]."'") or die("Error while updating the chat status to read: ".msyql_error());
		$output .= $row["text"]."\r\n";
	}
	return $output;
}
function uploadProfileImage($file_name, $data){
	if(file_exists($file_name)){
		$file_name = substr($file_name,0,stripos($file_name,'.'))."_".time().substr($file_name,strpos($file_name,'.'));
	}
	$fhandle=fopen($file_name, 'wb');
	fwrite($fhandle, $data);
	fclose($fhandle);
	return $file_name;
}
function setUserPicture($username,$image_path){
	connect();
	$result = mysql_query("SELECT * FROM `users` WHERE `username`='".$username."'") or die("Error searching for user: ".mysql_error());
	if(mysql_num_rows($result)<1){
		$result2 = mysql_query("INSERT INTO `users`(`username`, `profile_image`) VALUES ('$username','$image_path')") or die("Error while adding user's profile picture: ".mysql_error());
	}else{
		$result2 = mysql_query("UPDATE `users` set `profile_image`='".$image_path."' WHERE `username`='".$username."' ") or die("Error while updating user's profile picture: ".mysql_error());
	}
	return($result2);
}
function getProfileImage($username){
	connect();
	$result = mysql_query("SELECT * FROM `users` WHERE `username`='".$username."'") or die("Error searching for user: ".mysql_error());
	if(mysql_num_rows($result)<1){
		return "false";
	}else{
		$row = mysql_fetch_array($result);
		return $row["profile_image"];
	}
}
?>