<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $ProductNumber = $_POST["productNumber"];

    $sql = "DELETE FROM product
            WHERE ProductNumber = '$ProductNumber';";

    $result = mysqli_query($conn, $sql);

    if ($result) {
        $response["success"] = true;
    }
    else {
        $response["success"] = false;
    }
    
    echo json_encode($response);
?>