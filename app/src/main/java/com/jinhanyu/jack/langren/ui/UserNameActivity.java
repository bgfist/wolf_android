package com.jinhanyu.jack.langren.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jinhanyu.jack.langren.MainApplication;
import com.jinhanyu.jack.langren.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class UserNameActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText game_number;
    private EditText game_password;
    private EditText game_name;
    private Button next;
    private ImageView showPassword;
    private boolean click;//判断是否显示密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_name);


        game_number = (EditText) findViewById(R.id.game_number);
        game_password = (EditText) findViewById(R.id.game_password);
        game_name = (EditText) findViewById(R.id.game_name);
        next = (Button) findViewById(R.id.next);
        showPassword = (ImageView) findViewById(R.id.showPassword);

        next.setOnClickListener(this);
        showPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next://注册页面的 账号及密码 保存到数据库
                final String username = game_number.getText().toString();
                final String password = game_password.getText().toString();
                final String gamename = game_name.getText().toString();
                final ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.put("mickname",gamename);
                user.put("score",0);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            e.printStackTrace();
                            Toast.makeText(UserNameActivity.this, "注册失败"+" code:"+e.getCode(), Toast.LENGTH_SHORT).show();
                        }else{
                            user.logInInBackground(username, password, new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (e != null) {
                                        e.printStackTrace();
                                        Toast.makeText(UserNameActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                    } else {
                                        MainApplication.userInfo.setUserId(user.getObjectId());
                                        MainApplication.userInfo.setName(username);
                                        MainApplication.userInfo.setScore((Integer) user.get("score"));
                                        MainApplication.user = user;
                                        //把登录信息保存到preferences里
                                        getSharedPreferences(MainApplication.login_preference_name, MODE_APPEND).edit().putString("username", username).putString("password", password).putBoolean("login",true).commit();
                                        Intent intent = new Intent(UserNameActivity.this, UserHeadActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        }
                    }
                });

                break;
            case R.id.showPassword://点击显示密码
                if (click) {
                    game_password.setTransformationMethod(null);//显示密码
                    click = false;
                } else {
                    game_password.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏密码
                    click = true;
                }
                break;
        }
    }
}
