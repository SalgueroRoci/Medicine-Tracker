<?php
session_start();
session_unset(); 
$_SESSION['message'] = "";
$block = "";
include_once 'connect.php';

//check if form is submitted
if (isset($_POST['login'])) {

	$username = $mysqli->real_escape_string($_POST['usr_id']);
	$password = $mysqli->real_escape_string($_POST['password']);
	$result = $mysqli->query("SELECT * FROM Users WHERE Username = '" . $username. "' and Password = '" . md5($password) . "'");

	if ($row = mysqli_fetch_array($result)) {		
		$status = $row['Status'];
		if($status == 'Banned') {
			$_SESSION['message'] = "You are banned!";
			header("Location: logout.php");
		}
		else {
			$_SESSION['username'] = $row['Username'];
			$_SESSION['status'] = $status;
			
			header("Location: medList.php");
		}
	} else {
		$_SESSION['message'] = "Incorrect Email or Password!!!";
	}
}

?>

<!DOCTYPE html>
<html lang="en">
<head>
  <title>Login</title>
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
        <li><a href="login.php">LOGIN</a></li>
        <li><a href="medList.php">MEDICINE LIST</a></li>
        <li><a href="about.html">ABOUT US</a></li>
      </ul>
    </div>
  </div>
</nav>

<div class="container">
    <div class="row">
        <div class="col-md-8">
			<?php if ($_SESSION['message'] == "" ) { $block = 'style="display:none; margin:0; padding:0;"';}
			
				echo '<div class="alert alert-danger" '.$block .' role="alert">
					'.$_SESSION['message'].'
					<button type="button" class="close" data-dismiss="alert" aria-label="Close">
						  <span aria-hidden="true">&times;</span>
					</button>	
				</div>';
			?>
				
				<form role="form" action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post" name="loginform">
					<fieldset>
						<legend>Login</legend>
								
						<div class="form-group">
							<font color="Gray"><label for="name">Username</label></font>
							<input type="text" maxlength="32" name="usr_id" placeholder="Enter Username" required class="form-control" />
						</div>

						<div class="form-group">
							<font color="Gray"><label for="name">Password</label></font>
							<input type="password" maxlength="32" name="password" placeholder="Enter Password" required class="form-control" />
						</div>

						<div class="form-group">
							<input type="submit" maxlength="32" name="login" value="Login" class="btn btn-primary" />
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>





    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
  </body>
</html>