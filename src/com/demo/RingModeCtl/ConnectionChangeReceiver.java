package com.demo.RingModeCtl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by zhanggx on 13-6-17.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    private final static Base base = new Base();
    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);


        if (settings.getBoolean("df_set", false) || settings.getBoolean("df_user_stop", false)) {
            return;
        }

        int lastRingMode = audioManager.getRingerMode();
        int ringMode = lastRingMode;
        boolean data_change = false;
        SharedPreferences.Editor editor = settings.edit();
        String ssid = base.getSSID(context);
        if (NetworkInfo.State.CONNECTED == state) {
            /* connected */
            Log.i("通知", ssid+":wifi已连接");
            if (settings.contains(ssid)) {
                editor.putString("df_connect_ssid", ssid);
                data_change = true;
                ringMode = settings.getInt(ssid, AudioManager.RINGER_MODE_VIBRATE);
            }
        }
        else if (NetworkInfo.State.DISCONNECTED == state) {
            /* disconnected */
            Log.i("通知", settings.getString("df_connect_ssid", "unknown")+":wifi已断开");
            if (settings.contains("df_connect_ssid")) {
                editor.remove("df_connect_ssid");
                data_change = true;
                ringMode = settings.getInt("df_default_ring_mode", AudioManager.RINGER_MODE_VIBRATE);
            }
        }
        Log.i("通知", Integer.toString(ringMode)+Integer.toString(lastRingMode));
        if (ringMode != lastRingMode) {
            editor.putBoolean("df_change_ring", true);
            data_change = true;
            audioManager.setRingerMode(ringMode);
        }
        if (data_change)
            editor.commit();
    }
}
