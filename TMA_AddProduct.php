<?php
    $con = mysqli_connect('localhost', 'root', '', 'tma_user');

    $ProductName = $_POST["productName"];
    $ProductDesc = $_POST["productDesc"];
    $productPrice = $_POST["productPrice"];
    $productOwner = $_POST["productOwner"];

    $statement = mysqli_prepare($con, "INSERT INTO product (ProductName, ProductDesc, ProductPrice, ProductOwner) VALUES (?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "ssdi", $ProductName, $ProductDesc, $productPrice, $productOwner);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
?>