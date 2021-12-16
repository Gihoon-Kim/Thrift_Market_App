<?php
    $con = mysqli_connect('localhost', 'root', '', 'tma_user');

    $ProductNumber = $_POST["productNumber"];

    $statement = mysqli_prepare($con,
        "SELECT UserName, UserEmail, TradeCount, UserPhoneNumber 
            FROM user 
            WHERE UserNumber in (
                SELECT ProductOwner 
                FROM `product` 
                WHERE ProductNumber = ?)"
                );

    mysqli_stmt_bind_param($statement,"i", $ProductNumber);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $UserName, $UserEmail, $TradeCount, $UserPhoneNumber);

    $response = array();
    $response["success"] = false;

    while (mysqli_stmt_fetch($statement)) {

        $response["success"] = true;
        $response["UserName"] = $UserName;
        $response["UserEmail"] = $UserEmail;
        $response["TradeCount"] = $TradeCount;
        $response["UserPhoneNumber"] = $UserPhoneNumber;
    }

    echo json_encode($response);
?>