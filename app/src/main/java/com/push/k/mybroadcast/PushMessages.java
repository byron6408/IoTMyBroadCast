package com.push.k.mybroadcast;

/**
 * Created by K on 2015/11/19.
 */
public class PushMessages {
    private String deviceId;
    private String msg;
    private String time;
    private int icon;
    public PushMessages(String deviceId, String msg, String time,int icon) {
        setDeviceId(deviceId);
        setMsg(msg);
        setTime(time);
        setIcon(icon);
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String name) {
        this.deviceId = name;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String price) {
        this.msg = price;
    }
    public String gettime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
}
