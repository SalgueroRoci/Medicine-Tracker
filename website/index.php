<?php
session_start();
include_once 'connect.php';

$block = "";
$_SESSION['message'] = "";
$admin = false; 
$loggin = false; 
if(isset($_SESSION['username']) ){
	$username = $_SESSION['username'];
	$status = $_SESSION['status'];
	if($status == "Admin") 
		$admin = true; 
	$loggin = true; 
}
else {
	$admin = false; 
    $loggin = false; 
}

$medsql = $mysqli->query("SELECT * FROM Medicines");
	
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Medicine Tracker</title>
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
  .bg-1 { 
      background-color: #cc0000; /* red */
      color: #ffffff;
  }
  .bg-2 { 
      background-color: #474e5d; /* Gray */
      color: #ffffff;
  }
  .bg-3 { 
      background-color: #ffffff; /* White */
      color: #555555;
  }
  .bg-4 { 
      background-color: #2f2f2f; /* Black Gray */
      color: #fff;
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
        <?php if($loggin) echo '<li><a href="logout.php">Logout</a></li>'; 
            else echo '<li><a href="login.php">LOGIN</a></li>'; ?>
        <li><a href="medList.php">MEDICINE LIST</a></li>
        <li><a href="about.html">ABOUT US</a></li>
      </ul>
    </div>
  </div>
</nav>

<!-- First Container -->
<div class="container-fluid bg-1 text-center">
  <img src="https://cdn.paindoctor.com/wp-content/uploads/2016/04/pain-tracker.jpg" class="img-responsive img-rounded margin" style="display:inline" alt="Phone" width="550" height="550">
  <h3>Being on time could save your life</h3>
</div>

<!-- Second Container -->
<div class="container-fluid bg-3 text-center">    
  <h3 class="margin"><b>Medicine Tracker is designed to track your medication intake.<br>Never forget again</b></h3><br>
  <div class="row">
    <div class="col-sm-3">
      <p>Register</p>
      <img src="https://i.imgur.com/BddJd5a.png" class="center" style="width:50%" alt="Image">
    </div>
    <div class="col-sm-3"> 
      <p>Add Medication</p>
      <img src="https://i.imgur.com/fvnXbW1.png" class="center" style="width:50%" alt="Image">
    </div>
    <div class="col-sm-3"> 
      <p>View List of Medication</p>
      <img src="https://i.imgur.com/YUYDAB0.png" class="center" style="width:50%" alt="Image">
    </div>
    <div class="col-sm-3"> 
      <p>Alarm</p>
      <img src="https://i.imgur.com/a73l6yO.png" class="center" style="width:100%" alt="Image">
    </div>
    
  </div>
</div>

<!-- Footer -->
<footer class="container-fluid bg-4 text-center">
  <p>To report any issues regarding the application, login to fill in the report form.</p> 
</footer>

</body>
</html>

