<?php
/* Displays user information and some useful messages */
session_start();
include_once '../connect.php';
$admin = false; 
$loggin = false; 
$block = "";
$_SESSION['message'] = "";

// Check if user is logged in using the session variable
if(!isset($_SESSION["username"])){
	$_SESSION['message'] = "You must log in before viewing your profile page!";
	header("Location: ../index.php");
	exit(); 
}
else {
    // Makes it easier to read
    $username = $_SESSION['username'];
	$status = $_SESSION['status'];
	$loggin = true;
	if($status == "Admin") $admin = true; 
	
	if(!$admin) {
		$_SESSION['message'] = "You must log in before viewing your profile page!";
		header("Location: index.php");
		exit();
	}
}
if(isset($_GET['msgID']) ) 
	$id = $_GET['msgID'];  
else 
	$id = "";
	
if (!isset($id) ) {
	$_SESSION['message'] = "No message to read";
	header("Location: index.php");
} 

$_SESSION['message'] = "";

//get message
$sql = "SELECT * FROM Messages WHERE MsgID='$id'";
$msg = mysqli_fetch_array($mysqli->query($sql) );

//if they pressed deleted 
if (isset($_POST['btnDelete']) ) {
	$id = $_POST['ID'];
    $update = "UPDATE Messages SET MsgStatus='Trash' WHERE MsgID='$id'";
	
	if (!$mysqli->query($update) ) {
		echo "Error updating trash"; 
	}
	header("Location: index.php");
} 
else if($msg['MsgStatus'] != "Trash") {		
	//update as read 
	$update = "UPDATE Messages SET MsgStatus='Read' WHERE MsgID='$id'";
	if (!$mysqli->query($update) ) {
		echo "Error updating read";
	}
}
	
?>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Mailbox for  <?php echo $username; ?></title>
<link rel="stylesheet" href="mail.css">
 <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  
</head>
<body>
<div class="container"  >


	<div class="row">
		<div class="col-md-2">
			<ul class="nav nav-pills nav-stacked">
				  <li role="presentation" ><a href="../index.php">Homepage</a></li>
				  <li role="presentation" class="active"><a href="index.php">Inbox</a></li>
				  <li role="presentation" ><a href="trash.php">Trash</a></li>
				  <li role="presentation" ><a href="../logout.php">Logout</a></li>
			</ul>
		</div>

		<div class="col-md-10">
			
			<?php if ($_SESSION['message'] == "" ) { $block = 'style="display:none; margin:0; padding:0;"';}
			
				echo '<div class="alert alert-info" '.$block .' role="alert">
					'.$_SESSION['message'].'
					<button type="button" class="close" data-dismiss="alert" aria-label="Close">
						  <span aria-hidden="true">&times;</span>
					</button>
				</div>';
			?>
			<div class='readmail'>
				<?php 
						echo "<div class='panel-body'><b>Subject: </b>". $msg['Subject']. "</div>
							<div class='panel-body'><b>Time: </b>". $msg['MsgTime']. "</div>
							<div class='panel-body'><b>From: </b>".	$msg['Sender']. "</div>
							<div class='panel-body'><b>Message: </b></br>" .$msg['MsgText']."</div>";							
							
						echo '<form style="float:right;" action="'.$_SERVER['PHP_SELF'].'" method="post"> 
								<input type="text" style="display:none" name="ID" maxlength="10" value="'.$id.'" />
								<input type="submit" name="btnDelete" class="btn btn-danger" value="Delete Message">
							</form>';						
				?>
			</div>
			
		</div>
	</div>
	
</div>
</body>
</html>