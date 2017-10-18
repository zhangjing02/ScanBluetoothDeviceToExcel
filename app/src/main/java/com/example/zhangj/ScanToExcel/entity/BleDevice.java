package com.example.zhangj.mybluetooth_demo11.entity;

import java.io.Serializable;

public class BleDevice implements Serializable {
	private int id=-1;
	private String deviceName;//蓝牙地址
	private String deviceAddress;//蓝牙地址
	private int deviceType; //1：广播人体秤 2：脂肪秤 3：心率秤 4：八电极人体分析仪 5:BLE人体秤 6:闹钟BLE人体秤
	private int rssi;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	@Override
	public String toString() {
		return deviceAddress;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (obj instanceof BleDevice) {
        	BleDevice u = (BleDevice) obj;
            return deviceAddress.equals(u.getDeviceAddress());
        }   
        return super.equals(obj); 
	}
}
