<?php
include "connect.php";
$token = $_POST['token'];
$iddonhang = $_POST['id'];
// check data email 
$query =  'UPDATE `donhang` SET `zalo`= "'.$token.'" WHERE `id` =' . $iddonhang . '';
$data = mysqli_query($conn, $query);
if ($data == true) {
       $arr = [
              'success' => true,
              'message' => "Thanh cong",

       ];
} else {
       $arr = [
              'success' => false,
              'message' => "Khong thanh cong",
       ];
}
print_r(json_encode($arr));
