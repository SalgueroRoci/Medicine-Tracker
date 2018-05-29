<?php
session_start();

if(isset($_SESSION['username'])) {
	session_destroy();
	unset($_SESSION['username']);
	unset($_SESSION['name']);
	$_SESSION['message'] = "You have been logged out"; 
	header("Location: ../index.php");
} else {
	$_SESSION['message'] = "You have been logged out"; 
	header("Location: ../index.php");
}
?>