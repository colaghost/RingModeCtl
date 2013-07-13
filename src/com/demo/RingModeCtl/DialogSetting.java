package com.demo.RingModeCtl;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
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
    private Button mSetBtn;
    private Button mCancleBtn;
    private TextView mState;
    private ArrayAdapter<String> adapter;
    private static final Base base = new Base();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        mSpinner = (Spinner)findViewById(R.id.setting_spinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, base.getRingArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSetBtn = (Button)findViewById(R.id.button_set);
        mSetBtn.setOnClickListener(this);

        mCancleBtn = (Button)findViewById(R.id.button_cancle);
        mCancleBtn.setOnClickListener(this);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        mSpinner.setSelection(base.getRingIndex(settings.getInt("df_default_ring_mode", AudioManager.RINGER_MODE_VIBRATE)));

        mState = (TextView)findViewById(R.id.state_text);
        mState.setText(base.getRingModeState(settings.getBoolean("df_set", false), settings.getBoolean("df_user_stop", false)));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_set: {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                int ringMode = base.getRingMode(mSpinner.getSelectedItemPosition());
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("df_default_ring_mode", ringMode);
                editor.commit();
                break;
            }
            case R.id.button_cancle:
                break;
        }
        this.finish();
    }
}
