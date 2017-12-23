<?php 
 
 //getting the dboperation class
 require_once 'DBOperations.php';
 
 //function validating all the paramters are available
 //we will pass the required parameters to this function 
 function areParamsAvailable($params){
    //assuming all parameters are available 
    $available = true; 
    $missingparams = ""; 
    
    foreach($params as $param){
        if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
        $available = false; 
        $missingparams = $missingparams . ", " . $param; 
        }
    }
    
    //if parameters are missing 
    if(!$available){
        $response = array(); 
        $response['error'] = true; 
        $response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';
        
        //displaying error
        echo json_encode($response);
        
        //stopping further execution
        die();
    }
 }
 
 //an array to display response
 $response = array();
 
 //if it is an api call 
 //that means a get parameter named api call is set in the URL 
 //and with this parameter we are concluding that it is an api call
 if(isset($_GET['apicall'])){
    switch($_GET['apicall']){
        //the CREATE operation
        //if the api call value is 'createnote'
        //we will create a record in the database
        case 'createnote':
        //first check the parameters required for this request are available or not 
        areParamsAvailable(array('rating', 'name', 'nose', 'palate', 'finish', 'extra'));
        
        //creating a new dboperation object
        $db = new DBOperation();
        
        //creating a new record in the database
        $result = $db->createNote(
        $_POST['rating'],
        $_POST['name'],
        $_POST['nose'],
        $_POST['palate'],
        $_POST['finish'],
        $_POST['extra']
        );
        
        
        //if the record is created adding success to response
        if($result){
            //record is created means there is no error
            $response['error'] = false; 
            
            //in message we have a success message
            $response['message'] = 'Note added successfully';
            
            //and we are getting all the notes from the database in the response
            $response['notes'] = $db->getNotes();
        }
        else{
            //if record is not added that means there is an error 
            $response['error'] = true; 
            
            //and we have the error message
            $response['message'] = 'Error occurred please try again';
        }
        
        break; 
        
        //the READ operation
        //if the call is getNotes
        case 'getnotes':
        $db = new DBOperation();
        $response['error'] = false; 
        $response['message'] = 'Request successfully completed';
        $response['notes'] = $db->getNotes();
        break; 
        
        // the READ operation
        // if the call is getNose
        case 'getnose':
        $db = new DBOperation();
        $response['error'] = false;
        $response['message'] = 'Request successfully completed';
        $response['notes'] = $db->getNose();
        break;

        // the READ operation
        // if the call is getName
        case 'getname':
        $db = new DBOperation();
        $response['error'] = false;
        $response['message'] = 'Request successfully completed';
        $response['notes'] = $db->getName();
        break;

        // the READ operation
        // if the call is getPalate
        case 'getpalate':
        $db = new DBOperation();
        $response['error'] = false;
        $response['message'] = 'Request successfully completed';
        $response['notes'] = $db->getPalate();
        break;

        // the READ operation
        // if the call is getFinish
        case 'getfinish':
        $db = new DBOperation();
        $response['error'] = false;
        $response['message'] = 'Request successfully completed';
        $response['notes'] = $db->getFinish();
        break;
        
        //the UPDATE operation
        case 'updatenote':
        areParamsAvailable(array('id','rating','name','nose','palate', 'finish', 'extra'));
        $db = new DBOperation();
        $result = $db->updateNote(
            $_POST['id'],
            $_POST['rating'],
            $_POST['name'],
            $_POST['nose'],
            $_POST['palate'],
            $_POST['finish'],
            $_POST['extra']        
        );
        
        if($result){
            $response['error'] = false; 
            $response['message'] = 'Note updated successfully!';
            $response['notes'] = $db->getNotes();
        } 
        else {
            $response['error'] = true; 
            $response['message'] = 'Error occurred please try again';
        }
        break; 
        
        //the delete operation
        case 'deletenote':
        
        //for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
        if(isset($_GET['id'])){
            $db = new DBOperation();
            if($db->deleteNote($_GET['id'])){
                $response['error'] = false; 
                $response['message'] = 'Note deleted successfully!';
                $response['notes'] = $db->getNotes();
            }
            else {
                $response['error'] = true; 
                $response['message'] = 'Error occurred please try again';
            }
        }
        else {
            $response['error'] = true; 
            $response['message'] = 'Nothing to delete, please provide an ID';
        }
        break; 
    }
    
 }
 else {
    //if it is not api call 
    //pushing appropriate values to response array 
    $response['error'] = true; 
    $response['message'] = 'Invalid API Call';
 }
 
 //displaying the response in json structure 
 echo json_encode($response);