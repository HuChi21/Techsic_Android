package com.example.techsicmanager.models;

import java.io.Serializable;
import java.util.List;

public class DonHang implements Serializable {
    int id;
    int idtaikhoan;
    String hoten;
    String sodt;
    String email;
    String diachi;
    String ghichu;
    String ngaydat;
    String ngaynhan;
    int soluong;
    String tongthanhtoan;
    String kieutinhtrang;
    String hinhthuc;
    List<Item> item;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdtaikhoan() {
        return idtaikhoan;
    }

    public void setIdtaikhoan(int idtaikhoan) {
        this.idtaikhoan = idtaikhoan;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getSodt() {
        return sodt;
    }

    public void setSodt(String sodt) {
        this.sodt = sodt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getNgaydat() {
        return ngaydat;
    }

    public void setNgaydat(String ngaydat) {
        this.ngaydat = ngaydat;
    }

    public String getNgaynhan() {
        return ngaynhan;
    }

    public void setNgaynhan(String ngaynhan) {
        this.ngaynhan = ngaynhan;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getTongthanhtoan() {
        return tongthanhtoan;
    }

    public void setTongthanhtoan(String tongthanhtoan) {
        this.tongthanhtoan = tongthanhtoan;
    }

    public String getKieutinhtrang() {
        return kieutinhtrang;
    }

    public void setKieutinhtrang(String kieutinhtrang) {
        this.kieutinhtrang = kieutinhtrang;
    }

    public String getHinhthuc() {
        return hinhthuc;
    }

    public void setHinhthuc(String hinhthuc) {
        this.hinhthuc = hinhthuc;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }
}
