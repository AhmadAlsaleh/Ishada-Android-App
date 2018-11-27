package com.crazy_iter.ishada.Models;

import java.util.ArrayList;

public class ItemModel {

    private int num;
    private String subject, extra;
    private ArrayList<String> users;

    public ItemModel(int num, String subject, String extra, ArrayList<String> users) {
        this.num = num;
        this.subject = subject;
        this.extra = extra;
        this.users = users;
    }

    public ItemModel(int num) {
        this.num = num;
        this.subject = "";
        this.extra = "";
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}
