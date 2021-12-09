<?php
    $con = mysqli_connect('localhost', 'root', '', 'tma_user');

    $ProductOwner = $_POST["productOwner"];

    $statement = mysqli_prepare($con,
        "SELECT ProductNumber, ProductName, ProductDesc, ProductPrice, u.UserName, TradeLocation, Processing, AddedDate
            FROM product p
            JOIN user u
            ON p.ProductOwner = u.UserNumber
            WHERE ProductOwner = ?;"
            );
    mysqli_stmt_bind_param($statement, "i", $ProductOwner);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $ProductNumber, $ProductName, $ProductDesc, $ProductPrice, $ProductOwner, $TradeLocation, $Processing, $AddedDate);

    $response = array();

    while (mysqli_stmt_fetch($statement)) {

        array_push($response, 
                    array(
                        'success' => true,
                        'ProductNumber' => $ProductNumber,
                        'ProductName' => $ProductName,
                        'ProductDesc' => $ProductDesc,
                        'ProductOwner' => $ProductOwner,
                        'ProductPrice' => $ProductPrice,
                        'TradeLocation' => $TradeLocation,
                        'AddedDate' => $AddedDate
                    ));
    }

    echo json_encode(array("Products"=>$response));

    mysqli_close($con);
?>