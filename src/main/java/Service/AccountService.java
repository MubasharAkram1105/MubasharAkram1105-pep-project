package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.ArrayList;
import java.util.List;

public class AccountService {

    
    private AccountDAO accountDAO;
    //no args constructers 
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    //constructor when dao is provided
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    //retrieve all accounts 
    public List<Account> getAllAccounts(){
        //fill list with accounts from database
        List<Account> returnAllAccounts = accountDAO.getAllAcounts();
        //return new list with all accounts 
        return returnAllAccounts;

    }

    //add new account to database if standards are meet then return new added account , if not send back account to user with message
    public Account addAccount(Account account){
       
            if(checkAccountInfo(account) == true){
            //setting info for new account from account param
            Account addedAccount = accountDAO.addNewAccount(account);
            //returning new account
            return addedAccount;
            }
            else {
                return null;
            }


    }

    public Account checkLogin(Account account){
        //use check login DAO method giving account paramaters 
        Account validLogin = accountDAO.checkLogin(account.getUsername(), account.getPassword());
        //if not successful send back null 
        if(validLogin == null){
            return null;
        //if successful send back account that tried to login
        } else {
            return validLogin;
        }
 
    }
    //checking if register user followed proper account steps 
    public boolean checkAccountInfo(Account account){

        String username = account.getUsername();
        String password = account.getPassword();

        //username cant be empty , password cannot be empty or less than 4, also username cannot exist
        //true if none of the if statements are hit 
        if(username == null){
            return false;
        }
        if(username == ""){
            return false;
        }
        if(password == null){
            return false;
        }
        if (password.length() < 4){
            return false;
        }
        if (accountDAO.getAccountFromUsername(username) != null ){
            return false;
        } 
        return true;
    }

}
