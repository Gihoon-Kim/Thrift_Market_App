<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $ProductName = $_POST["productName"];
    $ProductDesc = $_POST["productDesc"];
    $productPrice = $_POST["productPrice"];
    $productOwner = $_POST["productOwner"];
    $Location = $_POST["location"];
    $AddedDate = $_POST["AddedDate"];
    $NumOfImages = $_POST["numOfImages"];
    
    //Receive JSON
    $JSON_Received = $_POST["images"];
    //Decode JSON
    $obj = json_decode($JSON_Received, true);

    $sql = "INSERT INTO product (
        ProductName, 
        ProductDesc, 
        ProductPrice, 
        ProductOwner, 
        tradeLocation,
        AddedDate) 
        VALUES (
            '$ProductName', 
            '$ProductDesc', 
            '$productPrice',
            '$productOwner', 
            '$Location',
            '$AddedDate')";
    
    $result = mysqli_query($conn, $sql);
    $response = array();
    $response["success"] = true;


    $sql = "SELECT UserName, UserEmail
            FROM user
            WHERE UserNumber = '$productOwner';";
    $result = mysqli_query($conn, $sql);

    $UserName;
    $UserEmail;
    while ($row = mysqli_fetch_array($result)) {

        $UserName = $row["UserName"];
        $UserEmail = $row["UserEmail"];
    }

    $response["UserName"] = $UserName;
    $response["UserEmail"] = $UserEmail;
    $response["NumOfImages"] = $NumOfImages;
    echo json_encode($response);
    
    $root = $_SERVER["DOCUMENT_ROOT"];

    $path = $root . "/users/{$UserName}_{$UserEmail}";

    for ($i = 0; $i < $NumOfImages; $i = $i + 1) {
        if (file_put_contents($path . "/{$ProductName}" . "_" . ($i+1) . ".jpeg", base64_decode($obj["Count".($i+1)]))) {

       }
    }
?>