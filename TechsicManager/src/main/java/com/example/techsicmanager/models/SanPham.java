package com.example.techsicmanager.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class SanPham implements Serializable {
    int idsp;
    String tensp;
    String thuonghieu;
    BigDecimal giasp;
    String hinhanh;
    String thongso;
    String mota;
    int idloaisp;
    int sltonkho;
    List<PhanLoai> phanLoais;

    public int getIdsp() {
        return idsp;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getThuonghieu() {
        return thuonghieu;
    }

    public void setThuonghieu(String thuonghieu) {
        this.thuonghieu = thuonghieu;
    }

    public BigDecimal getGiasp() {
        return giasp;
    }

    public void setGiasp(BigDecimal giasp) {
        this.giasp = giasp;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }


    public String getThongso() {
        return thongso;
    }

    public void setThongso(String thongso) {
        this.thongso = thongso;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getIdloaisp() {
        return idloaisp;
    }

    public void setIdloaisp(int idloaisp) {
        this.idloaisp = idloaisp;
    }

    public int getSltonkho() {
        return sltonkho;
    }

    public void setSltonkho(int sltonkho) {
        this.sltonkho = sltonkho;
    }

    public List<PhanLoai> getPhanLoais() {
        return phanLoais;
    }

    public void setPhanLoais(List<PhanLoai> phanLoais) {
        this.phanLoais = phanLoais;
    }
}
