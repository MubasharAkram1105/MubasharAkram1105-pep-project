package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerAccount);
        app.post("/login", this::loginAccount);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageFromId);
        app.delete("/messages/{message_id}", this::deleteMessageWithId);
        app.patch("/messages/{message_id}", this::updateMessageWithId);
        app.get("/accounts/{accounts_id}/messages", this::getAllMessagesFromUser);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    //Register account 
    private void registerAccount(Context ctx) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        //using method to add account 
        Account registeredAccount = accountService.addAccount(account);
        //if successful json will return string showing account,if not error 400
        if(registeredAccount != null){
            ctx.json(mapper.writeValueAsString(registeredAccount));
        } else {
            ctx.status(400);
        }
    } 
    
    //login account 
    private void loginAccount(Context ctx) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        //using check login method 
        account = accountService.checkLogin(account);
        //if successful return account as json if not error 401
        if(account != null){
            ctx.json(mapper.writeValueAsString(account));
        } else {
            ctx.status(401);
        }
    } 
    //create message
    private void createMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        //using create method function
        Message newMessage = messageService.createMessage(message);
        //if successful return new message if not error 400
        if(newMessage != null){
            ctx.json(mapper.writeValueAsString(newMessage));
        } else {
            ctx.status(400);
        }
    }
    //get all messages
    private void getAllMessages(Context ctx){
        
        //used method get all messages to fill list ,then displays it as json
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
    //get message from id 
    private void getMessageFromId(Context ctx){
        //getting int using path param and turning it  from a string to int and saving it to messageID var
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        //using the new var to get the message with its id
        Message message = messageService.getMessageFromID(messageId);
        //if found show message, if not error 200
        if(message != null){
            ctx.json(message);
        } else {
            ctx.status(200);
        }

    }
    //delete message with id 
    private void deleteMessageWithId(Context ctx){
        //getting int using path param and turning it  from a string to int and saving it to messageID var
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        //using the new var to get the message with its id
        Message message = messageService.getMessageFromID(messageId);
        //if found show message first then delete , if not error 200
        if(message != null){
            ctx.json(message);
            messageService.deleteMessageWithId(messageId);
        } else {
            ctx.status(200);
        }
    }

    private void updateMessageWithId(Context ctx) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));

            message.setMessage_id(messageId);
        
            Message updatedMessage = messageService.updateMessageWithId(message);

        if(updatedMessage != null){
            ctx.json(updatedMessage);
        } else{
            ctx.status(400);
        }
    }

    private void getAllMessagesFromUser(Context ctx){
        //parse int from path param
        int accountId = Integer.parseInt(ctx.pathParam("accounts_id"));
        //send int to method 
        List<Message> messages = messageService.getAllMessagesFromUser(accountId);
        //if list returned is not empty return as json 
        if(!messages.isEmpty()){
            ctx.json(messages);
        } else { 
            //if empty show empty array list as json then send status 200 
            ctx.json(messages);
            ctx.status(200);
        }
    }
}