package com.example.yee.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 用于创建和更新数据库的类
 */
public class DbHelper extends SQLiteOpenHelper {

    //构造方法，一般用于创建数据库
    public DbHelper(Context context){
        super(context,"bill.db",null,1); //版本为1
    }

    //数据库第一次创建时调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //初始化表结构
        String t_user = "create table t_user(id integer primary key autoincrement,"+
                "username varchar(100) not null,"+
                "password varchar(100) not null,"+
                "email varchar(100) not null,"+
                "registerTime varchar(100) not null)";
        db.execSQL(t_user);  //创建数据库表t_user

        String t_bill = "create table t_bill(id integer primary key autoincrement,"+
                "username varchar(100) not null,"+
                "type varchar(100) not null,"+
                "sort varchar(100) not null,"+
                "date varchar(100) not null," +
                "money double not null,"+
                "remark varchar(200))";
        db.execSQL(t_bill);  //创建数据库表t_bill
    }

    //当数据库更新时调用，例如数据库版本升级，增加表，修改表字段等
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
