package com.swufe.mymoney;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class showRate extends ListActivity {
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_show_rate2);
        List<String> list1 = new ArrayList<String>();
        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("List","lastRateDateStr=" + logDate);
        PreferenceManager.getDefaultSharedPreferences(this);
        Map<String,String> map = new HashMap<String,String>();
        map = (Map<String, String>) sp.getAll();
        for(Map.Entry<String, String> entry : map.entrySet()){
            String mapKey = entry.getKey();
            String mapValue = String.valueOf(entry.getValue());
            list1.add(mapKey+"==>"+mapValue);
        }
        ListAdapter adapter = new ArrayAdapter<String>(showRate.this,android.R.layout.simple_list_item_1,list1);
        setListAdapter(adapter);


    }
}
