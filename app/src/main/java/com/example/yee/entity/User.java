package com.example.yee.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户实体类
 */
//实现序列化接口，在activity间传递对象
public class User implements Parcelable {

    private int id;  //用户id
    private String username;  //用户名
    private String password;  //密码
    private String email;  //邮箱
    private String registerTime;  //注册时间

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(int id, String username, String password, String email, String registerTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.registerTime = registerTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public static final Parcelable.Creator<User> CREATOR=new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            User user = new User();
            user.id = in.readInt();
            user.username = in.readString();
            user.password = in.readString();
            user.email = in.readString();
            user.registerTime = in.readString();
            return user;
        }

        @Override
        public User[] newArray(int size) {
            // TODO Auto-generated method stub
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(registerTime);
    }
}
