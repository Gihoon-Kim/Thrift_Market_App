<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $ProductOwner = $_POST["productOwner"];

    $sql = "SELECT ProductNumber, ProductName, ProductDesc, ProductPrice, u.UserName, TradeLocation, Processing, AddedDate
            FROM product p
            JOIN user u
            ON p.ProductOwner = u.UserNumber
            WHERE ProductOwner = '$ProductOwner';";

    $result = mysqli_query($conn, $sql);

    $response = array();

    while ($row = mysqli_fetch_array($result)) {

        array_push($response, 
                    array(
                        'success' => true,
                        'ProductNumber' => $row["ProductNumber"],
                        'ProductName' => $row["ProductName"],
                        'ProductDesc' => $row["ProductDesc"],
                        'ProductOwner' => $row["UserName"],
                        'ProductPrice' => $row["ProductPrice"],
                        'TradeLocation' => $row["TradeLocation"],
                        'AddedDate' => $row["AddedDate"]
                    ));
    }

    echo json_encode(array("Products"=>$response));
?>