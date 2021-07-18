package com.example.a101guesthouse;

public class User {

    public String firstname, lastname, email, username, country;

    public User(){

    }

    public User (String firstname, String lastname, String email, String username, String country) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.country = country;
    }
}
