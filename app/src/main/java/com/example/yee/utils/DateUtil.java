package com.example.yee.utils;

import com.example.yee.entity.Bill;

import java.util.*;
import java.text.*;


public class DateUtil {

    //根据年月日 计算星期
    public static String dateToWeek(String datetime) throws java.text.ParseException {
        datetime = datetime.split("\\.")[0]+"-"
                + datetime.split("\\.")[1]+"-"
                + datetime.split("\\.")[2];
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date date = (Date) f.parse(datetime);
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 切换账单的日期需要更新数据
     * @param month
     * @param year
     * @param billList
     * @param dateList
     */
    public static void dateToUpdate(String month, String year, List<Bill> billList,List dateList){
        //使用迭代器删除集合中不符合条件的项
        Iterator dateIterator = dateList.iterator();
        while(dateIterator.hasNext()){
            String s = (String)dateIterator.next();
            String date_m = s.split("\\.")[1];  //月
            String date_y = s.split("\\.")[0];  //年
            if(!date_y.equals(year) || !date_m.equals(month)){
                dateIterator.remove();
            }
        }

        Iterator<Bill> billIterator = billList.iterator();
        while(billIterator.hasNext()){
            Bill bill = billIterator.next();
            String date_m = bill.getDate().split("\\.")[1];  //月
            String date_y = bill.getDate().split("\\.")[0];  //年
            if(!date_y.equals(year) || !date_m.equals(month)){
                billIterator.remove();
            }
        }
    }

    /**
     * 根据本年的周数获取7天日期范围
     * @param week  周数
     * @return
     */
    public static String getWeekDays(Integer week){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
        Calendar cal=Calendar.getInstance();
        String year = new SimpleDateFormat("yyyy").format(new Date()); //当前年份
        cal.setFirstDayOfWeek(Calendar.MONDAY);  //星期一
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        String beginDate = sdf.format(cal.getTime());  //获取星期一开始的时间戳
        cal.add(Calendar.DAY_OF_WEEK, 6); //6天后
        String endDate = sdf.format(cal.getTime());
        return beginDate+"~"+endDate;
    }

    /**
     * 判断某个日期是否在一个日期范围内
     * @param begin
     * @param end
     * @param day
     * @return
     */
    public static boolean dayInWeek(String begin,String end,String day){
        boolean flag = false;
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date date1 = simpleDateFormat.parse(begin);
            Date date2 = simpleDateFormat.parse(end);
            Date date3 = simpleDateFormat.parse(day);
            if(date1.getTime()<=date3.getTime() && date2.getTime()>=date3.getTime()){
                flag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 计算指定日期与当前日期的相差天数
     * @param date
     * @return
     */
    public static long hasDay(String date){
        long day = 0;
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date before = simpleDateFormat.parse(date);
            Date now = new Date();
            day = (now.getTime()-before.getTime())/ (1000L*3600L*24L);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

//    public static void main(String[] args) {
//        System.out.println(hasDay("2020-05-30"));
//    }
}
