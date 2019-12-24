package com.example.getmybus.Recyclerview_bus_details;

public class Busdata {
    String src_dest;
    String bus_num;
    String exp_time;
    public Busdata(String src_dest, String bus_num, String exp_time, String location) {
        this.src_dest = src_dest;
        this.bus_num = bus_num;
        this.exp_time = exp_time;
        this.location = location;
    }
    public String getSrc_dest() {
        return src_dest;
    }

    public String getBus_num() {
        return bus_num;
    }

    public String getExp_time() {
        return exp_time;
    }

    public String getLocation() {
        return location;
    }

    String location;
}
