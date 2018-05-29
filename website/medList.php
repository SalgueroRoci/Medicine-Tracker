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
	$loggin = false; 
}

$medsql = $mysqli->query("SELECT * FROM Medicines");
	
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Medicine List</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

  <link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

 <style>
  body {
      font: 20px Montserrat, sans-serif;
      line-height: 1.8;
      color: #f5f6f7;
  }
  p {font-size: 16px;}
  .margin {margin-bottom: 45px;}
    .bg-2 { 
      background-color: #474e5d; /* Gray */
      color: #ffffff;
  }

  .container-fluid {
      padding-top: 70px;
      padding-bottom: 70px;
  }

  .navbar {
      padding-top: 15px;
      padding-bottom: 15px;
      border: 0;
      border-radius: 0;
      margin-bottom: 0;
      font-size: 12px;
      letter-spacing: 5px;
  }
  .navbar-nav  li a:hover {
      color: #1abc9c !important;
  }
  .table-dark tr:hover {
      color: black;
  }
  </style>
  </head>
  <body>
      <!-- Navbar -->
<nav class="navbar navbar-default">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>                        
      </button>
      <img src="https://i.imgur.com/2SlA8Hs.png" align="left" alt="AppIcon" width=5% height="5%">
      <a class="navbar-brand" href="index.php">Medicine Tracker</a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav navbar-right">
          <?php
            if(!$loggin){
                echo'<li><a href="login.php">LOGIN</a></li>';
            }
            else{
                echo'<li><a href="logout.php">LOGOUT</a></li>';
            }
		  ?>				
        
        <li><a href="medList.php">MEDICINE LIST</a></li>
        <li><a href="about.html">ABOUT US</a></li>
      </ul>
    </div>
  </div>
</nav>
</body>

	<div class="container-fluid bg-2 text-left"> 
		<div class="row">
			<div class="col-md-2">				
				<?php 
				    if($admin) {
						echo '<a class="btn btn-primary" href="addMed.php">Add Medication</a>';
						echo '<a class="btn btn-primary" href="mailbox/index.php">Report MailBox</a>';
					}
					if($loggin) {
						echo '<a class="btn btn-warning" href="report.php">Report Issue</a>';
					}
					
					
				?>
			</div>
			
			<div class="col-md-7">
				
				<?php if ($_SESSION['message'] == "" ) { $block = 'style="display:none; margin:0; padding:0;"';}		
					echo '<div class="alert alert-info" '.$block .' role="alert">
						'.$_SESSION['message'].'
						<button type="button" class="close" data-dismiss="alert" aria-label="Close">
							  <span aria-hidden="true">&times;</span>
						</button>
					</div>';
				?>
				
				<h1>Medicine List</h1>
				<h4>Medicine table that has information about medicines used for our mobile application.</h4>
				<?php
					echo '<table class="table table-hover table-dark">';
					echo '<thead>
						<tr>
						  <th scope="col">#</th>
						  <th scope="col">Medicine name</th>
						  <th scope="col">Details</th>
						</tr>
					  </thead>
					  <tbody>';
			 
					$i = 1;
					while($row = mysqli_fetch_array($medsql))
					 {         
						echo '<tr>
							<th scope="row">'.$i.'</th>
							<td>'.$row['OfficialName'].'</td>
						    <td><a class="btn btn-primary" href="MedInfo.php?medname='.$row['OfficialName'].'">Details</a></td>';
							if ($admin) {
								echo '<td>
								<form action="editMed.php" method="post">
									<input type="hidden" maxlength="32" name="medname" placeholder="Medname" value="'.$row['OfficialName'].'" />
									<input type="submit" class="btn btn-primary" name="edit" value="Edit Medication">
								</form>
								</td>';
							}
							'</tr>';
						$i++;
					 }
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
</html>