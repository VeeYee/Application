package com.example.yee;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yee.entity.User;
import com.example.yee.fragment.UserFragment;
import com.example.yee.fragment.ManageFragment;
import com.example.yee.fragment.BillFragment;
import com.example.yee.fragment.AddFragment;
import com.example.yee.fragment.ChartFragment;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    // 底部菜单5个LinearLayout
    private LinearLayout ll_tab_one;
    private LinearLayout ll_tab_two;
    private LinearLayout ll_tab_three;
    private LinearLayout ll_tab_four;
    private LinearLayout ll_tab_five;

    // 底部菜单5个ImageView
    private ImageView ivTabOne;
    private ImageView ivTabTwo;
    private ImageView ivTabThree;
    private ImageView ivTabFour;
    private ImageView ivTabFive;

    // 底部菜单5个菜单标题
    private TextView tvTabOne;
    private TextView tvTabTwo;
    private TextView tvTabThree;
    private TextView tvTabFour;
    private TextView tvTabFive;

    // 5个Fragment
    private Fragment billFragment;
    private Fragment chartFragment;
    private Fragment addFragment;
    private Fragment manageFragment;
    private Fragment userFragment;

    //当前登录的用户
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取当前登录的用户
        user = (User)getIntent().getExtras().get("user");
        // 初始化控件
        initView();
        // 初始化底部按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        initFragment(0);
        //状态栏颜色
        setThisStatusBarColor();
    }

    //设置状态栏渐变颜色
    private void setThisStatusBarColor() {
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //利用反射机制修改状态栏背景
                int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
                View statusBarView = getWindow().findViewById(identifier);
                statusBarView.setBackgroundResource(R.drawable.status_bar_gradient_bg);
                getWindow().getDecorView().removeOnLayoutChangeListener(this);
            }
        });
    }

    //1.初始化控件
    private void initView() {
        // 底部菜单5个LinearLayout
        this.ll_tab_one = (LinearLayout) findViewById(R.id.ll_tab_one);
        this.ll_tab_two = (LinearLayout) findViewById(R.id.ll_tab_two);
        this.ll_tab_three = (LinearLayout) findViewById(R.id.ll_tab_three);
        this.ll_tab_four = (LinearLayout) findViewById(R.id.ll_tab_four);
        this.ll_tab_five = (LinearLayout) findViewById(R.id.ll_tab_five);

        // 底部菜单5个ImageView
        this.ivTabOne = (ImageView) findViewById(R.id.iv_tab_one);
        this.ivTabTwo = (ImageView) findViewById(R.id.iv_tab_two);
        this.ivTabThree = (ImageView) findViewById(R.id.iv_tab_three);
        this.ivTabFour = (ImageView) findViewById(R.id.iv_tab_four);
        this.ivTabFive = (ImageView) findViewById(R.id.iv_tab_five);

        // 底部菜单5个菜单标题
        this.tvTabOne = (TextView) findViewById(R.id.tv_tab_one);
        this.tvTabTwo = (TextView) findViewById(R.id.tv_tab_two);
        this.tvTabThree = (TextView) findViewById(R.id.tv_tab_three);
        this.tvTabFour = (TextView) findViewById(R.id.tv_tab_four);
        this.tvTabFive = (TextView) findViewById(R.id.tv_tab_five);
    }

    //2.设置控件的初始样式
    private void restartMenu() {
        // ImageView设置为未选中的样式
        ivTabOne.setImageResource(R.mipmap.ic_tab_home_normal);
        ivTabTwo.setImageResource(R.mipmap.ic_tab_chart_normal);
        ivTabThree.setImageResource(R.mipmap.ic_tab_add_normal);
        ivTabFour.setImageResource(R.mipmap.ic_tab_wealth_normal);
        ivTabFive.setImageResource(R.mipmap.ic_tab_person_normal);
        // TextView置为白色
        tvTabOne.setTextColor(getResources().getColor(R.color.col_gray));
        tvTabTwo.setTextColor(getResources().getColor(R.color.col_gray));
        tvTabThree.setTextColor(getResources().getColor(R.color.col_gray));
        tvTabFour.setTextColor(getResources().getColor(R.color.col_gray));
        tvTabFive.setTextColor(getResources().getColor(R.color.col_gray));
    }

    //3.初始化底部菜单事件  绑定点击事件
    private void initEvent() {
        // 设置按钮监听
        ll_tab_one.setOnClickListener(this);
        ll_tab_two.setOnClickListener(this);
        ll_tab_three.setOnClickListener(this);
        ll_tab_four.setOnClickListener(this);
        ll_tab_five.setOnClickListener(this);
    }

    //4.底部菜单的点击事件
    @Override
    public void onClick(View v) {
        // 每次点击后，重置菜单的样式
        restartMenu();
        // 修改ImageView和TextView的样式，并跳转页面
        switch (v.getId()) {
            case R.id.ll_tab_one:
                ivTabOne.setImageResource(R.mipmap.ic_tab_home_selected);
                tvTabOne.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                initFragment(0);
                break;
            case R.id.ll_tab_two:
                ivTabTwo.setImageResource(R.mipmap.ic_tab_chart_selected);
                tvTabTwo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                initFragment(1);
                break;
            case R.id.ll_tab_three:
                tvTabThree.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                initFragment(2);
                break;
            case R.id.ll_tab_four:
                ivTabFour.setImageResource(R.mipmap.ic_tab_wealth_selected);
                tvTabFour.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                initFragment(3);
                break;
            case R.id.ll_tab_five:
                ivTabFive.setImageResource(R.mipmap.ic_tab_person_selected);
                tvTabFive.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                initFragment(4);
                break;

            default:
                break;
        }
    }

    //5.切换当前显示的fragment
    private void initFragment(int index) {
        // 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (billFragment == null) {
                    //第一次创建
                    billFragment = new BillFragment();
                    transaction.add(R.id.fl_content, billFragment);
                } else {
                    //直接显示fragment
                    transaction.show(billFragment);
                }
                break;
            case 1:
                if (chartFragment == null) {
                    chartFragment = new ChartFragment();
                    transaction.add(R.id.fl_content, chartFragment);
                } else {
                    transaction.show(chartFragment);
                }

                break;
            case 2:
                if (addFragment == null) {
                    addFragment = new AddFragment();
                    transaction.add(R.id.fl_content, addFragment);
                } else {
                    transaction.show(addFragment);
                }

                break;
            case 3:
                if (manageFragment == null) {
                    manageFragment = new ManageFragment();
                    transaction.add(R.id.fl_content, manageFragment);
                } else {
                    transaction.show(manageFragment);
                }

                break;
            case 4:
                if (userFragment == null) {
                    userFragment = new UserFragment();
                    transaction.add(R.id.fl_content, userFragment);
                } else {
                    transaction.show(userFragment);
                }

                break;
            default:
                break;
        }
        // 提交事务
        transaction.commit();
    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (billFragment != null) {
            transaction.hide(billFragment);
        }
        if (chartFragment != null) {
            transaction.hide(chartFragment);
        }
        if (addFragment != null) {
            transaction.hide(addFragment);
        }
        if (manageFragment != null) {
            transaction.hide(manageFragment);
        }
        if (userFragment != null) {
            transaction.hide(userFragment);
        }
    }
}
