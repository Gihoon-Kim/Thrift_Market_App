<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $ProductOwner = $_POST["productOwner"];

    $sql = "SELECT ProductNumber, p.ProductName, ProductDesc, ProductPrice, u.UserName, TradeLocation, Processing, AddedDate, i.FilePath, i.imageName
            FROM product p
            JOIN user u
            JOIN images i
            ON p.ProductOwner = u.UserNumber
            WHERE ProductOwner = 14 AND i.ImageNumber = 0 AND i.ProductName = p.ProductName;";

    $result = mysqli_query($conn, $sql);

    $response = array();

    while ($row = mysqli_fetch_array($result)) {

        $ProductName = $row["ProductName"];
        $UserName = $row["UserName"];
        $path = $row["FilePath"] . "/{$ProductName}" . "_1_{$UserName}" . ".jpeg";
        $imageFile = file_get_contents($path);
        $imageFileData = base64_encode($imageFile);
        array_push($response, 
                    array(
                        'success' => true,
                        'ProductNumber' => $row["ProductNumber"],
                        'ProductName' => $row["ProductName"],
                        'ProductDesc' => $row["ProductDesc"],
                        'ProductOwner' => $row["UserName"],
                        'ProductPrice' => $row["ProductPrice"],
                        'TradeLocation' => $row["TradeLocation"],
                        'AddedDate' => $row["AddedDate"],
                        'ImageFile' => $imageFileData
                    ));
    }

    echo json_encode(array("Products"=>$response));
?>