<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $sql = "SELECT p.ProductNumber, p.ProductName, p.ProductDesc, p.ProductPrice, u.UserName, p.tradeLocation, p.AddedDate, i.FilePath, i.imageName
	        FROM product p
	        JOIN user u
            JOIN images i
	        ON p.ProductOwner = u.UserNumber
	        WHERE i.ProductName = p.ProductName AND i.ImageNumber = 0;";

    $result = mysqli_query($conn, $sql);

    $response = array();

    while ($row = mysqli_fetch_array($result)) {

        $ProductName = $row["ProductName"];
        $UserName = $row["UserName"];
        $ImageName = $row["imageName"];
        $path = $row["FilePath"] . "/{$ImageName}" . ".jpeg";
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
                        'TradeLocation' => $row["tradeLocation"],
                        'AddedDate' => $row["AddedDate"],
                        'ImageFile' => $imageFileData
                    ));
    }

    echo json_encode(array("Products"=>$response));
?>