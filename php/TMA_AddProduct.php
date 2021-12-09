<?php
    $con = mysqli_connect('localhost', 'root', '', 'tma_user');

    $ProductName = $_POST["productName"];
    $ProductDesc = $_POST["productDesc"];
    $productPrice = $_POST["productPrice"];
    $productOwner = $_POST["productOwner"];
    $Location = $_POST["location"];
    $Date = date('Y-m-d');

    $statement = mysqli_prepare($con, "INSERT INTO product (ProductName, ProductDesc, ProductPrice, ProductOwner, tradeLocation, AddedDate) VALUES (?, ?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "ssdiss", $ProductName, $ProductDesc, $productPrice, $productOwner, $Location, $Date);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
?>