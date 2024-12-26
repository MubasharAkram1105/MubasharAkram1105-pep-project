package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageDAO {
    
    public Message createMessage(Message message){

        Connection connection = ConnectionUtil.getConnection();
        try {
            // add user name and password to the database        
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //send values to prepared statement 
            preparedStatement.setInt(1,message.getPosted_by());
            preparedStatement.setString(2,message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            //return created message 
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //of unsuccesful return null
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Select all data 
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            //add data to list until no more data
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //send list
        return messages;
    }

    public Message getWithId(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //sql script to get message from id 
            String sql = "SELECT * FROM message WHERE message_id =?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //give sql prepared statement the method paramaters 
            preparedStatement.setInt(1,message_id);

            ResultSet rs = preparedStatement.executeQuery();
            //return message with id 
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //if not found return null
        return null;
    }

    public int deleteWithId(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //delete message sql script 
            String sql = "DELETE * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //give sql the method paramaters
            preparedStatement.setInt(1,message_id);
            //delete and send back rows effected 
            return preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //if not succesful send back 0 rows affeceted 
        return 0;
    }

    public int updateWithId(Message message){
        Connection connection = ConnectionUtil.getConnection();
        int updatedRows = 0;
        try {
            //SQL for update record 
            String sql = "UPDATE message SET posted_by =?, message_text =?, time_posted_epoch =? WHERE message_id =?;";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //set params using the message given to us in method params 
            preparedStatement.setInt(1,message.getPosted_by());
            preparedStatement.setString(2,message.getMessage_text());
            preparedStatement.setLong(3,message.getTime_posted_epoch());
            preparedStatement.setInt(4,message.getMessage_id());
            //update and then return amount of rows updated 
            updatedRows = preparedStatement.executeUpdate();
            return updatedRows; 
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //if not succesful return 0 rows updated 
        return 0;
    }

    public List<Message> getAllMessagesFromUser(int accountId){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //query to find messages posted by specific account 
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //set value in prepared statement to account id given to us , account id = posted by 
            preparedStatement.setInt(1, accountId);
            //execute query 
            ResultSet rs = preparedStatement.executeQuery();
            //loop thru result set and add to array until no more values 
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            //return array 
            return messages;  
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //if not complete send back empty array 
       return new ArrayList<>();
       
    }
}
