package com.example.yee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yee.dao.UserDao;
import com.example.yee.entity.User;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setThisStatusBarColor(); //状态栏渐变颜色
    }

    //数据库操作类
    private UserDao userDao = new UserDao(this);

    //点击“登录”按钮
    public void login(View view){
        User user = null; //登录用户
        //获取账号和密码
        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        if("".equals(username) || username.length() == 0 ){
            Toast.makeText(this,"请填写用户名",Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(password) || password.length() == 0){
            Toast.makeText(this,"请填写密码",Toast.LENGTH_SHORT).show();
            return;
        }
        //判断账号和密码能否登录
        user = userDao.loginByName(username,password);
        if(user == null) {
            //再判断邮箱和密码能否登录
            user = userDao.loginByEmail(username, password);
            if (user == null) {
                //两者都不能登录
                Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //登录成功 进入主页面
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("user",user); //传递当前登录的用户对象
        startActivity(intent);
    }

    //点击“没有账号？立即注册”按钮
    public void toRegister(View view){
        //跳转到注册页面
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}
