<?php

    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $UserEmail = $_POST["userEmail"];
    $UserPassword = $_POST["userPassword"];

    $sql = "SELECT UserNumber, UserEmail, UserPassword, UserName, UserPhoneNumber
            FROM user
            WHERE UserEmail = '$UserEmail' AND UserPassword = '$UserPassword'";

    $result = mysqli_query($conn, $sql);

    $response = array();
    $response["success"] = false;

    while ($row = mysqli_fetch_array($result)) {

        $response["success"] = true;
        $response["UserNumber"] = $row["UserNumber"];
        $response["UserEmail"] = $row["UserEmail"];
        $response["UserPassword"] = $row["UserPassword"];
        $response["UserPhoneNumber"] = $row["UserPhoneNumber"];
        $response["UserName"] = $row["UserName"];
    }

    echo json_encode($response);
?>