package com.example.yee.entity;

import java.util.Date;

/**
 * 账单实体类
 */
public class Bill {

    private int id;  //账单id
    private String username; //账号
    private String type; //账单类型 （支出、收入）
    private String sort; //账单分类 （餐饮、娱乐、医疗...)
    private String date; //账单日期
    private double money;  //账单金额
    private String remark; //账单备注

    public Bill() {
    }

    public Bill(int id, String username, String type, String sort, String date, double money, String remark) {
        this.id = id;
        this.username = username;
        this.type = type;
        this.sort = sort;
        this.date = date;
        this.money = money;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", type='" + type + '\'' +
                ", sort='" + sort + '\'' +
                ", money=" + money +
                ", date='" + date + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
