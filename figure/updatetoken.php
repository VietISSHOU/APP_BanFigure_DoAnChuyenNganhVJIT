<?php
include "connect.php";
$token = $_POST['token'];
$id = $_POST['id'];
// check data email 
$query =  'UPDATE `user` SET `token`="' . $token . '" WHERE `id` =' . $id . '';
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
