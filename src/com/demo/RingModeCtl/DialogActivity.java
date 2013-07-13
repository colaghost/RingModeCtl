package com.demo.RingModeCtl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by zhanggx on 13-6-10.
 */
public class DialogActivity extends Activity implements OnClickListener{
    private Button addButton;
    private Button cancleButton;
    private EditText editText;
    private Spinner spinner;

    private ArrayAdapter<String> adapter;
    private static final Base base = new Base();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        addButton = (Button)findViewById(R.id.add_button);
        cancleButton = (Button)findViewById(R.id.cancle_button);
        editText = (EditText)findViewById(R.id.editText);
        spinner = (Spinner)findViewById(R.id.spinner);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, base.getRingArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        addButton.setOnClickListener(this);
        cancleButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button: {
                if (editText.getText().toString() == "") {
                    new AlertDialog.Builder(this).setTitle("警告").setMessage("请输入wifi接入点名称").setPositiveButton("确定", null).show();
                    return;
                }

                int ringMode = base.getRingMode(spinner.getSelectedItemPosition());

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = settings.edit();
                String addSSID = editText.getText().toString();
                editor.putInt(addSSID, ringMode);
                if (!settings.getBoolean("df_set", false) && !settings.getBoolean("df_user_stop", false)) {
                    if (base.getSSID(this).equals(addSSID)) {
                        editor.putString("df_connect_ssid", addSSID);
                        if (base.getRingMode(this) != ringMode) {
                            editor.putBoolean("df_change_ring", true);
                            base.setRingMode(this, ringMode);
                        }
                    }
                }
                editor.commit();
                this.setResult(RESULT_OK);
            }
                break;
            case R.id.cancle_button:
                this.setResult(RESULT_CANCELED);
                break;
        }
        this.finish();
    }
}
