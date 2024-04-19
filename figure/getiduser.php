<?php
include "connect.php";
$status = $_POST['status'];
if ($status == 0) {
	$query = "SELECT * FROM `user` WHERE `status` =" . $status;
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
	} else {
		$arr = [
			'success' => false,
			'message' => "khang thanh cong",
			'result' => $result
		];
	}
}
print_r(json_encode($arr));
