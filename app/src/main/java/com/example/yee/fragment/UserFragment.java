package com.example.yee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.yee.MainActivity;
import com.example.yee.R;
import com.example.yee.entity.User;
import com.example.yee.utils.DateUtil;

/**
 * 用户页面
 */
public class UserFragment extends Fragment{

    private User user; //当前登录的用户

    //控件
    private TextView u_username;
    private TextView u_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container, false);
        //获取当前登录的用户
        user = ((MainActivity)getActivity()).user;

        //初始化控件
        u_username = view.findViewById(R.id.u_username);
        u_username.setText(user.getUsername()+"");  //设置用户名
        u_date = view.findViewById(R.id.u_date);
        long day = DateUtil.hasDay(user.getRegisterTime());
        u_date.setText("坚持记账的第"+(day+1)+"天");  //设置天数

        return view;
    }

}
