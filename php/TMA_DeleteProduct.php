<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $ProductNumber = $_POST["productNumber"];
    $ProductName = $_POST["productName"];

    $sql = "DELETE p, i
            FROM product as p
            INNER JOIN images as i on i.productName = p.productName
            WHERE p.ProductNumber = '$ProductNumber'
            AND i.productName = '$ProductName';";

    $result = mysqli_query($conn, $sql);

    if ($result) {
        $response["success"] = true;
    }
    else {
        $response["success"] = false;
    }
    
    echo json_encode($response);
?>