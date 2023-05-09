package com.example.techsic;

public class Cache {

    private static Cache instance;
    private int data;

    private Cache() {
        // Khởi tạo lớp Singleton
    }

    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
