package com.numismatic_app.server.dto;

public class UserDto {

    private String username;

    private String password;

    public UserDto() {
        // Do nothing
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
