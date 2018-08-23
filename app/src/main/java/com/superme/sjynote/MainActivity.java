package com.superme.sjynote;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * 主界面
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        TextInputView textInputView = findViewById(R.id.textInputView);
        TextView text = findViewById(R.id.text);
        textInputView.setText("1");
        text.setText(textInputView.getText());
    }
}
