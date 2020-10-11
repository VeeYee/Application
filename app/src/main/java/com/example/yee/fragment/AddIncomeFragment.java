package com.example.yee.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.yee.MainActivity;
import com.example.yee.R;
import com.example.yee.dao.BillDao;
import com.example.yee.entity.Bill;
import com.example.yee.entity.User;

import java.util.Calendar;

/**
 * 添加收入页面
 */
public class AddIncomeFragment extends Fragment{

    //13个按钮
    private ImageButton icon_wage;
    private ImageButton icon_award;
    private ImageButton icon_red;
    private ImageButton icon_part;
    private ImageButton icon_fund;

    private ImageButton icon_winning;
    private ImageButton icon_manage;
    private ImageButton icon_business;
    private ImageButton icon_transfer;
    private ImageButton icon_stock;

    private ImageButton icon_interest;
    private ImageButton icon_subsidy;
    private ImageButton icon_other;

    //账单其他控件
    private EditText input_date;  //支出日期
    private EditText input_money;  //支出金额
    private EditText input_remark; //备注
    private ImageView save_btn; //保存按钮

    //账单的信息
    private String username;  //当前用户
    private String type = "收入";  //账单类型 （支出、收入）
    private String sort;  //账单分类（工资、红包、津贴..等）
    private String date;  //账单日期
    private double money; //账单金额
    private String remark; //账单备注

