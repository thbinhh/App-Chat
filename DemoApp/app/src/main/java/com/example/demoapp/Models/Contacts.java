package com.example.demoapp.Models;

public class Contacts {
    String userName, status, profilePic , Statusof;
    public Contacts() {
    }

    public Contacts(String userName, String status, String profilePic, String Statusof) {
        this.userName = userName;
        this.status = status;
        this.profilePic = profilePic;
        this.Statusof = Statusof;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
    public String getStatusof() {
        return Statusof;
    }

    public void setStatusof(String statusof) {
        this.Statusof = statusof;
    }
}
