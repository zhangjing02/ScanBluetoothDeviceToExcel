package com.example.zhangj.mybluetooth_demo11;

import android.app.Application;

import com.example.zhangj.mybluetooth_demo11.entity.BleDevice;


/**
 * Created by liangls on 2016/6/icon_back.
 */
public class MyApp extends Application {
    public static BleDevice mDevice;//当前设备
    public static int type;//1:碧德 2：moving


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
