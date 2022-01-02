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

    $response = array();

    // INSERT INTO product database
    if ($stmt = $conn->prepare("INSERT INTO product (
        ProductName, 
        ProductDesc, 
        ProductPrice, 
        ProductOwner, 
        tradeLocation,
        AddedDate) 
        VALUES (
            ?, 
            ?, 
            ?,
            ?, 
            ?,
            ?)")) {

        $stmt->bind_param(
        "ssssss", 
        $ProductName, 
        $ProductDesc, 
        $productPrice,
        $productOwner, 
        $Location,
        $AddedDate
        );
        
        $stmt->execute();
        $response["success"] = true;
    } else {

        $response["success"] = false;
    }
      
    
    echo json_encode($response);
?>