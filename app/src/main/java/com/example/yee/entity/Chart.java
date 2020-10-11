package com.example.yee.entity;

/**
 * 统计实体类
 */
public class Chart {

    private int sort_icon;  //分类图标
    private String sort_name; //分类名
    private String sort_percent; //该分类消费的百分比
    private double sort_money; //该分类下消费的总金额
    private double sort_bar; //条形图的宽度

    public Chart() {
    }

    public Chart(int sort_icon, String sort_name, String sort_percent, double sort_money, double sort_bar) {
        this.sort_icon = sort_icon;
        this.sort_name = sort_name;
        this.sort_percent = sort_percent;
        this.sort_money = sort_money;
        this.sort_bar = sort_bar;
    }

    public int getSort_icon() {
        return sort_icon;
    }

    public void setSort_icon(int sort_icon) {
        this.sort_icon = sort_icon;
    }

    public String getSort_name() {
        return sort_name;
    }

    public void setSort_name(String sort_name) {
        this.sort_name = sort_name;
    }

    public String getSort_percent() {
        return sort_percent;
    }

    public void setSort_percent(String sort_percent) {
        this.sort_percent = sort_percent;
    }

    public double getSort_money() {
        return sort_money;
    }

    public void setSort_money(double sort_money) {
        this.sort_money = sort_money;
    }

    public double getSort_bar() {
        return sort_bar;
    }

    public void setSort_bar(double sort_bar) {
        this.sort_bar = sort_bar;
    }

    @Override
    public String toString() {
        return "Chart{" +
                "sort_icon=" + sort_icon +
                ", sort_name='" + sort_name + '\'' +
                ", sort_percent='" + sort_percent + '\'' +
                ", sort_money=" + sort_money +
                ", sort_bar=" + sort_bar +
                '}';
    }
}
