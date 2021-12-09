<?php
    $con = mysqli_connect('localhost', 'root', '', 'tma_user');

    $ProductName = $_POST["productName"];
    $ProductDesc = $_POST["productDesc"];
    $productPrice = $_POST["productPrice"];
    $productOwner = $_POST["productOwner"];
    $Location = $_POST["location"];

    $statement = mysqli_prepare($con, "INSERT INTO product (ProductName, ProductDesc, ProductPrice, ProductOwner, tradeLocation) VALUES (?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "ssdis", $ProductName, $ProductDesc, $productPrice, $productOwner, $Location);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
?>