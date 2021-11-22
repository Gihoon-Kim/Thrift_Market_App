<?php
    $con = mysqli_connect('localhost', 'root', '', 'tma_user');
    mysqli_query($con, 'SET NAMES utf8');

    $UserEmail = $_POST["userEmail"];

    $statement = mysqli_prepare($con, "SELECT UserEmail FROM user WHERE UserEmail = ?");

    mysqli_stmt_bind_param($statement, "s", $UserEmail);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $UserEmail);

    $response = array();
    $response["success"] = true;

    while (mysqli_stmt_fetch($statement)) {

        $response["success"] = false;
        $response["userEmail"] = $UserEmail;
    }

    echo json_encode($response);
?>