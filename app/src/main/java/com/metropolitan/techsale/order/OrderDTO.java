package com.metropolitan.techsale.order;

import java.util.List;

public class OrderDTO {

    private List<Integer> items;

    private String username;

    private String phone;

    private String address;

    private String firstName;

    private String lastName;

    public OrderDTO() { }


    public OrderDTO(List<Integer> items, String username, String phone, String address, String firstName, String lastName) {
        this.items = items;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
