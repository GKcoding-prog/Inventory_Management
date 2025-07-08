package com.ism.models;

public abstract class User {
    private long userId;
    private String firstName;
    private String email;
    private String password;
    private String role;
    private String phone; // New field
    private String status; // New field

    public User(long userId, String firstName, String email, String password, String role, String phone, String status) {
        this.userId = userId;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Abstract method to define access level
    public abstract String getAccessLevel();
}
