package com.example.yee.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yee.MainActivity;
import com.example.yee.R;
import com.example.yee.dao.BillDao;
import com.example.yee.entity.Bill;
import com.example.yee.entity.User;

import java.util.Calendar;
import java.util.List;

/**
 * 添加支出页面
 */
public class AddOutlayFragment extends Fragment{

    //15个按钮
    private ImageButton icon_food;
    private ImageButton icon_play;
    private ImageButton icon_phone;
    private ImageButton icon_home;
    private ImageButton icon_daily;
    private ImageButton icon_friend;
    private ImageButton icon_medical;
    private ImageButton icon_traffic;
    private ImageButton icon_shop;
    private ImageButton icon_tour;
    private ImageButton icon_pet;
    private ImageButton icon_school;
    private ImageButton icon_sport;
    private ImageButton icon_baby;
    private ImageButton icon_other;

    //账单其他控件
    private EditText input_date;  //支出日期
    private EditText input_money;  //支出金额
    private EditText input_remark; //备注
    private ImageView save_btn; //保存按钮

    //账单的信息
    private String username;  //当前用户
    private String type = "支出";  //账单类型 （支出、收入）
    private String sort;  //账单分类（餐饮、娱乐、医疗..等）
    private String date;  //账单日期
    private double money; //账单金额
    private String remark; //账单备注

    //账单对象
    private Bill bill = new Bill();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_outlay,container, false);
        //1.初始化15个图标按钮
        icon_food = (ImageButton)view.findViewById(R.id.icon_food);
        icon_play = (ImageButton)view.findViewById(R.id.icon_play);
        icon_phone = (ImageButton)view.findViewById(R.id.icon_phone);
        icon_home = (ImageButton)view.findViewById(R.id.icon_home);
        icon_daily = (ImageButton)view.findViewById(R.id.icon_daily);
        icon_friend = (ImageButton)view.findViewById(R.id.icon_friend);
        icon_medical = (ImageButton)view.findViewById(R.id.icon_medical);
        icon_traffic = (ImageButton)view.findViewById(R.id.icon_traffic);
        icon_shop = (ImageButton)view.findViewById(R.id.icon_shop);
        icon_tour = (ImageButton)view.findViewById(R.id.icon_tour);
        icon_pet = (ImageButton)view.findViewById(R.id.icon_pet);
        icon_school = (ImageButton)view.findViewById(R.id.icon_school);
        icon_sport = (ImageButton)view.findViewById(R.id.icon_sport);
        icon_baby = (ImageButton)view.findViewById(R.id.icon_baby);
        icon_other = (ImageButton)view.findViewById(R.id.icon_other);
        //所有图标的点击事件
        initClick();
        //默认选中第一个图标-餐饮
        bill.setSort(getText(R.string.tv_food).toString());

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
        resetIcon(icon_food,R.mipmap.icon_o_food_normal);
        resetIcon(icon_play,R.mipmap.icon_o_play_normal);
        resetIcon(icon_phone,R.mipmap.icon_o_phone_normal);
        resetIcon(icon_home,R.mipmap.icon_o_home_normal);
        resetIcon(icon_daily,R.mipmap.icon_o_daily_normal);

        resetIcon(icon_friend,R.mipmap.icon_o_friend_normal);
        resetIcon(icon_medical,R.mipmap.icon_o_medical_normal);
        resetIcon(icon_traffic,R.mipmap.icon_o_traffic_normal);
        resetIcon(icon_shop,R.mipmap.icon_o_shop_normal);
        resetIcon(icon_tour,R.mipmap.icon_o_tour_normal);

        resetIcon(icon_pet,R.mipmap.icon_o_pet_normal);
        resetIcon(icon_school,R.mipmap.icon_o_school_normal);
        resetIcon(icon_sport,R.mipmap.icon_o_sport_normal);
        resetIcon(icon_baby,R.mipmap.icon_o_baby_normal);
        resetIcon(icon_other,R.mipmap.icon_o_other_normal);
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
        selectIcon(icon_food,R.mipmap.icon_o_food_selected,getText(R.string.tv_food).toString());
        selectIcon(icon_play,R.mipmap.icon_o_play_selected,getText(R.string.tv_play).toString());
        selectIcon(icon_phone,R.mipmap.icon_o_phone_selected,getText(R.string.tv_phone).toString());
        selectIcon(icon_home,R.mipmap.icon_o_home_selected,getText(R.string.tv_home).toString());
        selectIcon(icon_daily,R.mipmap.icon_o_daily_selected,getText(R.string.tv_daily).toString());

        selectIcon(icon_friend,R.mipmap.icon_o_friend_selected,getText(R.string.tv_friend).toString());
        selectIcon(icon_medical,R.mipmap.icon_o_medical_selected,getText(R.string.tv_medical).toString());
        selectIcon(icon_traffic,R.mipmap.icon_o_traffic_selected,getText(R.string.tv_traffic).toString());
        selectIcon(icon_shop,R.mipmap.icon_o_shop_selected,getText(R.string.tv_shop).toString());
        selectIcon(icon_tour,R.mipmap.icon_o_tour_selected,getText(R.string.tv_tour).toString());

        selectIcon(icon_pet,R.mipmap.icon_o_pet_selected,getText(R.string.tv_pet).toString());
        selectIcon(icon_school,R.mipmap.icon_o_school_selected,getText(R.string.tv_school).toString());
        selectIcon(icon_sport,R.mipmap.icon_o_sport_selected,getText(R.string.tv_sport).toString());
        selectIcon(icon_baby,R.mipmap.icon_o_baby_selected,getText(R.string.tv_baby).toString());
        selectIcon(icon_other,R.mipmap.icon_o_other_selected,getText(R.string.tv_other).toString());
    }

    //点击"保存"按钮，保存账单
    private void saveBill(ImageView button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = input_date.getText().toString();  //账单日期
                if("".equals(date) || date.length() == 0){
                    Toast.makeText(v.getContext(),"请填写支出日期",Toast.LENGTH_SHORT).show();
                    return;
                }
                String moneyStr = input_money.getText().toString();
                if("".equals(moneyStr) || moneyStr.length() == 0){
                    Toast.makeText(v.getContext(),"请填写支出金额",Toast.LENGTH_SHORT).show();
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

