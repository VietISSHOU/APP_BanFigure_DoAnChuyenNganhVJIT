
<?php
include "connect.php";
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'PHPMailer/src/Exception.php';
require 'PHPMailer/src/PHPMailer.php';
require 'PHPMailer/src/SMTP.php';
  $email = $_POST['email'];
  $query = 'SELECT * FROM `user` WHERE `email` = "'.$email.'"';
  $data = mysqli_query($conn, $query);
  $result = array();
  while ($row = mysqli_fetch_assoc($data)) {
  $result[] = ($row);
  // code....
  }
  if(empty($result)){
      $arr = [
    'success' => false,
    'message' => "Sai dia chi mail",
    'result' => $result
    ];
  }else{
    // send mail
  
    $email = ($result[0]["email"]);
    $matkhau = ($result[0]["pass"]);

    $link="<a href='http://192.168.1.6/figure/reset_pass.php?key=".$email."&reset=".$matkhau."'>Truy cập link để lấy lại mật khẩu !</a>";
    
    $mail = new PHPMailer();
    $mail->CharSet =  "utf-8";
    $mail->IsSMTP();
    // enable SMTP authentication
    $mail->SMTPAuth = true;                  
    // GMAIL username
    $mail->Username = "appfigurenvt@gmail.com";
    // GMAIL password
    $mail->Password = "vfzy cxru qocn ghor"; // mat khau cua mail
    
    $mail->SMTPSecure = "ssl";  
    // sets GMAIL as the SMTP server
    $mail->Host = "smtp.gmail.com";
    // set the SMTP port for the GMAIL server
    $mail->Port = "465";
    $mail->From = 'appfigurenvt@gmail.com';  // mail nguoi nhan 
    $mail->FromName ='App Figure';
    $mail->AddAddress($email, 'reciever_name');
    $mail->Subject  =  'Reset Password';
    $mail->IsHTML(true);
    $mail->Body    = $link;
    if($mail->Send())
    {
      $arr = [
        'success' => true,
        'message' => "Vui lòng kiểm tra email",
        'result' => $result
    ];
    }
    else
    {
      $arr = [
        'success' => false,
        'message' => "khong thanh cong",
        'result' => $mail -> ErrorInfo
      ];
    }
  } 
  print_r(json_encode($arr));

  
?>
