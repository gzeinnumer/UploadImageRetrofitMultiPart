<?php 

	//importing dbDetails file 
	define('HOST','localhost');
	define('USER','root');
	define('PASS','');
	define('DB','');
	
	
     $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect...');
             
     //response array 
     $response = array(); 
    
    $id = $_POST['id'];
    
    $sql = "DELETE FROM exer_oploadfile_multipart_crud WHERE id='$id'";
    
	if(mysqli_query($con,$sql)){
	   $response["result"] = "1";
       $response["msg"] = "data makanan berhasil dihapus!!";
       echo json_encode($response);
	}else{
	   $response["result"] = "0";
       $response["msg"] = "maaf! gagal menghapus data!";
		echo json_encode($response);
	}
	
	mysqli_close($con);

