package com.demo.RingModeCtl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zhanggx on 13-7-11.
 */
public class RingerChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.RINGER_MODE_CHANGED")) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            Log.i("通知", "情景模式改变"+Boolean.toString(settings.getBoolean("df_change_ring", false)));
            if (settings.getBoolean("df_set", false)) {
                return;
            }
            SharedPreferences.Editor editor = settings.edit();
            if (settings.getBoolean("df_change_ring", false)) {
                Toast.makeText(context, "程序情景模式已切换", Toast.LENGTH_LONG).show();
                editor.putBoolean("df_change_ring", false);
            }
            else {
                Toast.makeText(context, "用户情景模式已切换", Toast.LENGTH_LONG).show();
                editor.putBoolean("df_user_stop", !settings.getBoolean("df_user_stop", false));
            }
            editor.commit();

        }
    }
}
