<?php 


	//importing dbDetails file
	define('HOST','localhost');
	define('USER','root');
	define('PASS','');
	define('DB','');
	
     $upload_path = 'uploads/';
     $server_ip = gethostbyname(gethostname());
     $upload_url = 'http://'.$server_ip.'/exer_oploadfile_multipart_crud/'.$upload_path; 
     $response = array(); 
     
     if($_SERVER['REQUEST_METHOD']=='POST'){
         if(isset($_POST['name']) and isset($_FILES['image']['name'])){
         $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect...');
        
         $name = $_POST['name'];
        
         $fileinfo = pathinfo($_FILES['image']['name']);
        
         //$extension = $fileinfo['extension'];
        $extension = "jpg";
        
         //$file_url = $upload_url . getFileName() . '.' . $extension;
         $file_url = getFileName() . '.' . $extension;
         //$file_url = $_FILES['image']['name'];
		
		 $file_path = $upload_path . getFileName() . '.'. $extension; 
         
         try{
             move_uploaded_file($_FILES['image']['tmp_name'],$file_path);
             $sql = "INSERT INTO exer_oploadfile_multipart_crud (`id`, `url`, `name`) VALUES (NULL, '$file_url', '$name');";
             
             if(mysqli_query($con,$sql)){
                 $response['error'] = false; 
                 $response['url'] = $file_url; 
                 $response['name'] = $name;
             }
         }catch(Exception $e){
             $response['error']=true;
             $response['message']=$e->getMessage();
         } 
         echo json_encode($response);
         
         mysqli_close($con);
         }else{
             $response['error']=true;
             $response['message']='Please choose a file';
         }
     }
     
     function getFileName(){
         $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect...');
         $sql = "SELECT max(id) as id FROM exer_oploadfile_multipart_crud";
         $result = mysqli_fetch_array(mysqli_query($con,$sql));
         
         mysqli_close($con);
         if($result['id']==null)
            return 1; 
         else 
            return ++$result['id']; 
     }
     