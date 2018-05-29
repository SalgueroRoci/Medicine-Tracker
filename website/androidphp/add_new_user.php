<?php
 
/*
 * Following code will create a new product user
 * All product details are read from HTTP Post Request
 * Input:  POST 'username' , 'password', and 'email' 
 * Output: 'success', 'message'
 */
 
// array for JSON response
$response = array();
 
// Create connection
include_once 'connect.php';

// check for required fields
if (isset($_POST['username']) && isset($_POST['password']) && isset($_POST['email']) ) {
 
    //get information 
    $user = $mysqli->real_escape_string($_POST['username']);
    $pass = $_POST['password'];
    $email = $_POST['email'];
    
    
//check if username is unique, check if password > 6 , check if valid email 
    //check is user is entered before/username taken
    $result = $mysqli->query("SELECT * FROM Users WHERE Username='$user'");
    if( $result->num_rows > 0) {
	    $response["success"] = 0;
        $response["message"] = "username taken!"; 
        echo json_encode($response);
	}
    else {
        $sql = "INSERT INTO Users (Username, Password, Email) VALUES('$user','".md5($pass)."','$email')";
        
        //if the query is successsful, user added
        if ($mysqli->query($sql) === true){
            $response["success"] = 1;
            $response["message"] = "Registration Successful!"; 
            echo json_encode($response);
        }
        else {
            $response["success"] = 0;
            $response["message"] = "Something went wrong!"; 
            echo json_encode($response);
        }
    }//end username dup check else
    
} 
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}

?>	