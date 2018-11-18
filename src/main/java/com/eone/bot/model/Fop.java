package com.eone.bot.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Fop {
    private String full_name;
    private String address;
    private String activity;
    private String status;

    public Fop(String full_name, String address, String activity, String status) {
        this.full_name = full_name;
        this.address = address;
        this.activity = activity;
        this.status = status;
    }

    public String getFull_name() {
        return full_name;
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
