package com.example.administrator.age_101;

public class User {

    //Singleton
    private static User instance;

    private String cardId;
    private String name;
    private String surname;
    private String mail;
    private int type;


    private User(){}

    public static User getInstance() {
        return instance;
    }

    public void setInstance(){
        instance = this;
    }


    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
