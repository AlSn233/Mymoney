package com.swufe.mymoney;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText t1;
    TextView t2;
    double dollar_rate = 0.1472;
    double euro_rate = 0.1256;
    double won_rate = 171.4256;

    final DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = findViewById(R.id.text1);
        t2 = findViewById(R.id.text2);

    }


    public void bt1(View v) {
        if (t1.getText().toString() == null) {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        } else {
            double money = Double.valueOf(t1.getText().toString());
            double dollar = dollar_rate * money;
            t2.setText(df.format(dollar));
        }
    }

    public void bt2(View v) {
        if (t1.getText().toString() == null) {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        } else {
            double money = Double.valueOf(t1.getText().toString());
            double euro = euro_rate * money;
            t2.setText(df.format(euro));
        }
    }

    public void bt3(View v) {
        if (t1.getText().toString() == null) {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        } else {
            double money = Double.valueOf(t1.getText().toString());
            double won = won_rate * money;
            t2.setText(df.format(won));
        }
    }

    public void bt4(View v) {
        t2.setText("");
        t1.setText("");
    }

    public void open(View v) {
        Intent second = new Intent(this, Main2Activity.class);
        second.putExtra("dollar_rate_key", dollar_rate);
        second.putExtra("bound_rate_key", euro_rate);
        second.putExtra("won_rate_key", won_rate);

        Log.i(TAG, "open: dollarRate=" + dollar_rate);
        Log.i(TAG, "open: boundRate=" + euro_rate);
        Log.i(TAG, "open: wonRate=" + won_rate);
        startActivityForResult(second, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            //读取bundle中传回数据
            Bundle bundle = data.getExtras();
            dollar_rate = bundle.getDouble("key_dollar", 0.1f);
            euro_rate = bundle.getDouble("key_euro", 0.1f);
            won_rate = bundle.getDouble("key_won", 0.1f);
            Log.i(TAG, "onActivityResult: dollar_rate=" + dollar_rate);
            Log.i(TAG, "onActivityResult: euro_rate=" + euro_rate);
            Log.i(TAG, "onActivityResult: won_rate=" + won_rate);

            //使用myrate.XML中汇率看是否成功存入、读取
            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            dollar_rate = sharedPreferences.getFloat("key_dollar2", 0.0f);
            euro_rate = sharedPreferences.getFloat("key_euro2", 0.0f);
            won_rate = sharedPreferences.getFloat("key_won2", 0.0f);
            Log.i(TAG, "onActivityResult: dollar_rate2=" + dollar_rate);
            Log.i(TAG, "onActivityResult: euro_rate2=" + euro_rate);
            Log.i(TAG, "onActivityResult: won_rate2=" + won_rate);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}