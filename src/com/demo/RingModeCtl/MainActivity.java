package com.demo.RingModeCtl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends Activity {
	private AudioManager mAudioManager;
    private WifiManager mWifi;
    private ListView mListView;
    private SimpleAdapter mSimpleAdapter;
    private ArrayList<HashMap<String, String>> mArrayList;

    private  static final Base base = new Base();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mButtonSet.setText(isSet ? "恢复" : "勿扰");

        //mNetworkStateReceiver = new ConnectionChangeReceiver();
        
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mWifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);



        /*
        WifiInfo wifiInfo = mWifi.getConnectionInfo();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        if (wifiInfo != null)
        {
            new AlertDialog.Builder(this).setTitle("标题").setMessage(wifiInfo.getSSID() + settings.getInt("colaghost", 3)).setPositiveButton("确定", null).show();
        }
        else
        {
            new AlertDialog.Builder(this).setTitle("标题").setMessage("木有wifi信息").setPositiveButton("确定", null).show();
        }
        */

        /*
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, filter);
        */

        mListView = (ListView)findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap<String, String>)mListView.getItemAtPosition(i);
                String title = map.get("title");
                mArrayList.remove(i);
                mSimpleAdapter.notifyDataSetChanged();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove(title);
                if (settings.getString("df_connect_ssid", "").equals(title))
                    editor.remove("df_connect_ssid");
                editor.commit();
            }
        });

        initListViewData();
    }


    private void initListViewData() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> keys = settings.getAll();
        mArrayList = new ArrayList<HashMap<String, String>>();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            if (entry.getKey().startsWith("df_") == false) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("title", entry.getKey());
                map.put("content", base.getRingName(Integer.parseInt(entry.getValue().toString())));
                mArrayList.add(map);
            }
        }
        mSimpleAdapter = new SimpleAdapter(this,
                mArrayList,
                R.layout.list_item,
                new String[] {"title", "content"},
                new int[] {R.id.itemTitle, R.id.itemContent}
                );
        mListView.setAdapter(mSimpleAdapter);
    }

    private void refreshListViewData() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> keys = settings.getAll();
        mArrayList.clear();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            if (entry.getKey().startsWith("df_") == false) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("title", entry.getKey());
                map.put("content", base.getRingName(Integer.parseInt(entry.getValue().toString())));
                mArrayList.add(map);
            }

        }
        mSimpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mNetworkStateReceiver);
    }

    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.activity_main, menu);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean isSet = settings.getBoolean("df_set", false);
        MenuItem menuSet = menu.getItem(0);
        menuSet.setTitle(isSet ? "恢复" : "勿扰");
        menuSet.setIcon(isSet ? R.drawable.play : R.drawable.pause);
        /*
        menu.add(
                Menu.NONE,
                Menu.FIRST+1,
                0,
                "设置"
        );
        menu.add(
                Menu.NONE,
                Menu.FIRST+2,
                1,
                "帮助"
        );*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings: {
                Intent i = new Intent(this, DialogSetting.class);
                startActivityForResult(i, 0);
                break;
            }
            case R.id.menu_silent: {
                setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            }
            case R.id.menu_normal: {
                setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            }
            case R.id.menu_vibrate: {
                setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
            }
            case R.id.menu_add: {
                Intent i = new Intent(this, DialogActivity.class);
                startActivityForResult(i, 0);
                break;
            }
            case R.id.menu_set: {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = settings.edit();
                boolean isSet = settings.getBoolean("df_set", true);
                item.setTitle(!isSet ? "恢复" : "勿扰");
                item.setIcon(!isSet ? R.drawable.play : R.drawable.pause);
                Toast.makeText(this, !isSet ? "进入勿扰模式" : "禁止勿扰模式", Toast.LENGTH_LONG).show();
                editor.putBoolean("df_set", !isSet);
                editor.commit();
                break;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    new AlertDialog.Builder(this).setTitle("通知").setMessage("添加成功").setPositiveButton("确定", null).show();
                    refreshListViewData();
                }
                break;
        }
    }
	
	private void setRingerMode(int ringerMode){
		mAudioManager.setRingerMode(ringerMode);

        /*
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("mode", ringerMode);
        editor.commit();
        */
	}
}
