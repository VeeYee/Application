package com.example.yee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yee.R;

/**
 * 添加页面
 */
public class AddFragment extends Fragment{

    //两个按钮
    private Button outlay;  //支出按钮
    private Button income; //收入按钮

    //两个fragment页面
    private Fragment addOutlayFragment;  //支出页面
    private Fragment addIncomeFragment; //收入页面

    //页面标题
    private TextView bill_title;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add,container, false);

        //初始化控件
        outlay = view.findViewById(R.id.outlay);
        income = view.findViewById(R.id.income_style);
        bill_title = view.findViewById(R.id.bill_title);

        //默认显示支出页面
        showFragment(0);

        //给两个按钮绑定点击事件
        outlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(0);  //显示支出页面
                //先恢复默认样式，再设置选中的按钮样式
                resetBtn();
                outlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.outlay_button_selected));
                outlay.setTextColor(getResources().getColor(R.color.btn_selected));
                bill_title.setText("花一笔");
            }
        });
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(1);  //显示收入页面
                //先恢复默认样式，再设置选中的按钮样式
                resetBtn();
                income.setBackgroundDrawable(getResources().getDrawable(R.drawable.income_button_selected));
                income.setTextColor(getResources().getColor(R.color.btn_selected));
                bill_title.setText("攒一笔");
            }
        });
        return view;
    }

    //按钮恢复未选中样式
    public void resetBtn(){
        //支出按钮
        outlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.outlay_button_normal));
        outlay.setTextColor(getResources().getColor(R.color.btn_normal));
        //收入按钮
        income.setBackgroundDrawable(getResources().getDrawable(R.drawable.income_button_normal));
        income.setTextColor(getResources().getColor(R.color.btn_normal));
    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (addOutlayFragment != null) {
            transaction.hide(addOutlayFragment);
        }
        if (addIncomeFragment != null) {
            transaction.hide(addIncomeFragment);
        }
    }

    //切换为当前显示的页面
    private void showFragment(int index){
        //事务管理器 在fragment中使用getChildFragmentManager()
        FragmentManager fragmentManager = getChildFragmentManager();
        //开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (addOutlayFragment == null) {
                    addOutlayFragment = new AddOutlayFragment();
                    transaction.add(R.id.fl_type, addOutlayFragment);
                } else {
                    transaction.show(addOutlayFragment);
                }
                break;
            case 1:
                if (addIncomeFragment == null) {
                    addIncomeFragment = new AddIncomeFragment();
                    transaction.add(R.id.fl_type, addIncomeFragment);
                } else {
                    transaction.show(addIncomeFragment);
                }
                break;
            default:
                break;
        }
        //提交事务
        transaction.commit();
    }

}

