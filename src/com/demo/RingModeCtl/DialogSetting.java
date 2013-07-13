package com.demo.RingModeCtl;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * Created by zhanggx on 13-7-12.
 */
public class DialogSetting extends Activity implements OnClickListener{
    private Spinner mSpinner;
    private Button mResetBtn;
    private TextView mState;
    private ArrayAdapter<String> adapter;
    private static final Base base = new Base();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSpinner = (Spinner)findViewById(R.id.setting_spinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, base.getRingArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mResetBtn = (Button)findViewById(R.id.button_reset);
        mResetBtn.setOnClickListener(this);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        mSpinner.setSelection(base.getRingIndex(settings.getInt("df_default_ring_mode", AudioManager.RINGER_MODE_VIBRATE)));
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                int ringMode = base.getRingMode(mSpinner.getSelectedItemPosition());
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("df_default_ring_mode", ringMode);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mState = (TextView)findViewById(R.id.state_text);
        mState.setText(base.getRingModeState(settings.getBoolean("df_set", false), settings.getBoolean("df_user_stop", false)));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_reset: {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = settings.edit();
                mState.setText("自动模式");
                editor.putBoolean("df_user_stop", false);
                editor.commit();
                break;
            }
            case R.id.setting_spinner: {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                int ringMode = base.getRingMode(mSpinner.getSelectedItemPosition());
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("df_default_ring_mode", ringMode);
                editor.commit();
                break;
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }
}
