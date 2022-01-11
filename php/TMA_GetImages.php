<?php
    $db_name = "hoonyhosting";
    $username = "hoonyhosting";
    $password = "wjsghkrl1!";
    $servername = "localhost";

    $conn = mysqli_connect($servername, $username, $password, $db_name);

    $ProductName = $_POST["productName"];
    $productOwner = $_POST["productOwner"];

    $sql = "SELECT FilePath, imageName
            FROM images
            WHERE ProductName = '$ProductName' AND ProductOwner = '$productOwner';";

    $result = mysqli_query($conn, $sql);

    $response = array();

    while ($row = mysqli_fetch_array($result)) {

        $i = 1;
        $ImageName = $row["imageName"];
        $path = $row["FilePath"] . "/{$ImageName}" . ".jpeg";
        $imageFile = file_get_contents($path);
        $imageFileData = base64_encode($imageFile);
        array_push($response, 
                    array(
                        'success' => true,
                        'ImageFile' => $imageFileData
                    ));
    }

    echo json_encode($response);
?>