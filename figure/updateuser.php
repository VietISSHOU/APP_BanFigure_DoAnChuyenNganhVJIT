<?php
include "connect.php";
$id = $_POST['id'];
$live = $_POST['live'];
// check data email 
if($live == 0){
       $query =  'UPDATE `user` SET `islive`= '.$live.' WHERE `id` =' . $id . '';
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
}else if($live == 1){
       $query =  'UPDATE `user` SET `islive`= '.$live.'  WHERE `id` =' . $id . '';
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
}
print_r(json_encode($arr));