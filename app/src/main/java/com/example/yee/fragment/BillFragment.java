package com.example.yee.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.yee.DateTimeDialogOnlyYMD;
import com.example.yee.MainActivity;
import com.example.yee.R;
import com.example.yee.dao.BillDao;
import com.example.yee.entity.Bill;
import com.example.yee.entity.User;
import com.example.yee.utils.DateUtil;
import com.example.yee.utils.IconUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 账单页
 */
public class BillFragment extends Fragment implements View.OnClickListener,DateTimeDialogOnlyYMD.MyOnDateSetListener{

    //时间
    private TextView bill_date;
    //总金额
    private TextView bill_outlay;
    private TextView bill_income;
    //listView列表
    private ListView list_items;
    //登录用户
    private User user;
    //数据库层
    private BillDao billDao;
    //数据集合
    private List<Bill> billList;  //所有账单（按日期降序）
    private List dateList; //所有日期（按日期降序）
    private List<Bill> viewList; //综合后的数据
    //适配器
    private MyAdapter myAdapter;
    //日期选择器
    private DateTimeDialogOnlyYMD dateTimeDialogOnlyYM;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    //当前账单的年和月
    private String year;
    private String month;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill,container, false);
        //初始化数据
        bill_date = view.findViewById(R.id.bill_date);
        bill_outlay = view.findViewById(R.id.bill_outlay);
        bill_income = view.findViewById(R.id.bill_income);
        list_items = view.findViewById(R.id.list_items);
        user = ((MainActivity)getActivity()).user;
        billDao = new BillDao(view.getContext());
        viewList = new ArrayList<>();
        myAdapter = new MyAdapter();
        dateTimeDialogOnlyYM = new DateTimeDialogOnlyYMD(getContext(), this, false, true, true);
        //点击日期弹出日期选择器
        bill_date.setOnClickListener(this);
        onDateSet(new Date());  //默认显示现在的时间

        //设置适配器
        list_items.setAdapter(myAdapter);
        return view;
    }

    //账单日期的点击事件
    @Override
    public void onClick(View v) {
        dateTimeDialogOnlyYM.hideOrShow();
    }
    //设置显示的时间格式
    @Override
    public void onDateSet(Date date) {
        String str = mFormatter.format(date);
        String[] s =  str.split("-");
        bill_date.setText(s[1]+"月，"+s[0]);
        //更新当前年与月
        year = bill_date.getText().toString().split("，")[1];
        month = bill_date.getText().toString().split("，")[0].substring(0,2);
        //处理数据
        initData();
    }

    //计算支出和收入的总金额
    private void CountMoney(){
        double outlay_money = 0;
        double income_money = 0;
        for(Bill bill:billList){
            if(bill.getType().equals("支出")){
                outlay_money += bill.getMoney();
            }else{
                income_money += bill.getMoney();
            }
        }
        bill_outlay.setText("-"+new DecimalFormat("#0.00").format(outlay_money));
        bill_income.setText("+"+new DecimalFormat("#0.00").format(income_money));
    }

    //处理listView中的数据
    private void initData(){
        //1.从数据库取出数据
        billList = billDao.queryBill(user.getUsername());  //查询出的账单数据
        dateList = billDao.queryBillDate(user.getUsername());  //查询出所有的日期
        //2.按日期处理数据
        DateUtil.dateToUpdate(month,year,billList,dateList);
        //3.计算支出、收入总金额
        CountMoney();
        //4.合并集合，显示数据
        viewList.clear();  //先清空集合
        Bill bill = null;
        int index = 0;  //账单集合的索引
        for(int i=0; i<dateList.size(); i++){
            double outlay_money = 0;  //支出的金额
            double income_money = 0; //收入金额
            //先加入日期
            bill = new Bill(0,null,null,null,dateList.get(i).toString(),0,null);
            viewList.add(bill);
            for (int j=index; j<billList.size(); j++){
                if(dateList.get(i).equals(billList.get(j).getDate())){
                    if(billList.get(j).getType().equals("支出")){
                        outlay_money += billList.get(j).getMoney();
                    }else{
                        income_money += billList.get(j).getMoney();
                    }
                    //日期相同，添加账单
                    viewList.add(billList.get(j));
                    if(j+1 == billList.size()){  //最后一个日期的金额
                        bill.setRemark(new DecimalFormat("#0.00").format(outlay_money)
                                +"-"
                                +new DecimalFormat("#0.00").format(income_money));  //账单支出、收入小计
                    }
                }else{
                    index = j;
                    bill.setRemark(new DecimalFormat("#0.00").format(outlay_money)
                            +"-"
                            +new DecimalFormat("#0.00").format(income_money));  //账单支出、收入小计
                    break;
                }
            }
        }
        //5.通知适配器数据改变
        myAdapter.notifyDataSetChanged();
    }

    //重新切换到此页面则变更数据
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            //重新处理数据列表
            initData();
        }
    }

    //适配器类
    class MyAdapter extends BaseAdapter{

        private static final int BILL_DATE_ITEM = 0;  //显示日期的item
        private static final int BILL_ITEM = 1;  //显示账单的item

        @Override
        public int getCount() {
            return viewList.size();  //数据大小
        }

        @Override
        public Object getItem(int position) {
            //返回数据项
            return viewList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;  //索引
        }

        //返回不同的item类型
        @Override
        public int getItemViewType(int position) {
            //id为0显示日期item，否则显示账单item
            if(viewList.get(position).getId()==0){
                return BILL_DATE_ITEM;
            }else{
                return BILL_ITEM;
            }
        }

        //item的类型数量  2种类型
        @Override
        public int getViewTypeCount() {
            return 2;
        }

        //显示日期的item控件
        class DateViewHolder{
            TextView bill_date_date; //日期
            TextView bill_date_outlay; //支出金额
            TextView bill_date_income;  //收入金额
        }

        //显示账单的item控件
        class BillViewHolder{
            ImageView bill_sort_icon; //图标
            TextView bill_sort;  //分类
            TextView bill_remark; //备注
            TextView bill_money; //金额
            ImageView bill_delete; //删除按钮
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //两个布局视图
            View dateView = null;
            View billView = null;

            int currentType = getItemViewType(position);
            //如果显示日期
            if(currentType == BILL_DATE_ITEM){
                DateViewHolder dateViewHolder = null;
                //第一次创建布局
                if(dateView == null){
                    dateViewHolder = new DateViewHolder();
                    dateView = LayoutInflater.from(getContext()).inflate(R.layout.bill_date_item,null); //选择日期布局
                    //初始化控件
                    dateViewHolder.bill_date_date = dateView.findViewById(R.id.bill_date_date); //日期
                    dateViewHolder.bill_date_outlay = dateView.findViewById(R.id.bill_date_outlay); //支出金额
                    dateViewHolder.bill_date_income = dateView.findViewById(R.id.bill_date_income); //收入金额
                    dateView.setTag(dateViewHolder);
                    convertView = dateView; //显示日期布局
                }else{
                    //不是第一创建，不需要初始化控件
                    dateViewHolder = (DateViewHolder)convertView.getTag();
                }
                //设置控件的值
                String date = viewList.get(position).getDate();
                String week = "";
                try {
                    week = DateUtil.dateToWeek(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dateViewHolder.bill_date_date.setText(date.split("\\.")[1]+"月"+date.split("\\.")[2]+"日"+"  "+week);
                dateViewHolder.bill_date_outlay.setText("支出：-"+viewList.get(position).getRemark().split("-")[0]);
                dateViewHolder.bill_date_income.setText("收入：+"+viewList.get(position).getRemark().split("-")[1]);
            }else if(currentType == BILL_ITEM){
                //如果显示账单
                BillViewHolder billViewHolder = null;
                //第一次创建布局
                if(billView == null){
                    billViewHolder = new BillViewHolder();
                    billView = LayoutInflater.from(getContext()).inflate(R.layout.bill_item,null); //选择账单布局
                    //初始化控件
                    billViewHolder.bill_sort_icon = billView.findViewById(R.id.bill_sort_icon);  //分类图标
                    billViewHolder.bill_sort = billView.findViewById(R.id.bill_sort);  //分类
                    billViewHolder.bill_remark = billView.findViewById(R.id.bill_remark); //备注
                    billViewHolder.bill_money = billView.findViewById(R.id.bill_money); //金额
                    billViewHolder.bill_delete = billView.findViewById(R.id.bill_delete); //删除按钮
                    billView.setTag(billViewHolder);
                    convertView = billView; //显示账单布局
                }else{
                    billViewHolder = (BillViewHolder)convertView.getTag();
                }
                //获取数据项
                final Bill bill = (Bill)viewList.get(position);
                //设置列表项的值
                int icon = IconUtil.getSort_Icon(bill.getSort());  //获取分类对应的图标
                billViewHolder.bill_sort_icon.setImageDrawable(getResources().getDrawable(icon));  //图标
                billViewHolder.bill_sort.setText(bill.getSort()); //分类
                if(bill.getRemark().equals("")){ //备注
                    billViewHolder.bill_remark.setText("无");
                }else{
                    billViewHolder.bill_remark.setText(bill.getRemark());
                }
                if(bill.getType().equals("支出")){ //金额
                    billViewHolder.bill_money.setText("-"+bill.getMoney());
                }else{
                    billViewHolder.bill_money.setText("+"+bill.getMoney());
                }
                //删除账单
                billViewHolder.bill_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //建立对话框的监听对象，用户确定后调用此监听代码
                        android.content.DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除操作，重新处理数据
                                if(billDao.deleteBill(bill.getId())>0){
                                    Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                                    initData();
                                }else{
                                    Toast.makeText(getContext(),"删除失败，请重试",Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        //建立对话框
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("您确定要删除吗？");
                        builder.setPositiveButton("确定",ok);
                        builder.setNegativeButton("取消",null);
                        builder.show();
                    }
                });
            }
            return convertView;
        }
    }
}
