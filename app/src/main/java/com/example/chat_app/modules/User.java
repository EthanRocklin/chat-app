package com.example.chat_app.modules;

import java.io.Serializable;

public class User implements Serializable {

    private String email;
    private String id;
    private String image;
    private String name;
    private String token;

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
