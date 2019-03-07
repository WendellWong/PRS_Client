package com.wangz.prs_client;

public class userData {
    private int id;
    private int StdId;
    private String name;
    private String email;
    private int phone;
    private int role;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getStdId() {
        return StdId;
    }
    public void setStdId(int StdId) {
        this.StdId = StdId;
    }
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
    public void setPwd(String email) {
        this.email = email;
    }
    public int getPhone() {
        return phone;
    }
    public void setPhone(int phone) {
        this.phone = phone;
    }
    public int getRole() {
        return role;
    }
    public void setRole(int role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ",StdId=" + StdId + ", name=" + name + ", email=" + email + ",phone="+ phone + ",role=" + role + "]";
    }
}
