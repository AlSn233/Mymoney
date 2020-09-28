package com.swufe.mymoney;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class Main2Activity extends AppCompatActivity implements Runnable{
    EditText t_dollar2;
    EditText t_euro2;
    EditText t_won2;
    final DecimalFormat df   = new DecimalFormat("######0.00");
    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        t_dollar2 = findViewById(R.id.text3);
        t_euro2 = findViewById(R.id.text4);
        t_won2 = findViewById(R.id.text5);

        Intent intent = getIntent();
        double dollar2 = intent.getDoubleExtra("dollar_rate_key",0.0f);
        double euro2 = intent.getDoubleExtra("bound_rate_key",0.0f);
        double won2 = intent.getDoubleExtra("won_rate_key",0.0f);

        Log.i(TAG,"onCreate: dollar2=" + dollar2);
        Log.i(TAG,"onCreate: bound2=" + euro2);
        Log.i(TAG,"onCreate: won2=" + won2);
        t_dollar2.setText(Double.toString(dollar2));
        t_euro2.setText(Double.toString(euro2));
        t_won2.setText(Double.toString(won2));

        //开启子线程
        Thread t = new Thread(this);
        t.start();

        Handler handler;
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: getMessage msg = " + str);
                    //show.setText(str);
                }
                super.handleMessage(msg);
            }
        };
//获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
//msg.what = 5;
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);


    }

    private String inputStream2String(InputStream inputStream) throws IOException {
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(inputStream, "gb2312");
            while (true) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            return out.toString();
        }


    //    点击SAVE保存到Bundle并带回数据到调用的页面（2020/9/28新增功能：将信息存储到SharePreferences）
    public void save(View view){
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        t_dollar2 = findViewById(R.id.text3);
        t_euro2 = findViewById(R.id.text4);
        t_won2 = findViewById(R.id.text5);
        double dollar_rate2 = Double.parseDouble(t_dollar2.getText().toString());
        double euro_rate2 = Double.parseDouble(t_euro2.getText().toString());
        double won_rate2 = Double.parseDouble(t_won2.getText().toString());
        bdl.putDouble("key_dollar",dollar_rate2);
        bdl.putDouble("key_euro",euro_rate2);
        bdl.putDouble("key_won",won_rate2);
        intent.putExtras(bdl);
        setResult(2,intent);//设置resultCode及带回的数据
        Log.i(TAG, "transportData: key_dollar="+dollar_rate2);
        Log.i(TAG, "transportData: key_euro="+euro_rate2);
        Log.i(TAG, "transportData: key_won="+won_rate2);

//        获取SharePreferences对象，myrate.XML用来保存汇率
//        myrate.XML在device file explorer的data/data/com.swufe.appinclass/shared_prefs中
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);//MODE_PRIVATE值仅本程序可访问
//        获取Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("key_dollar2", (float) dollar_rate2);
        editor.putFloat("key_euro2",(float) euro_rate2);
        editor.putFloat("key_won2",(float) won_rate2);
        //类似于数据库中提交
        editor.commit ();
        Toast.makeText(this,"修改后汇率已成功存入myrate.XML",Toast.LENGTH_SHORT).show();

        finish();//返回到调用页面
    }
    @Override
    public void run() {
        Log.i(TAG,"run:run()......");
        //获取网络数据
        URL url = null;
        try {
            url = new URL("https://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
            Log.i(TAG, "run: html=" + html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}