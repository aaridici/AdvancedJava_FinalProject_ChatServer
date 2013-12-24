<?php
include('functions.php');
if(isset($_GET)){
	if(isset($_GET["file_name"])){
		$file_name="uploads/".$_GET["file_name"];
		$file_data=file_get_contents('php://input');
		if($file_data!=NULL){
			echo("Received file name:".$filename."\r\n");
			$path = uploadProfileImage($file_name, $file_data);
			echo("File uploaded to:".$path."\r\n");
			if($_GET["username"]){
				connect();
				$result = setUserPicture(mysql_real_escape_string($_GET["username"]),$path);
				echo($result);
			}else{
				echo("No username received");
			}			
		}
	}
}
?>