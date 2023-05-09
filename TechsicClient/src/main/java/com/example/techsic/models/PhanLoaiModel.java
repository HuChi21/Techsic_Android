package com.example.techsic.models;

import java.util.List;

public class PhanLoaiModel {
    boolean success;
    String message;
    List<PhanLoai> result;

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

    public List<PhanLoai> getResult() {
        return result;
    }

    public void setResult(List<PhanLoai> result) {
        this.result = result;
    }

}
