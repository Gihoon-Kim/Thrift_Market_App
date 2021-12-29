<?php

    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $UserName = $_POST["userName"];
    $UserEmail = $_POST["userEmail"];
    $UserPassword = $_POST["userPwd"];
    $UserPhoneNumber = $_POST["userPhoneNumber"];

    $sql = "INSERT 
            INTO user (UserName, UserEmail, UserPassword, UserPhoneNumber) 
            VALUES ('$UserName', '$UserEmail', '$UserPassword', '$UserPhoneNumber')";
    $result = mysqli_query($conn, $sql);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

    $root = $_SERVER["DOCUMENT_ROOT"];

    $path = $root . "/users/{$UserName}_{$UserEmail}";
    echo $path;
    if (!file_exists($path)) {

        mkdir($path, 0755, true);
    }
?>