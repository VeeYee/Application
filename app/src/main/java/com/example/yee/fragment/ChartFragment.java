package com.example.yee.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.yee.MainActivity;
import com.example.yee.R;
import com.example.yee.dao.BillDao;
import com.example.yee.entity.Bill;
import com.example.yee.entity.Chart;
import com.example.yee.entity.User;
import com.example.yee.utils.ChartUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 统计页面
 */
public class ChartFragment extends Fragment{

    //时间按钮
    private TextView btn_week;
    private TextView btn_month;
    private TextView btn_year;
    //支出收入按钮
    private TextView btn_outlay;
    private TextView btn_income;
    private TextView outlay_style;
    private TextView income_style;
    //左右箭头
    private ImageView btn_left;
    private ImageView btn_right;
    private TextView nowTime; //视图的具体时间
    //显示图标的列表
    private ListView chart_items;
    //数据
    private User user;
    private BillDao billDao;
    private List<Bill> billList;
    private List sortList;
    private List<Chart> chartList;  //最后显示的数据
    //适配器
    private MyAdapter myAdapter;

    //按条件查询
    private int typeOI; //收支类型
    private int typeWMY; //周月年
    private int typeLR; //左右箭头
    private String nowMonth;  //当前月份
    private String nowYear; //当前年份
    private String nowWeek; //当前周

    private int now_week;  //本周周次

