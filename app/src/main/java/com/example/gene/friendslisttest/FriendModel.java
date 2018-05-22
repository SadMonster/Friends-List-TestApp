package com.example.gene.friendslisttest;

import java.util.Comparator;

public class FriendModel {
    public String name;
    public String email;
    public String birthday;
    public String avatarUrl;
    public String thumbURL;

    public FriendModel(String name, String email, String birthday,String thumb, String avatar) {
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.thumbURL = thumb;
        this.avatarUrl = avatar;
    }
}