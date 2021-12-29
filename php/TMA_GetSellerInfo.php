<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $ProductNumber = $_POST["productNumber"];

    $sql = "SELECT UserName, UserEmail, TradeCount, UserPhoneNumber 
            FROM user 
            WHERE UserNumber in (
                SELECT ProductOwner 
                FROM `product` 
                WHERE ProductNumber = '$ProductNumber'
                )";

    $result = mysqli_query($conn, $sql);

    $response = array();
    $response["success"] = false;

    while ($row = mysqli_fetch_array($result)) {

        $response["success"] = true;
        $response["UserName"] = $row["UserName"];
        $response["UserEmail"] = $row["UserEmail"];
        $response["TradeCount"] = $row["TradeCount"];
        $response["UserPhoneNumber"] = $row["UserPhoneNumber"];
    }

    echo json_encode($response);
?>