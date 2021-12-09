<?php
    $con = mysqli_connect('localhost', 'root', '', 'tma_user');

    $UserName = $_POST["userName"];
    $UserEmail = $_POST["userEmail"];
    $UserPassword = $_POST["userPwd"];
    $UserPhoneNumber = $_POST["userPhoneNumber"];

    $statement = mysqli_prepare($con, "INSERT INTO user (UserName, UserEmail, UserPassword, UserPhoneNumber) VALUES (?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "ssss", $UserName, $UserEmail, $UserPassword, $UserPhoneNumber);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
?>