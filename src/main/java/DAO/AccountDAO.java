package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountDAO {
    
    //function to get all account usernames to check if user registration can be done and if login checks out 
    public List<Account> getAllAcounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            //Select all data 
            String sql = "SELECT * FROM account;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //execute sql
            ResultSet rs = preparedStatement.executeQuery();
            //add to list until no more data 
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //return list of all accounts
        return accounts;
    }

    //add new user 
    public Account addNewAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            // add user name and password to the database        
            String sql = "INSERT INTO account (username, password) VALUES (?,?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //send values to prepared statement 
            preparedStatement.setString(1,account.getUsername());
            preparedStatement.setString(2,account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            //get new account data then return if succesful 
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //return null if add was unsuccessful
        return null;
    }

    //searches for account with specific username
    public Account getAccountFromUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //sql to get all from specific username 
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //get variable from method param
            preparedStatement.setString(1,username);

            ResultSet rs = preparedStatement.executeQuery();
            //get account data then return if succesful 
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //return null if method couldnt connect or find account
        return null;
    }

    public Account checkLogin(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //checking login by finding an account where username and password both match with method params
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //giving sql the method param variables
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);

            ResultSet rs = preparedStatement.executeQuery();
            //get data and return account if successful
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //if not successful return null
        return null;
    }
    public Account getAccountFromID(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //get account from id 
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //give method params to sql script 
            preparedStatement.setInt(1,account_id);

            ResultSet rs = preparedStatement.executeQuery();
            //return data from account if found 
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //if not succesful return null
        return null;
    }


}