    //账单对象
    private Bill bill = new Bill();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_income,container, false);
        //1.初始化13个图标按钮
        icon_wage = (ImageButton)view.findViewById(R.id.icon_wage);
        icon_award = (ImageButton)view.findViewById(R.id.icon_award);
        icon_red = (ImageButton)view.findViewById(R.id.icon_red);
        icon_part = (ImageButton)view.findViewById(R.id.icon_part);
        icon_fund = (ImageButton)view.findViewById(R.id.icon_fund);

        icon_winning = (ImageButton)view.findViewById(R.id.icon_winning);
        icon_manage = (ImageButton)view.findViewById(R.id.icon_manage);
        icon_business = (ImageButton)view.findViewById(R.id.icon_business);
        icon_transfer = (ImageButton)view.findViewById(R.id.icon_transfer);
        icon_stock = (ImageButton)view.findViewById(R.id.icon_stock);

        icon_interest = (ImageButton)view.findViewById(R.id.icon_interest);
        icon_subsidy = (ImageButton)view.findViewById(R.id.icon_subsidy);
        icon_other = (ImageButton)view.findViewById(R.id.icon_other);
        //所有图标的点击事件
        initClick();
        //默认选中第一个图标-工资
        bill.setSort(getText(R.string.tv_wage).toString());

        //2.初始化其他控件
        input_date = (EditText)view.findViewById(R.id.input_date);
        input_money = (EditText)view.findViewById(R.id.input_money);
        input_remark = (EditText)view.findViewById(R.id.input_remark);
        save_btn = (ImageView)view.findViewById(R.id.save_btn);

        //点击日期输入框弹出日历
        final Calendar calendar = Calendar.getInstance();
        input_date.setInputType(InputType.TYPE_NULL);  //不弹出键盘
        input_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        //设置选中的日期
                        input_date.setText(DateFormat.format("yyy.MM.dd", calendar));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        User user = ((MainActivity)(getActivity())).user;  //从主activity中获取当前登录的用户
        username = user.getUsername();
        //点击保存按钮
        saveBill(save_btn);
        return view;
    }

    //1.重置图标的样式
    private void resetIcon(ImageButton imageButton, int icon){
        imageButton.setImageDrawable(getResources().getDrawable(icon));
        imageButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_button_normal));
    }

    //2.初始化所有的图标
    private void initIcon(){
        resetIcon(icon_wage,R.mipmap.icon_i_wage_normal);
        resetIcon(icon_award,R.mipmap.icon_i_award_normal);
        resetIcon(icon_red,R.mipmap.icon_i_red_normal);
        resetIcon(icon_part,R.mipmap.icon_i_part_normal);
        resetIcon(icon_fund,R.mipmap.icon_i_fund_normal);

        resetIcon(icon_winning,R.mipmap.icon_i_winning_normal);
        resetIcon(icon_manage,R.mipmap.icon_i_manage_normal);
        resetIcon(icon_business,R.mipmap.icon_i_business_normal);
        resetIcon(icon_transfer,R.mipmap.icon_i_transfer_normal);
        resetIcon(icon_stock,R.mipmap.icon_i_stock_normal);

        resetIcon(icon_interest,R.mipmap.icon_i_interest_normal);
        resetIcon(icon_subsidy,R.mipmap.icon_i_subsidy_normal);
        resetIcon(icon_other,R.mipmap.icon_i_other_normal);
    }

    //3.给图标绑定的点击事件（点击图标，切换图标样式）
    private void selectIcon(final ImageButton imageButton, final int icon, final String sort){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击前先初始化所有图标的样式
                initIcon();
                imageButton.setImageDrawable(getResources().getDrawable(icon));
                imageButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_button_selected));
                //设置账单的分类
                bill.setSort(sort);
            }
        });
    }

    //4.初始化图标的点击事件
    private void initClick(){
        selectIcon(icon_wage,R.mipmap.icon_i_wage_selected,getText(R.string.tv_wage).toString());
        selectIcon(icon_award,R.mipmap.icon_i_award_selected,getText(R.string.tv_award).toString());
        selectIcon(icon_red,R.mipmap.icon_i_red_selected,getText(R.string.tv_red).toString());
        selectIcon(icon_part,R.mipmap.icon_i_part_selected,getText(R.string.tv_part).toString());
        selectIcon(icon_fund,R.mipmap.icon_i_fund_selected,getText(R.string.tv_fund).toString());

        selectIcon(icon_winning,R.mipmap.icon_i_winning_selected,getText(R.string.tv_winning).toString());
        selectIcon(icon_business,R.mipmap.icon_i_business_selected,getText(R.string.tv_business).toString());
        selectIcon(icon_manage,R.mipmap.icon_i_manage_selected,getText(R.string.tv_manage).toString());
        selectIcon(icon_transfer,R.mipmap.icon_i_transfer_selected,getText(R.string.tv_transfer).toString());
        selectIcon(icon_stock,R.mipmap.icon_i_stock_selected,getText(R.string.tv_stock).toString());

        selectIcon(icon_interest,R.mipmap.icon_i_interest_selected,getText(R.string.tv_interest).toString());
        selectIcon(icon_subsidy,R.mipmap.icon_i_subsidy_selected,getText(R.string.tv_subsidy).toString());
        selectIcon(icon_other,R.mipmap.icon_i_other_selected,getText(R.string.tv_other).toString());
    }

    //点击"保存"按钮，保存账单
    private void saveBill(ImageView button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = input_date.getText().toString();  //账单日期
                if("".equals(date) || date.length() == 0){
                    Toast.makeText(v.getContext(),"请填写收入日期",Toast.LENGTH_SHORT).show();
                    return;
                }
                String moneyStr = input_money.getText().toString();
                if("".equals(moneyStr) || moneyStr.length() == 0){
                    Toast.makeText(v.getContext(),"请填写收入金额",Toast.LENGTH_SHORT).show();
                    return;
                }
                money = Double.parseDouble(moneyStr);  //账单金额
                remark = input_remark.getText().toString(); //账单备注 （可不填）
                //属性封装
                bill.setUsername(username);
                bill.setType(type);
                bill.setDate(date);
                bill.setMoney(money);
                bill.setRemark(remark);
                //调用数据操作方法
                BillDao billDao = new BillDao(getContext());
                long result = billDao.addBill(bill);
                if(result>0){
                    Toast.makeText(v.getContext(),"保存成功",Toast.LENGTH_SHORT).show();
                    //清空数据
                    input_date.setText("");
                    input_money.setText("");
                    input_remark.setText("");
                }else{
                    Toast.makeText(v.getContext(),"保存失败，请重试！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

