package com.appquanly.japanfigure.utils;

import com.appquanly.japanfigure.Model.GioHang;
import com.appquanly.japanfigure.Model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    //public static final String BASE_URL = "http://192.168.1.4/figure/";
    //public static final String BASE_URL = "http://192.168.0.189/figure/";
    public static final String BASE_URL = "http://172.20.10.2/figure/";
    //public static final String BASE_URL = "http://192.168.1.46/figure/";
    //public static final String BASE_URL = "http://10.50.3.103/figure/";


    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang = new ArrayList<>();
    public static User user_current = new User();
    public static  String ID_RECEIVER;
    public static final  String SENDID = "idsend";
    public static  final String RECEIVERID = "idreceived";
    public static  final String MESSAGE = "message";
    public static  final String DATETIME = "datetime";
    public static  final String PATH_CHAT = "chat";
}
