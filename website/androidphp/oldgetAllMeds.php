<?php
 
/*
 * Following code will create a new product user
 * All product details are read from HTTP Post Request
 * Input:  POST or JSON 'username' 
 * Output: 'success', 'message', 'message'[]
 */
 
// array for JSON response
$response = array();
 
// Create connection
include_once 'connect.php';

$data = json_decode(file_get_contents('php://input'), true);

// check for required fields
if (isset($_POST['username']) ) {
    //get information 
    $user = $mysqli->real_escape_string($_POST['username']);
} 
else if (isset($data['username']) ) {
     $user = $mysqli->real_escape_string($data['username']);
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
        $medsql = $mysqli->query("SELECT * FROM UserMeds WHERE User='$user'");
        if( $result->num_rows == 0) {
            $response["success"] = 2;
            $response["message"] = "No Medications";
            echo json_encode($response);
        }
        else {
            $response["success"] = 1;
            $response["message"] = "Success!";
            
            //fetch medicines
            $response["medicines"] = array(); 
            while($med = $medsql->fetch_assoc()) {
                array_push($response["medicines"], $med);
            }
            
            echo json_encode($response);
        }
    }
?>	