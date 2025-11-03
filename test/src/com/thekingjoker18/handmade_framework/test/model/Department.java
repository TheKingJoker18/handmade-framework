package com.thekingjoker18.handmade_framework.test.model;

import com.thekingjoker18.handmade_framework.annotation.ModelField;

public class Department {
    @ModelField(name = "name") String name;
    @ModelField(name = "money") double money;

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public double getMoney() { return this.money; }
    public void setMoney(double money) { this.money = money; }

    public Department() {}
    public Department(String name, double money) {
        this.setName(name);
        this.setMoney(money);
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name +
                "', money=" + money +
                '}';
    }
}
