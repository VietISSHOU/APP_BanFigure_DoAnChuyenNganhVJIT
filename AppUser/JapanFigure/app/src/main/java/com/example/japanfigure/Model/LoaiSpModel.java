package com.example.japanfigure.Model;

import java.util.List;

public class LoaiSpModel {
    boolean success;
    String massage;
    List<Loai> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public List<Loai> getResult() {
        return result;
    }

    public void setResult(List<Loai> result) {
        this.result = result;
    }
}
