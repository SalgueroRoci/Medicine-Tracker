<?php
 
/*
 * Following code will get med info for one user medication
 * All product details are read from HTTP Post Request
 *
 */
 
// array for JSON response
$response = array();
$data = json_decode(file_get_contents('php://input'), true);


// Create connection
include_once 'connect.php';

// check for required fields
if (isset($_POST['username']) && isset($_POST['medname'])) {
    //get information 
    $user = $mysqli->real_escape_string($_POST['username']);
    $medName = $mysqli->real_escape_string($_POST['medname']);
} 
else if (isset($_GET['username']) && isset($_GET['medname'])) {
     $user = $mysqli->real_escape_string($_GET['username']);
     $medName = $mysqli->real_escape_string($_GET['medname']);
}
else if (isset($data['username']) && isset($data['medname']) )  {
     $user = $mysqli->real_escape_string($data['username']);
     $medName = $mysqli->real_escape_string($data['medname']);
}
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required fields missing!"; 
    
    echo json_encode($response);
    return;
}
echo $user . " " . $medName; 
//Check if user exists
    $result = $mysqli->query("SELECT * FROM Users WHERE Username='$user'");
    if( $result->num_rows == 0) {
	    $response["success"] = 0;
        $response["message"] = "Username doesn't exist!"; 
        echo json_encode($response);
	}
    else { //user exists
        $medsql = $mysqli->query("UPDATE UserMeds SET AmtLeft = AmtLeft - 1 WHERE User = '$user' AND MedName = '$medName' and AmtLeft > 0");
        if(!$medsql) {
            $response["success"] = 0;
            $response["message"] = "No Medication with that name!";
            echo json_encode($response);
        }
        else {
            $response["success"] = 1;
            $response["message"] = "Medicine Taken and Updated.";
        }
            
        echo json_encode($response);
        
    }
?>	