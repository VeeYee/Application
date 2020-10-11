package com.example.yee.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.yee.entity.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 用户信息数据库操作类
 */
public class UserDao {

    //定义DbHelper帮助类
    private DbHelper helper;

    public UserDao(Context context) {
        //初始化DbHelper帮助类
        helper = new DbHelper(context);
    }

    /**
     * 增加用户信息
     */
    public long addUser(User user){
        long id = 0;
        //创建数据库操作对象
        SQLiteDatabase db = null;
        try{
            db = helper.getWritableDatabase(); //可写操作
            //先判断用户名是否存在
            Cursor cursor = db.query("t_user",null,"username = ?",
                    new String[]{user.getUsername()},null,null,null);
            boolean exist = cursor.moveToNext();
            if(exist) return -1;
            //再判断邮箱是否存在
            Cursor cursor1 = db.query("t_user",null,"email = ?",
                    new String[]{user.getEmail()},null,null,null);
            boolean exist1 = cursor1.moveToNext();
            if(exist1) return -2;
            //用户名和邮箱都不存在时插入
            ContentValues values = new ContentValues();
            values.put("username", user.getUsername());
            values.put("password", user.getPassword());
            values.put("email",user.getEmail());
            values.put("registerTime",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            //增加一条个人信息记录
            id = db.insert("t_user", null, values);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();//关闭数据库
            }
        }
        return id;
    }

    /**
     * 根据用户名和密码登录
     * @param username
     * @param password
     * @return
     */
    public User loginByName(String username,String password){
        User user = null;
        //创建数据库操作对象
        SQLiteDatabase db = null;
        try{
            db = helper.getReadableDatabase();
            Cursor cursor = db.query("t_user",null,"username = ? and password = ?",
                    new String[]{username,password},null,null,null);
            //取出查询结果
            while (cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String uname = cursor.getString(cursor.getColumnIndex("username"));
                String pwd = cursor.getString(cursor.getColumnIndex("password"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String registerTime = cursor.getString(cursor.getColumnIndex("registerTime"));
                //封装到个人对象中
                user = new User(id,uname,pwd,email,registerTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
        return user;
    }

    /**
     * 根据电子邮箱和密码登录
     * @param email
     * @param password
     * @return
     */
    public User loginByEmail(String email,String password){
        User user = null;
        //创建数据库操作对象
        SQLiteDatabase db = null;
        try{
            db = helper.getReadableDatabase();
            Cursor cursor = db.query("t_user",null,"email = ? and password = ?",
                    new String[]{email,password},null,null,null);
            //取出查询结果
            while (cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String pwd = cursor.getString(cursor.getColumnIndex("password"));
                String uemail = cursor.getString(cursor.getColumnIndex("email"));
                String registerTime = cursor.getString(cursor.getColumnIndex("registerTime"));
                //封装到个人对象中
                user = new User(id,username,pwd,uemail,registerTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
        return user;
    }

}
