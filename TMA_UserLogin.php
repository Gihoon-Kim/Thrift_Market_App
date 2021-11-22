<?php
    $con = mysqli_connect('localhost', 'root', '', 'tma_user');
    mysqli_query($con, 'SET NAMES utf8');

    $UserEmail = $_POST["userEmail"];
    $UserPassword = $_POST["userPassword"];

    $statement = mysqli_prepare($con, "SELECT UserNumber, UserEmail, UserPassword, UserName, UserPhoneNumber FROM user WHERE UserEmail = ? AND UserPassword = ?");
    mysqli_stmt_bind_param($statement, "ss", $UserEmail, $UserPassword);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $UserNumber, $UserEmail, $UserPwd, $UserName, $UserPhoneNumber);

    $response = array();
    $response["success"] = false;

    while (mysqli_stmt_fetch($statement)) {

        $response["success"] = true;
        $response["UserNumber"] = $UserNumber;
        $response["UserEmail"] = $UserEmail;
        $response["UserPassword"] = $UserPassword;
        $response["UserPhoneNumber"] = $UserPhoneNumber;
        $response["UserName"] = $UserName;
    }

    echo json_encode($response);
?>