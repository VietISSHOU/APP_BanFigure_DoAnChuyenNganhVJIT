<?php

include "connect.php";

if ($_GET['key'] && $_GET['reset']) {
       $email = $_GET['key'];
       $pass = $_GET['reset'];
       $query = "select email,pass from user where email='$email' and pass='$pass'";
       $data = mysqli_query($conn, $query);


       if ($data == true) {
?>
              <form method="post" action="submit_new.php" >
                     <label for="exampleInputEmail1" >Email:</label><br>
                     <input type="text" name="email" style="width:500px; height: 50px;" readonly="False" value="<?php echo $email; ?>"><br> 
                     <label for="exampleInputEmail1">Password new:</label><br> 
                     <input type="password" style="width:500px; height: 50px;" name='password'><br> 
                     <input type="submit" name="submit_password">
              </form>
             
<?php
       }
}
?>