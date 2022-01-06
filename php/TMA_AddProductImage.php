<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $ProductName = $_POST["productName"];
    $NumOfImages = $_POST["numOfImages"];
    $productOwner = $_POST["productOwner"];

    //Receive JSON
    $JSON_Received = $_POST["images"];
    //Decode JSON
    $obj = json_decode($JSON_Received, true);
    
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

    $response = array();

    $response["UserName"] = $UserName;
    $response["success"] = false;
    $root = $_SERVER["DOCUMENT_ROOT"];

    $path = $root . "/users/{$UserName}_{$UserEmail}";

    for ($i = 0; $i < $NumOfImages; $i += 1) {

        if (file_put_contents($path . "/{$ProductName}" . "_" . ($i+1) . "_{$UserName}" . ".jpeg", base64_decode($obj["Count".($i+1)]))) {
            
            $response["success"] = true;
        } else {

            $response["success"] = false;
            return;
        }

        $imageName = $ProductName . "_" . ($i + 1) . "_{$UserName}";
        
        $result = mysqli_query($conn, "SELECT * FROM images WHERE imageName='$imageName';");
        $num_rows = mysqli_num_rows($result);

        if ($num_rows) {

            break;
        } else {
            if ($stmt = $conn->prepare("INSERT INTO images (imageName, ProductName, FilePath, ImageNumber) VALUES (?, ?, ?, ?)")) {

                $stmt->bind_param(
                    "sssi", 
                    $imageName, 
                    $ProductName,
                    $path,
                    $i
                );
                $stmt->execute();
                
                $response["success"] = true;
            } else {

                $response["success"] = false;
            }
        }
    }

    echo json_encode($response); 
?>