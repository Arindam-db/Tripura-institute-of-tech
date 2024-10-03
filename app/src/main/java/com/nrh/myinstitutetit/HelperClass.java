package com.nrh.myinstitutetit;

//for storing sensitive data like name passwords email branch

public class HelperClass {
    String name, email, password, branch;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public HelperClass(String name, String email, String password, String branch) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.branch = branch;
    }

    public HelperClass() {
    }
}
