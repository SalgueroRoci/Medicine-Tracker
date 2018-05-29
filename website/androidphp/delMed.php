<?php
 
/*
 * Following code will delete medication for user 
 * All product details are read from HTTP Post Request
 * Input:  POST or JSON 'username' amd 'medname'
 * Output: 'success', 'message'
 */
 
// array for JSON response
$response = array();
 
// Create connection
include_once 'connect.php';

$data = json_decode(file_get_contents('php://input'), true);

// check for required fields
if (isset($_POST['username']) && isset($_POST['medname'])) {
    //get information 
    $user = $mysqli->real_escape_string($_POST['username']);
    $medName = $mysqli->real_escape_string($_POST['medname']);
} 
else if (isset($data['username']) && isset($data['medname'])) {
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

//Check if user exists
    $result = $mysqli->query("SELECT * FROM Users WHERE Username='$user'");
    if( $result->num_rows == 0) {
	    $response["success"] = 0;
        $response["message"] = "Username doesn't exist!"; 
        echo json_encode($response);
	}
    else { //user exists
        $medsql = $mysqli->query("DELETE FROM UserMeds WHERE User = '$user' AND MedName = '$medName' ");
        if(!$medsql) {
            $response["success"] = 0;
            $response["message"] = "No Medication with that name!";
            echo json_encode($response);
        }
        else {
            $response["success"] = 1;
            $response["message"] = "Success! Medication ".$medName." deleted." ;
            echo json_encode($response);
        }
    }
    
?>	