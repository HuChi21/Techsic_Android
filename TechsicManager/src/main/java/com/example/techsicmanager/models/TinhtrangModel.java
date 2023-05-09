package com.example.techsicmanager.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TinhtrangModel {
    boolean success;
    String message;
    List<String> result;

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

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}