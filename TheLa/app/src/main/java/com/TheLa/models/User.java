package com.TheLa.models;

import java.io.Serializable;

public class User implements Serializable {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String code;
    private String address;
    private String phone;
    private String role;
    private Boolean isActive;

    public User(){

    }
    public User(Long userId, String name, String email, String password, String code,
                String address, String phone, String role, Boolean isActive) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.code = code;
        this.address = address;
        this.phone = phone;
        this.role = role;
        this.isActive = isActive;
    }

    public User(String name, String email, String password, String code, String address, String phone, String role, Boolean isActive) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.code = code;
        this.address = address;
        this.phone = phone;
        this.role = role;
        this.isActive = isActive;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
