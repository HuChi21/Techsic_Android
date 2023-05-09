package com.example.techsic.models;

import java.io.Serializable;

public class TinTuc implements Serializable {
    int idtintuc;
    String kieutintuc;
    String tieude;
    String hinhanh;
    String noidung;
    String tacgia;
    String thoigian;
    int idsp;
    int khuyenmai;

    public int getIdtintuc() {
        return idtintuc;
    }

    public void setIdtintuc(int idtintuc) {
        this.idtintuc = idtintuc;
    }

    public String getKieutintuc() {
        return kieutintuc;
    }

    public void setKieutintuc(String kieutintuc) {
        this.kieutintuc = kieutintuc;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getTacgia() {
        return tacgia;
    }

    public void setTacgia(String tacgia) {
        this.tacgia = tacgia;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public int getIdsp() {
        return idsp;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public int getKhuyenmai() {
        return khuyenmai;
    }

    public void setKhuyenmai(int khuyenmai) {
        this.khuyenmai = khuyenmai;
    }
}
