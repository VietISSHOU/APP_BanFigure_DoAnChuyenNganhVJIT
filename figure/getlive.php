<?php
include "connect.php";
$status = $_POST['status'];
$live = $_POST['live'];
if ($status == 1) {
	$query = "SELECT * FROM `user` WHERE `status` = ".$status."  AND `islive` =".$live." " ;
	$data = mysqli_query($conn, $query);
	$result = array();
	while ($row = mysqli_fetch_assoc($data)) {
		$result[] = ($row);
		// code....
	}
	if (!empty($result)) {
		$arr = [
			'success' => true,
			'message' => "thanh cong",
			'result' => $result
		];
	} else  {
	$query = "SELECT * FROM `user` WHERE `status` = 2  AND `islive` =".$live." " ;
	$data = mysqli_query($conn, $query);
	$result = array();
	while ($row = mysqli_fetch_assoc($data)) {
		$result[] = ($row);
		// code....
	}
	if (!empty($result)) {
		$arr = [
			'success' => true,
			'message' => "thanh cong",
			'result' => $result
		];
	} else  {
		$arr = [
			'success' => false,
			'message' => "khong thanh cong",
			'result' => $result
		];
	}
		
	}
}
print_r(json_encode($arr));
