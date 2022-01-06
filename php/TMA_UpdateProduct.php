<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $ProductNumber = $_POST["productNumber"];
    $ProductName = $_POST["productName"];
    $ProductDesc = $_POST["productDesc"];
    $productPrice = $_POST["productPrice"];
    $Processing = $_POST["status"];

    $sql = "UPDATE product, images
            SET product.productName = '$ProductName', 
                product.productDesc = '$ProductDesc', 
                product.productPrice = '$productPrice', 
                product.Processing = '$Processing',
                images.productName = '$ProductName'
            WHERE product.productNumber = '$ProductNumber' AND  images.productName = product.productName";
    
    $result = mysqli_query($conn, $sql);

    $sql = "SELECT ProductNumber, ProductName, ProductDesc, ProductPrice, Processing
            FROM product
            WHERE productNumber = '$ProductNumber'";
    
    $result = mysqli_query($conn, $sql);

    $response = array();

    while ($row = mysqli_fetch_array($result)) {

        $response["success"] = true;
        $response["ProductNumber"] = $row["ProductNumber"];
        $response["ProductName"] = $row["ProductName"];
        $response["ProductDesc"] = $row["ProductDesc"];
        $response["ProductPrice"] = $row["ProductPrice"];
        $response["Processing"] = $row["Processing"];
    }

    echo json_encode($response);
?>