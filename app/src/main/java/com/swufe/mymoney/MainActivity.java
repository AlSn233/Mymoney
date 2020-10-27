package com.swufe.mymoney;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements Runnable{
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

        AlarmManager aManager=(AlarmManager)getSystemService(Service.ALARM_SERVICE);
        Intent intent=new Intent();
        // 启动一个名为DialogActivity的Activity
        intent.setClass(this, getRate.class);
        // 获取PendingIntent对象
        // requestCode 参数用来区分不同的PendingIntent对象
        // flag 参数常用的有4个值：
        //  FLAG_CANCEL_CURRENT 当需要获取的PendingIntent对象已经存在时，先取消当前的对象，再获取新的；
        // 	FLAG_ONE_SHOT 获取的PendingIntent对象只能使用一次，再次使用需要重新获取
        // 	FLAG_NO_CREATE 如果获取的PendingIntent对象不存在，则返回null
        //	FLAG_UPDATE_CURRENT 如果希望获取的PendingIntent对象与已经存在的PendingIntent对象相比，如果只是Intent附加的数据不同，那么当前存在的PendingIntent对象不会被取消，而是重新加载新的Intent附加的数据

        // 设置定时任务，这里使用绝对时间，即使休眠也提醒，程序启动后过一天会启动新的Activity，在这里配置一年的任务
        int cou = 0;
        for (cou=0; cou<365;cou++){
            PendingIntent pi=PendingIntent.getActivity(this, cou, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            aManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000*cou, pi);
        }

        //开启子线程
        Thread t = new Thread(this);
        t.start();

        Handler handler;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 5) {
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
        second.putExtra("euro_rate_key", euro_rate);
        second.putExtra("won_rate_key", won_rate);

        Log.i(TAG, "open: dollarRate=" + dollar_rate);
        Log.i(TAG, "open: euroRate=" + euro_rate);
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
    @Override
    public void run() {
        Log.i(TAG, "run:run()......");
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
        getwebrate();

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

    public void getwebrate(){
        String url = "http://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run: " + doc.title());
        Elements tables = doc.getElementsByTag("table");
        Element table6 = tables.get(0);
//获取TD中的数据
        Elements tds = table6.getElementsByTag("td");
        for (int i = 0; i < tds.size(); i += 6) {
            Element td1 = tds.get(i);
            Element td2 = tds.get(i + 5);
            String str1 = td1.text();
            String val = td2.text();
            Log.i(TAG, "run: " + str1 + "==>" + val);
            double v = 100f / Double.parseDouble(val);
            //获取数据并返回……

        }
    }

}