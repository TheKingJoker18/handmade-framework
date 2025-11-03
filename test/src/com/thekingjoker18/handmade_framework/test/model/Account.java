package com.thekingjoker18.handmade_framework.test.model;

import com.thekingjoker18.handmade_framework.annotation.Email;
import com.thekingjoker18.handmade_framework.annotation.Length;
import com.thekingjoker18.handmade_framework.annotation.ModelField;
import com.thekingjoker18.handmade_framework.annotation.NotNull;
import com.thekingjoker18.handmade_framework.annotation.Range;

public class Account {
    @ModelField(name = "id")
    @Range(min = "1", max = "10")
    int id;

    @ModelField(name = "email")
    @Email
    String email;

    @ModelField(name = "password")
    @NotNull
    @Length(value = 8)
    String password;

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password =  password; }

    public Account() {}
    public Account(int id, String email, String password) {
        this.setId(id);
        this.setEmail(email);
        this.setPassword(password);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" +  password + '\'' +
                '}';
    }
}
