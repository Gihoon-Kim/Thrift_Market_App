<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $ProductName = $_POST["productName"];
    $ProductDesc = $_POST["productDesc"];
    $productPrice = $_POST["productPrice"];
    $productOwner = $_POST["productOwner"];
    $Location = $_POST["location"];
    $AddedDate = $_POST["AddedDate"];

    $sql = "INSERT INTO product (
        ProductName, 
        ProductDesc, 
        ProductPrice, 
        ProductOwner, 
        tradeLocation,
        AddedDate) 
        VALUES (
            '$ProductName', 
            '$ProductDesc', 
            '$productPrice',
            '$productOwner', 
            '$Location',
            '$AddedDate')";
    
    $result = mysqli_query($conn, $sql);
    $response = array();
    $response["success"] = true;

    echo json_encode($response);
?>