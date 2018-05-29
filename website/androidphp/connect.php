<?php

    $us =  "id3237782_users"; // db user
    $pas= "bpWP5b3DbaM"; // db password (mention your db password here)
    $dbname = "id3237782_medicine"; // database name
    $servername = "localhost"; // db server

    $mysqli = new mysqli($servername, $us, $pas, $dbname);

// Check connection
if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
    echo "failed"; 
} 
//else echo "Connected successfully";

?>