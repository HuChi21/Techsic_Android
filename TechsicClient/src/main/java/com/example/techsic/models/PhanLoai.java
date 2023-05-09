package com.example.techsic.models;

import java.io.Serializable;

public class PhanLoai implements Serializable {
    int idsp;
    int idphanloai;
    String phanloai;

    public int getIdsp() {
        return idsp;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public int getIdphanloai() {
        return idphanloai;
    }

    public void setIdphanloai(int idphanloai) {
        this.idphanloai = idphanloai;
    }

    public String getPhanloai() {
        return phanloai;
    }

    public void setPhanloai(String phanloai) {
        this.phanloai = phanloai;
    }
}
