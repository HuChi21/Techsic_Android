package com.example.techsicmanager.models;

import java.util.List;

public class TaiKhoanModel {
    boolean success;
    String message;
    List<TaiKhoan> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TaiKhoan> getResult() {
        return result;
    }

    public void setResult(List<TaiKhoan> result) {
        this.result = result;
    }
}
