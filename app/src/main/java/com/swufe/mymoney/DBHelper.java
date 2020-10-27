package com.swufe.mymoney;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "myrate.db";
    public static final String TB_NAME = "tb_rates";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }
public DBHelper(Context context){
    super(context,DB_NAME,null,VERSION);
}


    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public static class RateItem {
        public void setId(int id) {
        }

        public void setCurName(String curname) {
        }

        public void setCurRate(String currate) {
        }


        public String getCurName() {
            return null;
        }

        public String getCurRate() {
            return null;
        }
    }
}
