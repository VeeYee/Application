package com.example.yee.utils;


import com.example.yee.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取对应图标的帮助类
 */
public class IconUtil {

    private static Map<String,Object> sort_icon = new HashMap<>();
    private static Map<String,Object> chart_icon = new HashMap<>();  //统计页使用图标

    //初始化集合
    public static void initMap(){
        //账单页面使用的图标
        sort_icon.put("餐饮", R.mipmap.icon_o_food_normal);
        sort_icon.put("娱乐",R.mipmap.icon_o_play_normal);
        sort_icon.put("通讯",R.mipmap.icon_o_phone_normal);
        sort_icon.put("住房",R.mipmap.icon_o_home_normal);
        sort_icon.put("日用",R.mipmap.icon_o_daily_normal);

        sort_icon.put("社交",R.mipmap.icon_o_friend_normal);
        sort_icon.put("医疗",R.mipmap.icon_o_medical_normal);
        sort_icon.put("交通",R.mipmap.icon_o_traffic_normal);
        sort_icon.put("购物",R.mipmap.icon_o_shop_normal);
        sort_icon.put("旅游",R.mipmap.icon_o_tour_normal);

        sort_icon.put("宠物", R.mipmap.icon_o_pet_normal);
        sort_icon.put("教育",R.mipmap.icon_o_school_normal);
        sort_icon.put("运动",R.mipmap.icon_o_sport_normal);
        sort_icon.put("育儿",R.mipmap.icon_o_baby_normal);
        sort_icon.put("其他",R.mipmap.icon_i_other_normal);

        sort_icon.put("工资",R.mipmap.icon_i_wage_normal);
        sort_icon.put("奖金",R.mipmap.icon_i_award_normal);
        sort_icon.put("红包",R.mipmap.icon_i_red_normal);
        sort_icon.put("兼职",R.mipmap.icon_i_part_normal);
        sort_icon.put("投资",R.mipmap.icon_i_fund_normal);

        sort_icon.put("中奖",R.mipmap.icon_i_winning_normal);
        sort_icon.put("理财",R.mipmap.icon_i_manage_normal);
        sort_icon.put("生意",R.mipmap.icon_i_business_normal);
        sort_icon.put("转账",R.mipmap.icon_i_transfer_normal);
        sort_icon.put("股票",R.mipmap.icon_i_stock_normal);

        sort_icon.put("利息",R.mipmap.icon_i_interest_normal);
        sort_icon.put("津贴",R.mipmap.icon_i_subsidy_normal);

        //统计使用的图标
        chart_icon.put("餐饮", R.mipmap.icon_o_food_selected);
        chart_icon.put("娱乐",R.mipmap.icon_o_play_selected);
        chart_icon.put("通讯",R.mipmap.icon_o_phone_selected);
        chart_icon.put("住房",R.mipmap.icon_o_home_selected);
        chart_icon.put("日用",R.mipmap.icon_o_daily_selected);

        chart_icon.put("社交",R.mipmap.icon_o_friend_selected);
        chart_icon.put("医疗",R.mipmap.icon_o_medical_selected);
        chart_icon.put("交通",R.mipmap.icon_o_traffic_selected);
        chart_icon.put("购物",R.mipmap.icon_o_shop_selected);
        chart_icon.put("旅游",R.mipmap.icon_o_tour_selected);

        chart_icon.put("宠物", R.mipmap.icon_o_pet_selected);
        chart_icon.put("教育",R.mipmap.icon_o_school_selected);
        chart_icon.put("运动",R.mipmap.icon_o_sport_selected);
        chart_icon.put("育儿",R.mipmap.icon_o_baby_selected);
        chart_icon.put("其他",R.mipmap.icon_i_other_selected);

        chart_icon.put("工资",R.mipmap.icon_i_wage_selected);
        chart_icon.put("奖金",R.mipmap.icon_i_award_selected);
        chart_icon.put("红包",R.mipmap.icon_i_red_selected);
        chart_icon.put("兼职",R.mipmap.icon_i_part_selected);
        chart_icon.put("投资",R.mipmap.icon_i_fund_selected);

        chart_icon.put("中奖",R.mipmap.icon_i_winning_selected);
        chart_icon.put("理财",R.mipmap.icon_i_manage_selected);
        chart_icon.put("生意",R.mipmap.icon_i_business_selected);
        chart_icon.put("转账",R.mipmap.icon_i_transfer_selected);
        chart_icon.put("股票",R.mipmap.icon_i_stock_selected);

        chart_icon.put("利息",R.mipmap.icon_i_interest_selected);
        chart_icon.put("津贴",R.mipmap.icon_i_subsidy_selected);


    }

    //通过分类名获取对应的分类图标
    public static int getSort_Icon(String sort){
        //初始化集合
        initMap();
        int icon = R.mipmap.icon_o_other_normal;
        if(sort_icon.containsKey(sort)){
            icon = (int)sort_icon.get(sort);
        }
        return icon;
    }

    //通过分类名获取对应的分类图标
    public static int getChart_Icon(String sort){
        //初始化集合
        initMap();
        int icon = R.mipmap.icon_o_other_selected;
        if(chart_icon.containsKey(sort)){
            icon = (int)chart_icon.get(sort);
        }
        return icon;
    }
}
