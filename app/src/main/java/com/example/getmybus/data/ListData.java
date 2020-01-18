package com.example.getmybus.data;

import java.util.List;

public class ListData {
    public static void setMylist(List<String> mylist) {
        ListData.mylist = mylist;
    }

    public static List<String> getMylist() {
        return mylist;
    }

    public static List<String> mylist;
}
