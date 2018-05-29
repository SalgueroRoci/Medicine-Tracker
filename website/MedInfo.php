<?php
session_start();
include_once 'connect.php';
$admin = false; 
$loggin = false; 
$block = "";
$_SESSION['message'] = "";

if(isset($_SESSION['username']) ){
	$username = $_SESSION['username'];
	$status = $_SESSION['status'];
	if($status == "Admin") 
		$admin = true; 
	$loggin = true; 
}
else {
	$username = "";
	$loggin = false; 
}

if(isset($_GET['medname']) )  {
	$mednam = $_GET['medname'];
	$medsql = $mysqli->query("SELECT * FROM Medicines Where OfficialName='$mednam';");
	if($med = $medsql->fetch_assoc()) {
		$medname = $med['OfficialName'];
		$uses = $med['Uses'];
		$warnings = $med['Warnings'];
		$ingredients = $med['Ingredients'];
		$h24Overdose = $med['24hOverdose'];
		$oneOverdose = $med['oneOverdose'];
	} 
	else {
		$medname = "Error";
		$uses = "";
		$warnings = "";
		$ingredients = "";
		$h24Overdose = "";
		$oneOverdose = "";
	}
	
}
else {
	$medname = "";
    $uses = "";
    $warnings = "";
    $ingredients = "";
    $h24Overdose = "";
    $oneOverdose = "";
}

if(isset($_POST['del']) ) {
	$medname = $_POST['medname'];
	$medsql = $mysqli->query("DELETE FROM Medicines WHERE OfficialName = '$medname';");
    if(!$medsql) {
        $_SESSION['message'] = "Something went wrong. " . $medname . " could not be deleted.";
    }
    else {        
		$_SESSION['message'] = "Successful Deletion!";
        header('Refresh: 10; URL=index.php');
    }
}
	
?>
<!doctype html>
<html lang="en">
  <head>
    <title>Medicine Tracker</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
  </head>
  <body>
    <div class="container">
		<div class="row">
			<div class="col-md-2">		
				<a class="btn btn-default" href="index.php">Homepage</a>
				<?php 
					if(!$loggin) 
						echo '<a class="btn btn-default" href="login.php">Login</a>';
					else 
						echo '<a class="btn btn-default" href="logout.php">Logout</a>';
						
					if($admin) {
						echo '<form action="'.$_SERVER['PHP_SELF'].'?medname='.$medname.'" method="post">
						<input type="hidden" maxlength="32" name="medname" placeholder="Medname" value="'.$medname.'" />
						<input type="submit" class="btn btn-danger" name="del" value="Delete Medication">';
						echo '</form>';
						
						echo '<form action="editMed.php" method="post">
						<input type="hidden" maxlength="32" name="medname" placeholder="Medname" value="'.$medname.'" />
						<input type="submit" class="btn btn-primary" name="edit" value="Edit Medication">';
						echo '</form>';						
					}						
					
				?>
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
				
				
				<?php
					echo '<h1>Medicine:'.$medname.'</h1>';
					echo '<table class="table table-hover table-dark">';
					echo '<thead>
						<tr>
						  <th scope="col">Field</th>
						  <th scope="col">Data</th>
						</tr>
					  </thead>
					  <tbody>';
					   
						echo '<tr>
							<td>Uses:</td>
						    <td>'.$uses.'</td>							
							</tr>
							<tr>
							<td>Warnings:</td>
						    <td>'.$warnings.'</td>							
							</tr>
							<tr>
							<td>Ingredients:</td>
						    <td>'.$ingredients.'</td>							
							</tr>
							<tr>
							<td>24 Hour Overdose:</td>
						    <td>'.$h24Overdose.'</td>							
							</tr>
							<tr>
							<td>Instant Overdose:</td>
						    <td>'.$oneOverdose.'</td>							
							</tr>';	
					 echo '</tbody></table>';
				?>
				 
			</div>	
		 
		</div>
	</div>
    
    

    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
	
	<?php $_SESSION['message'] = "";?>
  </body>
</html>