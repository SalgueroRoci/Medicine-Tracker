<?php
 
/*
 * Following code will login the user
 * All product details are read from HTTP Post Request
 * Input:  POST 'username' , 'password'
 * Output: 'success', 'message', 'username'
 */
 
// array for JSON response
$response = array();
 
// Create connection
include_once 'connect.php';

// check for required fields
if (isset($_POST['username']) && isset($_POST['password'])  ) {
 
    //get information 
    $user = $mysqli->real_escape_string($_POST['username']);
    $pass = $_POST['password'];
    
    //Check if user exists
    $result = $mysqli->query("SELECT * FROM Users WHERE Username='$user'");
    if( $result->num_rows == 0) {
	    $response["success"] = 0;
        $response["message"] = "Username doesn't exist!"; 
        echo json_encode($response);
	}
    else { //user exists
        $check = $result->fetch_assoc();

    	$pswd = md5($pass); 
        if ($pswd == $check['Password'] ) {
            $response["success"] = 1;
            $response["message"] = "Logging in...";
            $response["username"] = $user; 
            echo json_encode($response);
        }
        else {
            // required field is missing
            $response["success"] = 0;
            $response["message"] = "Password not correct!";
         
            // echoing JSON response
            echo json_encode($response);
        }
    }
    
} 
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}

?>	