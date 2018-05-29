<?php
session_start();
include_once 'connect.php';
$admin = false; 
$loggin = false; 
$block = "";
$_SESSION['message'] = "";

// Check if user is logged in using the session variable
if(!isset($_SESSION["username"])){
	$_SESSION['message'] = "You must log in before viewing this page!";
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
		$_SESSION['message'] = "You must log in before this page!";
		header("Location: index.php");
		exit();
	}
}

if( isset($_POST['addMed']) )  {
	$medname = $_POST['medname'];
	$uses = $_POST['uses'];
	$warnings = $_POST['warnings'];
	$ingredients = $_POST['ingredients'];
	$h24Over = $_POST['24hOv'];
	$oneOver = $_POST['oneOver'];
	
	$sql = "INSERT INTO `Medicines` (`OfficialName`, `Uses`, `Warnings`, `Ingredients`, `24hOverdose`, `oneOverdose`) VALUES ('$medname', '$uses', '$warnings', '$ingredients', '$h24Over', '$oneOver');";
        
    //if the query is successsful, user exists so add medication
    if ($mysqli->query($sql) === true){
        $_SESSION['message'] = "Medicine Added!"; 		
		header('Refresh: 10; URL=index.php');
    }
    else {
        $_SESSION['message'] = "Something went wrong! Medicine Table";
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Add Medication</title>
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

    <div class="container">
		<div class="row">
			<div class="col-md-10">
				
				<?php if ($_SESSION['message'] == "" ) { $block = 'style="display:none; margin:0; padding:0;"';}		
					echo '<div class="alert alert-info" '.$block .' role="alert">
						'.$_SESSION['message'].'
						<button type="button" class="close" data-dismiss="alert" aria-label="Close">
							  <span aria-hidden="true">&times;</span>
						</button>
					</div>';
				?>
				
				
				<form role="form" action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post" name="loginform">
					<fieldset>
						<legend>New Medication:</legend>
								
						<div class="form-group">
							<font color="black"><label for="name">Medicine Name</label></font>
							<input type="text" maxlength="32" name="medname" placeholder="Medicine Name" required class="form-control" />
						</div>

						<div class="form-group">
							<font color="black"><label for="name">Uses</label></font>
							<textarea type="text" maxlength="250" name="uses" placeholder="Medicine Uses" required class="form-control" ></textarea>
						</div>
						
						<div class="form-group">
							<font color="black"><label for="name">Warnings</label></font>
							<textarea type="text" maxlength="250" name="warnings" placeholder="Warnings" required class="form-control" ></textarea>
						</div>

						<div class="form-group">
							<font color="black"><label for="name">Ingredients</label></font>
							<textarea type="text" maxlength="250" name="ingredients" placeholder="Ingredients" required class="form-control" ></textarea>
						</div>
						
						<div class="form-group">
							<font color="black"><label for="name">24 hour Overdose:</label></font>
							<input type="number" maxlength="32" name="24hOv" placeholder="Overdose" required class="form-control" />
						</div>

						<div class="form-group">
							<font color="black"><label for="name">One Time Overdose:</label></font>
							<input type="number" maxlength="32" name="oneOver" placeholder="Instant Overdose" required class="form-control" />
						</div>
						<div class="form-group">
							<input type="submit" name="addMed" value="Add Medication" class="btn btn-primary" />
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
	
 
	<?php $_SESSION['message'] = "";?>
  </body>
</html>