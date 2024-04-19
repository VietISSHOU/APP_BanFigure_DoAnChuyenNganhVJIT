<?php
include "connect.php";
$tensp = $_POST['tensp'];
$gia = $_POST['gia'];
$hinhanh = $_POST['hinhanh'];
$mota = $_POST['mota'];
$loai = $_POST['loai'];
$sltonkho = $_POST['slsp'];
// check data email 
$query =  'INSERT INTO `sanphammoi`(`tensp`, `gia`, `hinhanh`, `mota`, `loai`, `sltonkho`) VALUES ("' . $tensp . '","' . $gia . '","' . $hinhanh . '","' . $mota . '",' . $loai . ','.$sltonkho.')';
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
