package com.example.techsicmanager.models.eventbus;

import com.example.techsicmanager.models.SanPham;

public class SuaXoaspEvent {
    SanPham sanPham;

    public SuaXoaspEvent(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }
}
