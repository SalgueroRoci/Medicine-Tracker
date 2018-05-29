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
	header("Location: index.php");
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
		header("Location: ../index.php");
		exit();
	}
}
//if checked box and pressed deleted button
if (isset($_POST['del']) && isset($_POST['checkbox'])) {
    foreach($_POST['checkbox'] as $del_id){
			//update as deleted to move to trash
			$sql = "UPDATE Messages SET MsgStatus='Deleted' WHERE MsgID='$del_id'";
			if (!$mysqli->query($sql) )
			{
			   // an error eoccurred
			   $_SESSION['message'] ="Error could not delete message: " . $del_id ;
			}
	}	
	echo "<meta http-equiv=\"refresh\" content=\"0;URL=index.php\">";
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
				  <li role="presentation" ><a href="index.php">Inbox</a></li>
				  <li role="presentation" class="active"><a href="trash.php">Trash</a></li>
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

			
			<form action="<?php echo $_SERVER['PHP_SELF'];?>" method="post">
				<table  class="mailbox">
				<tr><th>Delete</th><th>From</th><th>Subject</th><th>Time</th></tr>
				
				<?php
				
				$sql="SELECT * FROM Messages WHERE MsgStatus='Trash' ORDER BY MsgTime DESC";
				$result= $mysqli->query($sql);

				while($row = mysqli_fetch_array($result))
				 {         
					echo '<tr class="'.$row['MsgStatus'].'" >
						<td><input type="checkbox" name="checkbox[]" value="'.$row['MsgID'].'" ></td>
						<td class="clickmail" onclick="location.href=\'read.php?msgID='.$row['MsgID'].'\'" >
							<span class="glyphicon glyphicon-envelope"></span>&nbsp;&nbsp;'.$row['Sender'].'</td>
						<td class="clickmail" onclick="location.href=\'read.php?msgID='.$row['MsgID'].'\'" >'.$row['Subject'].'</td>
						<td class="clickmail" onclick="location.href=\'read.php?msgID='.$row['MsgID'].'\'" >'.$row['MsgTime'].'</td>	
					</tr>';
				 }
				 ?> 
				 
				</table>
				<input type="submit" class="btn btn-danger" name="del" value="Delete Message">
			</form>
		</div>
	</div>
</div>
<?php $_SESSION['message'] = "";?>
</body>
</html>