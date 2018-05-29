<?php
 
/*
 * Following code will get todays medication
 * All product details are read from HTTP Post Request
 * Input:  POST or JSON 'username' 'day' 'hour' 'min'
 * Output: 'success', 'message', 'medicines'['MedName', 'stage']
 */
 
// array for JSON response
$response = array();
 
// Create connection
include_once 'connect.php';

$data = json_decode(file_get_contents('php://input'), true);

// check for required fields
if (isset($_POST['username']) && isset($_POST['day']) && isset($_POST['hour']) && isset($_POST['min']) ) {
    //get information 
    $user = $mysqli->real_escape_string($_POST['username']);
    $day = $mysqli->real_escape_string($_POST['day']);
    $hour = $mysqli->real_escape_string($_POST['hour']);
    $min = $mysqli->real_escape_string($_POST['min']);
} 
else if (isset($data['username']) && isset($data['day']) && isset($data['hour']) && isset($data['min'])) {
     $user = $mysqli->real_escape_string($data['username']);
     $day = $mysqli->real_escape_string($_POST['day']);
     $hour = $mysqli->real_escape_string($_POST['hour']);
     $min = $mysqli->real_escape_string($_POST['min']);
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
        $medsql = $mysqli->query("SELECT DISTINCT MedNa FROM `Schedule` where UserN='$user' and Day='$day' ");
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
                $stat = getStatus($mysqli, $user, $med['MedNa'], $day, $hour, $min); 
                $obj = (object) [
                        'MedName' => $med['MedNa'],
                        'stage' => $stat
                    ];
                array_push($response["medicines"], $obj);
            }
            
            echo json_encode($response);
        }
    }
    
    //checks if schedule is close to 1 hour ahead of current time
    function getStatus($conn, $usname, $medna, $currDay, $currHour, $currMin) {
        $schedule = $conn->query("SELECT * FROM `Schedule` WHERE MedNa = '$medna' and UserN = '$usname' and Day = '$currDay' ");
        $time = $currHour . $currMin;
        $newhour = $currHour + 1; 
        $newtime = $newhour . $currMin;
        
         while($row = $schedule->fetch_assoc()) {
             $medtime = $row['Hour'] . $row['Min'];
            if($time < $medtime && $medtime < $newtime)
                return true; 
         }
         return false; 
    }
?>	