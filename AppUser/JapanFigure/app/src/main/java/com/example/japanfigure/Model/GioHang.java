package com.example.japanfigure.Model;

public class GioHang {
    int ipsp;
    String tensp;
    long giasp;
    String hinhsp;
    int soluong;
    boolean isCheked;
    int sltonkho;

    public int getSltonkho() {
        return sltonkho;
    }

    public void setSltonkho(int sltonkho) {
        this.sltonkho = sltonkho;
    }

    public GioHang(){

    }
    public boolean isCheked() {
        return isCheked;
    }

    public void setCheked(boolean cheked) {
        isCheked = cheked;
    }

    public int getIpsp() {
        return ipsp;
    }

    public void setIdsp(int ipsp) {
        this.ipsp = ipsp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public long getGiasp() {
        return giasp;
    }

    public void setGiasp(long giasp) {
        this.giasp = giasp;
    }

    public String getHinhsp() {
        return hinhsp;
    }

    public void setHinhsp(String hinhsp) {
        this.hinhsp = hinhsp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
