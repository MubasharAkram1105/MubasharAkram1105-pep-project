package Service;

import DAO.MessageDAO;
import Model.Message;
import Model.Account;
import DAO.AccountDAO;

import java.util.ArrayList;
import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;
    public AccountDAO accountDAO;
    //no arg constructers 
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();

    }
    //constructer when dao is provided 
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    //create new message 
    public Message createMessage(Message message){
        //checking if message follows guidelines before creating 
        if(checkMessageInfo(message) == true){
            Message newMessage = messageDAO.createMessage(message);
            return newMessage;
        //if message does not follow guidelines return null
        } else{
            return null;
        }
    }
    //making sure message follow guidelines, cannot be blank or more than 225 chars and user must exist 
    public boolean checkMessageInfo(Message message){

        Account existingUser = accountDAO.getAccountFromID(message.getPosted_by());

        if(message.getMessage_text()== ""){
            return false;
        }
        if(message.getMessage_text().length() > 225){
            return false;
        }
        if(existingUser == null){
            return false;
        }

        return true;
    }
    //get all messages 
    public List<Message> getAllMessages(){
        //fill list with all messages from database
        List<Message> returnAllMessages = messageDAO.getAllMessages();
        //return new list with all messages 
        return returnAllMessages;
    }
    //get all from user id 
    public Message getMessageFromID(int messageId){

        Message message = messageDAO.getWithId(messageId);

        return message;
    }
    //DOES NOT WORK
    public List<Message> getAllMessagesFromUser(int accountId){
        //fill list with all messages from database
        List<Message> returnAllMessagesFromUser = messageDAO.getAllMessagesFromUser(accountId);
        //return new list with all messages
        return returnAllMessagesFromUser;
    }
    //delete with message id 
    public int deleteMessageWithId(int messageId){
        
        int deleted = messageDAO.deleteWithId(messageId);

        return deleted;

    }
    //Update with message id 
    public Message updateMessageWithId(Message message){
    //set message to update to message sent by param
       Message updateMessage = messageDAO.getWithId(message.getMessage_id());
    // if message didnt exist it would be null send back null values
    if(updateMessage == null){
        return null;
    }
    //if not null update text 
       updateMessage.setMessage_text(message.getMessage_text());
    //now check if the text follows rules 
    if(checkMessageInfo(updateMessage)){
        //update text in database 
       messageDAO.updateWithId(updateMessage);
        //return updated message 
       return updateMessage;
    }  
    //if not succesfull return null
    return null;
    
    }
}
