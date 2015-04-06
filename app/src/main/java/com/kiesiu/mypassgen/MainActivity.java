/*
 * Copyright 2015 Łukasz Kieś <kiesiu@kiesiu.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kiesiu.mypassgen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;


public class MainActivity extends Activity {
    private TextView tvPassword;
    private TextView tvSize;
    private String strPassword = "";
    private int intPassword = 12;
    private final TextWatcher ePhraseListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() != 0) {
                try {
                    strPassword = MyPassGen.makePassword(editable.toString());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            } else {
                strPassword = "";
            }
            updatePasswordText();
        }
    };
    private final View.OnClickListener eRandomListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                strPassword = MyPassGen.randomPassword();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            updatePasswordText();
        }
    };
    private final SeekBar.OnSeekBarChangeListener eSizeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            intPassword = i + 6;
            tvSize.setText(String.valueOf(intPassword));
            updatePasswordText();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        tvSize = (TextView) findViewById(R.id.tvSize);
        SeekBar sbSize = (SeekBar) findViewById(R.id.sbSize);
        sbSize.setOnSeekBarChangeListener(eSizeListener);
        sbSize.setProgress(intPassword - 6);
        ((EditText) findViewById(R.id.etPhrase)).addTextChangedListener(ePhraseListener);
        findViewById(R.id.btnRandom).setOnClickListener(eRandomListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_rate:
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.kiesiu.mypassgen")));
                break;
            case R.id.action_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePasswordText() {
        if (intPassword < strPassword.length()) {
            tvPassword.setText(strPassword.substring(0, intPassword));
        } else if (strPassword.equals("")) {
            tvPassword.setText("");
        }
    }
}
