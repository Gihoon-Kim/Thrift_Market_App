<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $UserEmail = $_POST["userEmail"];

    $sql = "SELECT UserEmail 
            FROM user
            WHERE UserEmail = '$UserEmail'";
    $result = mysqli_query($conn, $sql);
    if (!$result) {
        echo mysqli_error($conn);
    }

    $response = array();
    $response["success"] = true;

    $board = mysqli_fetch_array($result); 
    if (!is_null($board)) {

        $response["success"] = false;
        $response["userEmail"] = $UserEmail;
    }

    echo json_encode($response);
?>