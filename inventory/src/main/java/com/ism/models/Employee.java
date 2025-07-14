package com.ism.models;

public class Employee extends User {

    public Employee(long userId, String firstName, String email, String password, String phone, String status) {
        super(userId, firstName, email, password, "Employee", phone, status);
    }

    @Override
    public String getAccessLevel() {
        return "Limited Access";
    }
}
