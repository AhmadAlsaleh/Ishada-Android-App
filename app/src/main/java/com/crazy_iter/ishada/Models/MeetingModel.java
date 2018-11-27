package com.crazy_iter.ishada.Models;

import java.util.ArrayList;

public class MeetingModel {

    private String id, title, startDate, endDate;
    private int num;
    private ArrayList<String> users;
    private ArrayList<ItemModel> items;

    public MeetingModel(String id, String title, String startDate, String endDate, int num, ArrayList<String> users, ArrayList<ItemModel> items) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.num = num;
        this.users = users;
        this.items = items;
    }

    public void addItem(ItemModel item) {
        this.items.add(item);
    }

    public void addUser(String user) {
        this.users.add(user);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public ArrayList<ItemModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemModel> items) {
        this.items = items;
    }
}

