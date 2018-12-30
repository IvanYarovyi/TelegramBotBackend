package com.eone.bot.model;

public class FopNorm {
    private String last_name;
    private String first_name;
    private String other_name;
    private String address;
    private String activity;
    private String status;

    public FopNorm() {
    }

    public FopNorm(String last_name, String first_name, String other_name, String address, String activity, String status) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.other_name = other_name;
        this.address = address;
        this.activity = activity;
        this.status = status;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public String getAddress() {
        return address;
    }

    public String getActivity() {
        return activity;
    }

    public String getStatus() {
        return status;
    }
}
