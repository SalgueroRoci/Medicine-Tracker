<?php
 
/*
 * Following code will get med info for one user medication
 * All product details are read from HTTP Post Request
 * Input:  POST or JSON 'username' amd 'medname'
 * Output: 'success', 'message', 'perbottle', 'amtleft', 'dose', 'schedule['day', 'hour', 'min', 'intentID']'
                'officialName', 'uses', 'warnings', 'ingredients', '24hOverdose', 'oneOverdose'
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
        $medsql = $mysqli->query("SELECT * FROM UserMeds WHERE User='$user' and MedName='$medName'");
        if( $medsql->num_rows == 0) {
            $response["success"] = 0;
            $response["message"] = "No Medication with that name!";
            echo json_encode($response);
        }
        else {
            $response["success"] = 1;
            $response["message"] = "Success!";
            $response["medname"] = $medName;
            
            $med = $medsql->fetch_assoc(); 
            $response["perbottle"] = $med['PerBottle'];
            $response["amtleft"] = $med['AmtLeft'];
            $response["dose"] = $med['Dosage'];
            $response["alarmCount"] = $med['AlarmCount']; 
            
            //get schedule
            $schedule = $mysqli->query("SELECT * FROM `Schedule` WHERE MedNa = '$medName' and UserN = '$user' ");
            $response["schedule"] = array(); 
            while($sch = $schedule->fetch_assoc()) {
                $obj = (object) [
                        'day' => $sch['Day'],
                        'hour' => $sch['Hour'],
                        'min' => $sch['Min'],
                        'intentID' => $sch['intentID']
                    ];
                array_push($response["schedule"], $obj);
            }
            
            //get official Medicine Information 
            $offsql = $mysqli->query("SELECT * FROM `Medicines` WHERE OfficialName = '$medName' ");
            if($offsql->num_rows == 0 ) {
                $response["officialName"] = "none";
                $response["uses"] = "none";
                $response["warnings"] = "none";
                $response["ingredients"] = "none";
                $response["24hOverdose"] = "0";
                $response["oneOverdose"] = "0";
            }
            else {
                $official = $offsql->fetch_assoc(); 
                $response["officialName"] = $official['OfficialName'];
                $response["uses"] = $official['Uses'];
                $response["warnings"] = $official['Warnings'];
                $response["ingredients"] = $official['Ingredients'];
                $response["24hOverdose"] = $official['24hOverdose'];
                $response["oneOverdose"] = $official['oneOverdose'];
            }
            
            echo json_encode($response);
        }
    }
    
?>	