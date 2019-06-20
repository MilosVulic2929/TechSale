package com.metropolitan.techsale.order;

import com.metropolitan.techsale.items.model.Item;

import java.util.List;

public class Order {

    private Integer id;

    private List<Item> items;

    private String phone;

    private String address;

    private String firstName;

    private String lastName;

    public Order() { }

    public Order(Integer id, List<Item> items, String phone, String address, String firstName, String lastName) {
        this.id = id;
        this.items = items;
        this.phone = phone;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Order(List<Item> items, String phone, String address, String firstName, String lastName) {
        this.items = items;
        this.phone = phone;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
