<?php

//Getting the DbConnect.php file
require_once 'DBConnect.php';

class DBOperation
{
    //Database connection link
    private $con;
 
    //Class constructor
    function __construct()
    {
        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();
 
        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }
 
    /*
    * The create operation
    * When this method is called a new record is created in the database
    */
    function createNote($rating, $name, $nose, $palate, $finish, $extra){
        $stmt = $this->con->prepare("INSERT INTO whis_notes (rating, name, nose, palate, finish, extra) VALUES (?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("isssss", $rating, $name, $nose, $palate, $finish, $extra);
        if($stmt->execute())
        return true; 
        return false; 
    }
    
    /*
    * The read operation
    * When this method is called it is returning all the existing record of the database
    */
    function getNotes(){
        $stmt = $this->con->prepare("SELECT id, rating, name, nose, palate, finish, extra FROM whis_notes");
        $stmt->execute();
        $stmt->bind_result($id, $rating, $name, $nose, $palate, $finish, $extra);

        $notes = array(); 

        while($stmt->fetch()){
            $note  = array();
            $note['id'] = $id; 
            $note['rating'] = $rating;
            $note['name'] = $name; 
            $note['nose'] = $nose; 
            $note['palate'] = $palate; 
            $note['finish'] = $finish; 
            $note['extra'] = $extra;
            
            array_push($notes, $note); 
        }

        return $notes; 
    }
    
    /*
    * The update operation
    * When this method is called the record with the given id is updated with the new given values
    */
    function updateNote($id, $rating, $name, $nose, $palate, $finish, $extra){
        $stmt = $this->con->prepare("UPDATE whis_notes SET rating = ?, name = ?, nose = ?, palate = ?, finish = ?, extra = ? WHERE id = ?");
        $stmt->bind_param("isssssi", $rating, $name, $nose, $palate, $finish, $extra, $id);
        if($stmt->execute())
        return true; 
        return false; 
    }
    
    
    /*
    * The delete operation
    * When this method is called record is deleted for the given id 
    */
    function deleteNote($id){
        $stmt = $this->con->prepare("DELETE FROM whis_notes WHERE id = ? ");
        $stmt->bind_param("i", $id);
        if($stmt->execute())
        return true; 

        return false; 
    }

    /*
    * The get nose categories operation
    */

    function getNose(){
        $stmt = $this->con->prepare("SELECT nose_id, nose FROM whis_nose");
        $stmt->execute();
        $stmt->bind_result($nose_id, $nose);

        $nose_cats = array(); 
        
        while($stmt->fetch()){
            $nose_cat  = array();
            $nose_cat['nose_id'] = $nose_id;
            $nose_cat['nose'] = $nose;
            
            array_push($nose_cats, $nose_cat); 
        }
        return $nose_cats; 
    }

    function getName(){
        $stmt = $this->con->prepare("SELECT name_id, name FROM whis_names");
        $stmt->execute();
        $stmt->bind_result($name_id, $name);

        $name_cats = array(); 
        
        while($stmt->fetch()){
            $name_cat  = array();
            $name_cat['name_id'] = $name_id;
            $name_cat['name'] = $name;
            
            array_push($name_cats, $name_cat); 
        }
        return $name_cats; 
    }

    function getPalate(){
        $stmt = $this->con->prepare("SELECT palate_id, palate FROM whis_palate");
        $stmt->execute();
        $stmt->bind_result($palate_id, $palate);

        $palate_cats = array(); 
        
        while($stmt->fetch()){
            $palate_cat  = array();
            $palate_cat['palate_id'] = $palate_id;
            $palate_cat['palate'] = $palate;
            
            array_push($palate_cats, $palate_cat); 
        }
        return $palate_cats; 
    }

    function getFinish(){
        $stmt = $this->con->prepare("SELECT finish_id, finish FROM whis_finish");
        $stmt->execute();
        $stmt->bind_result($finish_id, $finish);

        $finish_cats = array(); 
        
        while($stmt->fetch()){
            $finish_cat  = array();
            $finish_cat['finish_id'] = $finish_id;
            $finish_cat['finish'] = $finish;
            
            array_push($finish_cats, $finish_cat); 
        }
        return $finish_cats; 
    }
}

?>