package com.ism.models;

public class Boss extends User {

    public Boss(long userId, String firstName, String email, String password, String phone, String status) {
        super(userId, firstName, email, password, "Boss", phone, status);
    }

    @Override
    public String getAccessLevel() {
        return "Full Access";
    }
}
