package com.swufe.mymoney;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    private static final String TAG = "Main3Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ListView listView = (ListView) findViewById(R.id.mylist);
        String data[] = {"one", "tow", "three", "four"};
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 7) {
                    List<String> list2 = (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(
                            Main3Activity.this,
                            android.R.layout.simple_list_item_1,
                            list2);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }


        };


    }

    private void setListAdapter(ListAdapter adapter) {
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
            /* 获取数据并返回…… */
        }
    }
}