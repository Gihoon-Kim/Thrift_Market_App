<?php
    $con = mysqli_connect('localhost', 'root', '', 'tma_user');

    $ProductNumber = $_POST["productNumber"];

    $statement = mysqli_prepare($con,
        "DELETE FROM product
            WHERE ProductNumber = ?;"
    );

    mysqli_stmt_bind_param($statement, "i", $ProductNumber);
    mysqli_stmt_execute($statement);

    if ($statement) {
        $response["success"] = true;
    }
    else {
        $response["success"] = false;
    }
    
    echo json_encode($response);
?>