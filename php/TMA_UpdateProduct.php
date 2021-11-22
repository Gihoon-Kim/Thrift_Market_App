<?php
    $con = mysqli_connect('localhost', 'root', '', 'tma_user');

    $ProductNumber = $_POST["productNumber"];
    $ProductName = $_POST["productName"];
    $ProductDesc = $_POST["productDesc"];
    $productPrice = $_POST["productPrice"];

    $statement = mysqli_prepare($con, 
        "UPDATE product 
            SET productName = ?, productDesc = ?, productPrice = ?
            WHERE productNumber = ?;"
            );
    mysqli_stmt_bind_param($statement, "ssdi", $ProductName, $ProductDesc, $productPrice, $ProductNumber);
    mysqli_stmt_execute($statement);
    
    $statement = mysqli_prepare($con, 
        "SELECT ProductNumber, ProductName, ProductDesc, ProductPrice
	        FROM product 
            WHERE productNumber = ?;"
            );
    
    
    mysqli_stmt_bind_param($statement, "i", $ProductNumber);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $ProductNumber, $ProductName, $ProductDesc, $ProductPrice);

    $response = array();

    while (mysqli_stmt_fetch($statement)) {

        $response["success"] = true;
        $response["ProductNumber"] = $ProductNumber;
        $response["ProductName"] = $ProductName;
        $response["ProductDesc"] = $ProductDesc;
        $response["ProductPrice"] = $ProductPrice;
    }

    echo json_encode($response);
?>