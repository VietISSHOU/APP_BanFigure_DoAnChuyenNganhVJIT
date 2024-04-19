package com.appquanly.japanfigure.Model.EventBus;

import com.appquanly.japanfigure.Model.SanPhamMoi;

public class SuaXoaEvent {
    SanPhamMoi SanPhamMoi;

    public SuaXoaEvent(SanPhamMoi sanPham) {
        this.SanPhamMoi = sanPham;
    }

    public com.appquanly.japanfigure.Model.SanPhamMoi getSanPhamMoi() {
        return SanPhamMoi;
    }

    public void setSanPhamMoi(com.appquanly.japanfigure.Model.SanPhamMoi sanPhamMoi) {
        SanPhamMoi = sanPhamMoi;
    }


}
