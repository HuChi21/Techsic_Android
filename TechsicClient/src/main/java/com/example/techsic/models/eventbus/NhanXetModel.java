package com.example.techsic.models.eventbus;

import com.example.techsic.models.NhanXet;
import com.example.techsic.models.SanPham;

import java.util.List;

public class NhanXetModel {
    boolean success;
    String message;
    List<NhanXet> result;

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

    public List<NhanXet> getResult() {
        return result;
    }

    public void setResult(List<NhanXet> result) {
        this.result = result;
    }
}
