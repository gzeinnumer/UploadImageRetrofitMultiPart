<?php 


	//importing dbDetails file
	define('HOST','localhost');
	define('USER','root');
	define('PASS','');
	define('DB','');
	
    //this is our upload folder 
     $upload_path = 'uploads/';
     
     //Getting the server ip 
     $server_ip = gethostbyname(gethostname());
     
     //creating the upload url 
     $upload_url = 'http://'.$server_ip.'/exer_oploadfile_multipart_crud/'.$upload_path; 
     
     //response array 
     $response = array(); 
     
     
     if($_SERVER['REQUEST_METHOD']=='POST'){
         //connecting to the database 
             $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect...');
             
             //getting name from the request 
             $name = $_POST['name'];
             
             //getiing id
             $id = $_POST['id'];
             
             //getting image new/not
             $isNewImage = $_POST['isNewImage'];
             
             
             //trying to save the file in the directory 
             try{
             //saving the file 
                if($isNewImage == "new"){
                     //getting file info from the request 
                     $fileinfo = pathinfo($_FILES['image']['name']);
                     
                     //getting the file extension 
                    //  $extension = $fileinfo['extension'];
                     $extension = "jpg";
                     
                     //file url to store in the database 
                     //$file_url = $upload_url . getFileName() . '.' . $extension;
                     $file_url = getFileName() . '.' . $extension;
                     //$file_url = $_FILES['image']['name'];
            			
                     //file path to upload in the server 
                     $file_path = $upload_path . getFileName() . '.'. $extension; 
                     
                     move_uploaded_file($_FILES['image']['tmp_name'],$file_path);
                     $sql = "UPDATE exer_oploadfile_multipart_crud SET url='$file_url', name='$name' WHERE id='$id';";
                     
                     //adding the path and name to database 
                     if(mysqli_query($con,$sql)){
                     
                         //filling response array with values 
                         $response['sukses'] = true; 
                         $response['url'] = $file_url; 
                         $response['name'] = $name;
                     }
                } else { //old
                     $sql = "UPDATE exer_oploadfile_multipart_crud SET name='$name' WHERE id='$id';";
                     
                     //adding the path and name to database 
                     if(mysqli_query($con,$sql)){
                     
                         //filling response array with values 
                         $response['sukses'] = true; 
                         $response['name'] = $name;
                     }
                }
                
             //if some error occurred 
             }catch(Exception $e){
                 $response['error']=true;
                 $response['message']=$e->getMessage();
             } 
             //displaying the response 
             echo json_encode($response);
             
             //closing the connection 
             mysqli_close($con);
     }
     
     /*
     We are generating the file name 
     so this method will return a file name for the image to be upload 
     */
     function getFileName(){
         $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect...');
         $sql = "SELECT max(id) as id exer_oploadfile_multipart_crud";
         $result = mysqli_fetch_array(mysqli_query($con,$sql));
         
         mysqli_close($con);
         if($result['id']==null)
            return 1; 
         else 
            return ++$result['id']; 
     }
     