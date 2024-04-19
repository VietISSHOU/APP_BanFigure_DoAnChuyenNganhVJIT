<?php
include "connect.php";
$trangthai = $_POST['trangthai'];
$id = $_POST['id'];
// check data email 
$query =  'UPDATE `donhang` SET `trangthai`=' . $trangthai . ' WHERE `id` =' . $id . '';
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
