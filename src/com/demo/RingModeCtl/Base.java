package com.demo.RingModeCtl;

import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by zhanggx on 13-7-12.
 */
public class Base {
    private static final String[] ringArray = {"响铃", "静音", "震动"};
    public String[] getRingArray() {
        return ringArray;
    }
    public String getRingName(int ringMode) {
        switch (ringMode) {
            case AudioManager.RINGER_MODE_SILENT:
                return "静音";
            case AudioManager.RINGER_MODE_VIBRATE:
                return "震动";
            case AudioManager.RINGER_MODE_NORMAL:
                return "响铃";
        }
        return "未知";
    }
    public int getRingMode(int pos) {
        int ringMode = AudioManager.RINGER_MODE_NORMAL;

        switch (pos) {
            case 0:
                ringMode = AudioManager.RINGER_MODE_NORMAL;
                break;
            case 1:
                ringMode = AudioManager.RINGER_MODE_SILENT;
                break;
            case 2:
                ringMode = AudioManager.RINGER_MODE_VIBRATE;
                break;
        }
        return ringMode;
    }
    public int getRingMode(Context context) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode();
    }
    public void setRingMode(Context context, int ringMode) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(ringMode);
    }
    public int getRingIndex(int ringMode) {
        switch (ringMode) {
            case AudioManager.RINGER_MODE_SILENT:
                return 1;
            case AudioManager.RINGER_MODE_VIBRATE:
                return 2;
            case AudioManager.RINGER_MODE_NORMAL:
                return 0;
        }
        return 0;
    }
    public String getRingModeState(Boolean set, Boolean user_stop) {
        if (set)
            return "勿扰模式";
        if (user_stop)
            return "手动模式";
        else
            return "自动模式";
    }
    public String getSSID(Context context) {
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        ssid = ssid.replace("\"", "");
        return ssid;
    }
}
