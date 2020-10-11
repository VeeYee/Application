package com.example.yee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yee.dao.UserDao;
import com.example.yee.entity.User;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setThisStatusBarColor();  //设置状态栏颜色
    }

    private UserDao userDao = new UserDao(this);

    //点击“立即注册”按钮
    public void register(View view){
        //获取数据
        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        if("".equals(username) || username.length() == 0){
            Toast.makeText(this,"请填写用户名",Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(password) || password.length() == 0){
            Toast.makeText(this,"请填写密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(email) || email.length() == 0){
            Toast.makeText(this,"请填写电子邮箱",Toast.LENGTH_SHORT).show();
            return;
        }
        //封装属性
        User user = new User(username,password,email);
        long id = userDao.addUser(user);
        if(id == -1){
            Toast.makeText(this,"注册失败，该用户名已存在！",Toast.LENGTH_SHORT).show();
        }else if(id == -2){
            Toast.makeText(this,"注册失败，该邮箱已绑定其他账号！",Toast.LENGTH_SHORT).show();
        }else if(id > 0){
            Toast.makeText(this,"注册成功！",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this,"注册失败，请重试！",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击“返回”按钮
     * @param view
     */
    public void back(View view){
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
