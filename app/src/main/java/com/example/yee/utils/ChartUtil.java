package com.example.yee.utils;

import com.example.yee.entity.Bill;
import com.example.yee.entity.Chart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 统计数据
 */
public class ChartUtil {

    //公共方法
    private static List<Chart> common(double total_money,List<Bill> billList,List sortList){
        //存放结果的集合
        List<Chart> chartList = new ArrayList<>();
        //移出不是本时间内的分类
        if(billList.size() == 0){
            sortList.clear();  //没有账单，则将所有分类清空
        }
        Iterator sortIterator = sortList.iterator();
        while(sortIterator.hasNext()){
            String s = (String) sortIterator.next();
            for(int i=0; i<billList.size(); i++){
                if(billList.get(i).getSort().equals(s)){
                    break;
                }
                if(!billList.get(i).getSort().equals(s) && i+1 == billList.size()){
                    sortIterator.remove();
                }
            }
        }
        //统计该分类下的账单
        for(int i=0; i<sortList.size(); i++){
            double sort_money = 0; //单个分类的金额
            String sort_name = sortList.get(i).toString(); //分类名
            int sort_icon = IconUtil.getChart_Icon(sort_name); //分类图标
            for(int j=0; j<billList.size(); j++){
                if(sortList.get(i).equals(billList.get(j).getSort())){
                    sort_money += billList.get(j).getMoney();
                }
            }
            double sort_bar = sort_money/total_money;  //宽度
            double sort_percent = sort_bar*100; //百分比
            //封装数据
            Chart chart = new Chart(sort_icon,sort_name,Math.round(sort_percent)+"%",sort_money,sort_bar);
            chartList.add(chart);
        }
        return chartList;
    }

    /**
     * 根据周数进行统计
     * @param week
     * @param billList
     * @param sortList
     * @return
     */
    public static List<Chart> ChartByWeek(String week, List<Bill> billList, List sortList) {
        //获取本周的时间范围
        String range = DateUtil.getWeekDays(Integer.parseInt(week));
        //移出不是本周的账单
        double total_money = 0;  //总金额
        Iterator<Bill> billIterator = billList.iterator();
        while(billIterator.hasNext()){
            Bill bill = billIterator.next();
            String date = bill.getDate();  //日期
            if(!DateUtil.dayInWeek(range.split("~")[0],range.split("~")[1],date)){
                billIterator.remove(); //不在本周7天内则移出
            }else{
                total_money += bill.getMoney();
            }
        }
        List<Chart> chartList = common(total_money,billList,sortList);
        return chartList;
    }

    /**
     * 根据月份进行统计
     * @param month
     * @param billList
     * @param sortList
     * @return
     */
    public static List<Chart> ChartByMonth(String month, List<Bill> billList, List sortList){
        //移出不是本月的账单
        double total_money = 0;  //总金额
        Iterator<Bill> billIterator = billList.iterator();
        while(billIterator.hasNext()){
            Bill bill = billIterator.next();
            String date_m = bill.getDate().split("\\.")[1];  //月
            if(!date_m.equals(month)){
                billIterator.remove();
            }else{
                total_money += bill.getMoney();
            }
        }
        List<Chart> chartList = common(total_money,billList,sortList);
        return chartList;
    }

    /**
     * 根据年份进行统计
     * @param year
     * @param billList
     * @param sortList
     * @return
     */
    public static List<Chart> ChartByYear(String year, List<Bill> billList, List sortList){
        //移出不是本年的数据
        double total_money = 0;  //总金额
        Iterator<Bill> billIterator = billList.iterator();
        while(billIterator.hasNext()){
            Bill bill = billIterator.next();
            String date_y = bill.getDate().split("\\.")[0];  //年
            if(!date_y.equals(year)){
                billIterator.remove();
            }else{
                total_money += bill.getMoney();
            }
        }
        if(billList.size() == 0){
            sortList.clear();  //没有账单，则将所有分类清空
        }
        List<Chart> chartList = common(total_money,billList,sortList);
        return chartList;
    }

    /**
     * 计算本年每月的账单金额
     * @param billList  账单集合
     * @return
     */
    public static double[] ChartEveryMonth(List<Bill> billList){
        //当前年份
        String year = new SimpleDateFormat("yyyy").format(new Date());
        double[] money  = new double[12];
        //初始化数组
        for(int i=0; i<money.length; i++){
            money[i] = 0;
        }
        for (Bill bill : billList){
            String b_year = bill.getDate().split("\\.")[0];
            String b_month = bill.getDate().split("\\.")[1];
            if(b_year.equals(year)){
                int month = Integer.parseInt(b_month);
                money[month-1] += bill.getMoney();
            }
        }
        return money;
    }
}