    private static final int OUT = 1;
    private static final int IN = 2;
    private static final int WEEK = 1;
    private static final int MONTH = 2;
    private static final int YEAR = 3;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart,container, false);
        //初始化控件
        btn_week = view.findViewById(R.id.btn_week);
        btn_month = view.findViewById(R.id.btn_month);
        btn_year = view.findViewById(R.id.btn_year);
        btn_outlay = view.findViewById(R.id.btn_outlay);
        btn_outlay.getPaint().setFakeBoldText(true);
        btn_income = view.findViewById(R.id.btn_income);
        outlay_style = view.findViewById(R.id.outlay_style);
        income_style = view.findViewById(R.id.income_style);
        btn_left = view.findViewById(R.id.btn_left);
        btn_right = view.findViewById(R.id.btn_right);
        nowTime = view.findViewById(R.id.now_time);
        chart_items = view.findViewById(R.id.chart_items);
        user = ((MainActivity)getActivity()).user;
        billDao = new BillDao(view.getContext());
        chartList = new ArrayList<>();
        //设置适配器
        myAdapter = new MyAdapter();
        chart_items.setAdapter(myAdapter);
        //默认显示月视图、支出、当前月的账单情况
        typeWMY = MONTH;
        typeOI = OUT;
        nowMonth = new SimpleDateFormat("MM").format(new Date()); //当前时间的月份
        nowTime.setText(nowMonth+"月");
        //第一次加载
        nowYear = new SimpleDateFormat("yyyy").format(new Date()); //当前年份
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date());
        nowWeek = calendar.get(Calendar.WEEK_OF_YEAR)+"";  //当前周次
        now_week = calendar.get(Calendar.WEEK_OF_YEAR); //本周周次
        //第一次加载数据
        initData();
        //根据周月年查看统计图
        selectByWMY(btn_week,WEEK);
        selectByWMY(btn_month,MONTH);
        selectByWMY(btn_year,YEAR);
        //根据收支类型查看统计图
        selectByOI(btn_outlay,OUT);
        selectByOI(btn_income,IN);
        //左右箭头的点击事件
        selectByLR(btn_left,LEFT);
        selectByLR(btn_right,RIGHT);

        return view;
    }

    //按钮未选中的默认样式
    private void normalByWMY(){
        btn_week.setBackgroundDrawable(getResources().getDrawable(R.drawable.date_week_normal));
        btn_week.setTextColor(getResources().getColor(R.color.btn_normal));
        btn_month.setBackgroundDrawable(getResources().getDrawable(R.drawable.date_month_normal));
        btn_month.setTextColor(getResources().getColor(R.color.btn_normal));
        btn_year.setBackgroundDrawable(getResources().getDrawable(R.drawable.date_year_normal));
        btn_year.setTextColor(getResources().getColor(R.color.btn_normal));
    }

    //设置“周 月 年”选中后的样式
    private void selectByWMY(final TextView button, final int type){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalByWMY();
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.date_button_selected));
                button.setTextColor(getResources().getColor(R.color.btn_selected));
                typeWMY = type;  //设置周月年当前的值
                //更改具体时间
                if(typeWMY == WEEK){
                    if(Integer.parseInt(nowWeek) == now_week){
                        //如果查看的周是本周周次，则显示本周
                        nowTime.setText("本周");
                    }else if(Integer.parseInt(nowWeek) == now_week-1){
                        nowTime.setText("上周");
                    }else{
                        nowTime.setText(nowWeek+"周"); //其他周
                    }
                }else if(typeWMY == MONTH){
                    nowTime.setText(nowMonth+"月");
                }else{
                    nowTime.setText(nowYear+"年");
                }
                initData();  //重新加载数据
            }
        });
    }

    //收支按钮未选中的样式
    private void normalByOI(){
        btn_outlay.setTextColor(getResources().getColor(R.color.oi_normal));
        btn_outlay.getPaint().setFakeBoldText(false);
        outlay_style.setBackgroundDrawable(getResources().getDrawable(R.drawable.chart_oi_normal));
        btn_income.setTextColor(getResources().getColor(R.color.oi_normal));
        btn_income.getPaint().setFakeBoldText(false);
        income_style.setBackgroundDrawable(getResources().getDrawable(R.drawable.chart_oi_normal));
    }
    //收支按钮选择中后的样式
    private void selectByOI(final TextView button,final int type){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalByOI();
                button.setTextColor(getResources().getColor(R.color.oi_selected));
                button.getPaint().setFakeBoldText(true);  //粗体
                typeOI = type; //设置当前收支类型
                if(typeOI == OUT){
                    outlay_style.setBackgroundDrawable(getResources().getDrawable(R.drawable.chart_oi_selected));
                }else{
                    income_style.setBackgroundDrawable(getResources().getDrawable(R.drawable.chart_oi_selected));
                }
                initData(); //重新加载数据
            }
        });
    }

    //点击左右箭头时间
    private void selectByLR(ImageView button, final int type){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeLR = type;
                switch (typeWMY){
                    case WEEK:
                        showWeek();
                        break;
                    case MONTH:
                        showMonth();
                        break;
                    case YEAR:
                        showYear();
                        break;
                    default:
                        break;
                }
                initData();  //切换后重新加载数据
            }
        });
    }

    //显示不同周的视图
    private void showWeek(){
        int week = Integer.parseInt(nowWeek);  //当前查看的周次
        if(typeLR == LEFT && week>1){
            week -= 1;
        }else if(typeLR == RIGHT && week<now_week){
            week += 1;
        }
        if(week == now_week){
            nowWeek = now_week+"";
            nowTime.setText("本周");
        }else if(week == now_week-1){
            nowWeek = now_week-1+"";
            nowTime.setText("上周");
        }else{
            nowWeek = week+"";
            nowTime.setText(nowWeek+"周");
        }
    }

    //显示不同月份的视图
    private void showMonth(){
        String now_time = nowTime.getText().toString();
        int month = Integer.parseInt(now_time.substring(0,2));
        if(typeLR == LEFT && month>1){
            month -= 1;
        }else if(typeLR == RIGHT && month < 12){
            month += 1;
        }
        if((""+month).length() == 1){
            nowMonth = "0"+month;
            nowTime.setText(nowMonth+"月");
        }else{
            nowMonth = month+"";
            nowTime.setText(nowMonth+"月");
        }
    }

    //显示不同年份的视图
    private void showYear(){
        String now_time = nowTime.getText().toString();
        int year = Integer.parseInt(now_time.substring(0,4));
        if(typeLR == LEFT && year>1){
            year -= 1;
        }else {
            year += 1;
        }
        nowYear = year+"";
        nowTime.setText(nowYear+"年");
    }

    //处理ListView的数据源
    private void initData(){
        //判断收支类型
        if(typeOI == OUT){
            billList = billDao.queryBill(user.getUsername(),"支出"); //查询用户的所有账单
            sortList = billDao.querySort(user.getUsername(),"支出"); //查询用户账单中的所有分类
        }else{
            billList = billDao.queryBill(user.getUsername(),"收入");
            sortList = billDao.querySort(user.getUsername(),"收入");
        }
        //判断视图类型  周 月 年
        if(typeWMY == WEEK){
            //周视图
            chartList = ChartUtil.ChartByWeek(nowWeek,billList,sortList);
        }else if(typeWMY == MONTH){
            //月视图
            chartList = ChartUtil.ChartByMonth(nowMonth,billList,sortList);
        }else{
            //年视图
            chartList = ChartUtil.ChartByYear(nowYear,billList,sortList);
        }
        myAdapter.notifyDataSetChanged();  //通知数据更新
    }

    //重现加载页面时
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initData(); //重新加载数据
        }
    }

    //适配器
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return chartList.size();
        }

        @Override
        public Object getItem(int position) {
            return chartList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View chartView = View.inflate(getContext(),R.layout.chart_item,null);
            ImageButton sort_icon = chartView.findViewById(R.id.sort_icon);
            TextView sort_name = chartView.findViewById(R.id.sort_name);
            TextView sort_percent = chartView.findViewById(R.id.sort_percent);
            TextView sort_money = chartView.findViewById(R.id.sort_money);
            TextView sort_bar = chartView.findViewById(R.id.sort_bar);
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) sort_bar.getLayoutParams(); //获取当前控件的布局对象
            //图标背景
            switch (position%6){
                case 1:
                    sort_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.chart_icon_one));
                    break;
                case 2:
                    sort_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.chart_icon_two));
                    break;
                case 3:
                    sort_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.chart_icon_three));
                    break;
                case 4:
                    sort_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.chart_icon_four));
                    break;
                case 5:
                    sort_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.chart_icon_five));
                    break;
                default:
                    sort_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.chart_icon_six));
                    break;
            }
            //获取数据
            Chart chart = chartList.get(position);
            //显示数据
            sort_icon.setImageDrawable(getResources().getDrawable(chart.getSort_icon()));
            sort_name.setText(chart.getSort_name());
            sort_percent.setText(chart.getSort_percent());
            sort_money.setText(new DecimalFormat("#0.00").format(chart.getSort_money()));
            //获取屏幕宽度
            WindowManager wm = getActivity().getWindowManager();
            int width = wm.getDefaultDisplay().getWidth();
            params.width *= -1;
            params.width *= width*0.77*chart.getSort_bar();  //设置当前控件布局的宽度
            sort_bar.setLayoutParams(params);  //将设置好的布局参数应用到控件中
            return chartView;
        }
    }
}
