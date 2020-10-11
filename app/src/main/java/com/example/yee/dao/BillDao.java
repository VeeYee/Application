package com.example.yee.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yee.entity.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * 账单信息数据库操作类
 */
public class BillDao {

    //定义DbHelper帮助类
    private DbHelper helper;

    public BillDao(Context context) {
        //初始化DbHelper帮助类
        helper = new DbHelper(context);
    }

    /**
     * 新增一条账单记录
     * @param bill
     * @return
     */
    public long addBill(Bill bill){
        long id = 0;
        //创建数据库操作对象
        SQLiteDatabase db = null;
        try{
            db = helper.getWritableDatabase(); //可写操作
            //map形式封装数据
            ContentValues values = new ContentValues();
            values.put("username", bill.getUsername());
            values.put("type", bill.getType());
            values.put("sort",bill.getSort());
            values.put("date",bill.getDate());
            values.put("money",bill.getMoney());
            values.put("remark",bill.getRemark());
            //增加一条账单信息记录
            id = db.insert("t_bill", null, values);
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
     * 根据账号查询账单
     * @param username
     * @return
     */
    public List queryBill(String username){
        List billList = new ArrayList();
        //创建数据库操作对象
        SQLiteDatabase db = null;
        try{
            db = helper.getReadableDatabase();
            //查询  按时间降序
            Cursor cursor = db.query("t_bill",null,"username = ?",new String[]{username},null,null,"date desc",null);
            while (cursor.moveToNext()){
                //取数据
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String uname = cursor.getString(cursor.getColumnIndex("username"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String sort = cursor.getString(cursor.getColumnIndex("sort"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                double money = cursor.getDouble(cursor.getColumnIndex("money"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                //封装到账单对象中
                Bill bill = new Bill(id,uname,type,sort,date,money,remark);
                billList.add(bill);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();  //关闭数据库
            }
        }
        return billList;
    }

    /**
     * 根据用户查询记录账单的所有日期
     * @param username
     * @return
     */
    public List queryBillDate(String username){
        List dateList = new ArrayList();
        //创建数据库操作对象
        SQLiteDatabase db = null;
        try{
            db = helper.getReadableDatabase();
            //查询  按时间降序
            Cursor cursor = db.query("t_bill",new String[]{"date"},"username = ?",new String[]{username},"date",null,"date desc",null);
            while (cursor.moveToNext()){
                //取数据
                String date = cursor.getString(cursor.getColumnIndex("date"));
                //封装到账单对象中
                dateList.add(date);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();  //关闭数据库
            }
        }
        return dateList;
    }

    /**
     * 删除一条账单信息
     * @param id
     * @return
     */
    public int deleteBill(int id){
        int num = 0;
        SQLiteDatabase db = null;
        try{
            db = helper.getWritableDatabase();
            num = db.delete("t_bill","id = ?",new String[]{id+""});
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();
            }
        }
        return num;
    }

    /**
     * 查询用户账单的所有分类
     * @param username
     * @param type  按收支类型查询
     * @return
     */
    public List querySort(String username,String type){
        List sortList = new ArrayList();
        //创建数据库操作对象
        SQLiteDatabase db = null;
        try{
            db = helper.getReadableDatabase();
            //查询  按时间降序
            Cursor cursor = db.query("t_bill",new String[]{"sort"},"username = ? and type = ?",new String[]{username,type},"sort",null,null,null);
            while (cursor.moveToNext()){
                //取数据
                String sort = cursor.getString(cursor.getColumnIndex("sort"));
                //封装到账单对象中
                sortList.add(sort);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();  //关闭数据库
            }
        }
        return sortList;
    }

    /**
     * 查询用户的所有账单
     * @param username
     * @param type  按收支类型查询
     * @return
     */
    public List queryBill(String username,String type){
        List billList = new ArrayList();
        //创建数据库操作对象
        SQLiteDatabase db = null;
        try{
            db = helper.getReadableDatabase();
            //查询  按时间降序
            Cursor cursor = db.query("t_bill",null,"username = ? and type = ?",new String[]{username,type},null,null,"date desc",null);
            while (cursor.moveToNext()){
                //取数据
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String uname = cursor.getString(cursor.getColumnIndex("username"));
                String utype = cursor.getString(cursor.getColumnIndex("type"));
                String sort = cursor.getString(cursor.getColumnIndex("sort"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                double money = cursor.getDouble(cursor.getColumnIndex("money"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                //封装到账单对象中
                Bill bill = new Bill(id,uname,utype,sort,date,money,remark);
                billList.add(bill);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db!=null){
                db.close();  //关闭数据库
            }
        }
        return billList;
    }
}
