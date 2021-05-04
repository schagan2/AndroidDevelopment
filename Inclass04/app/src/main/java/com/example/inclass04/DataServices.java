package com.example.inclass04;

import java.io.Serializable;
import java.util.HashMap;

public class DataServices {
    private static HashMap<String, Account> accounts = new HashMap<String, Account>(){{
        put("a@a.com", new Account("Alice Smith", "a@a.com", "test123"));
        put("b@b.com", new Account("Bob Smith", "b@b.com", "test123"));
        put("c@c.com", new Account("Charles Smith", "c@c.com", "test123"));
    }};

    public static Account login(String email, String password){

        if(email == null || email.isEmpty() ){
            return null;
        }

        if(!accounts.containsKey(email.trim().toLowerCase())){
            return null;
        }

        Account account = accounts.get(email.trim().toLowerCase());

        if(account == null || !account.getPassword().equals(password)){
            return null;
        }
        return account;
    }

    public static Account register(String name, String email, String password){

        if(name == null || name.isEmpty() ){
            return null;
        }

        if(email == null || email.isEmpty() ){
            return null;
        }

        if(password == null || password.isEmpty() ){
            return null;
        }

        if(accounts.containsKey(email.trim().toLowerCase())){
            return null;
        }

        Account account = new Account(name, email.trim().toLowerCase(), password);

        accounts.put(email.trim().toLowerCase(), account);

        return account;
    }

    public static Account update(Account oldAccount, String name, String password){
        if(oldAccount == null){
            return null;
        }

        if(name == null || name.isEmpty() ){
            return null;
        }

        if(password == null || password.isEmpty() ){
            return null;
        }

        if(oldAccount.getEmail() == null || oldAccount.getEmail().isEmpty() ){
            return null;
        }

        if(!accounts.containsKey(oldAccount.getEmail().trim().toLowerCase())){
            return null;
        }

        String email = oldAccount.getEmail().trim().toLowerCase();

        Account account = new Account(name,email,password);
        accounts.put(email, account);

        return account;
    }

    public static class Account implements Serializable {
        private String name, email, password;
        public Account(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
