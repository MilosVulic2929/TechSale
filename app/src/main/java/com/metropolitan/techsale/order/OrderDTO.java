package com.metropolitan.techsale.order;

import java.util.List;

public class OrderDTO {

    private List<Integer> items;

    private String phone;

    private String address;

    private String firstName;

    private String lastName;

    public OrderDTO() { }


    public OrderDTO(List<Integer> items, String phone, String address, String firstName, String lastName) {
        this.items = items;
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
