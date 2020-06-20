package com.MobileCourse;

import android.content.Intent;
import com.MobileCourse.utils.MD5Utils;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChangePswActivity extends AppCompatActivity {
    private Button change_psw;
    private EditText changepsw_username, changepaw_old, changepsw_new, changepsw_new_again;
    private String user_name, oldpsw, newpsw, newpsw_again;
    private String result = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置页面布局 ,注册界面
        setContentView(R.layout.activity_changepassword);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        init();
    }
    private  void init() {
        change_psw = findViewById(R.id.btn_changepsw);
        changepsw_username = findViewById(R.id.changepsw_user_name);
        changepaw_old = findViewById(R.id.changepsw_old);
        changepsw_new = findViewById(R.id.changepsw_new);
        changepsw_new_again = findViewById(R.id.changepsw_new_again);
        change_psw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                user_name = changepsw_username.getText().toString().trim();
                oldpsw = changepaw_old.getText().toString().trim();
                newpsw = changepsw_new.getText().toString().trim();
                newpsw_again = changepsw_new_again.getText().toString().trim();
                if (TextUtils.isEmpty(user_name)) {
                    Toast.makeText(ChangePswActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(oldpsw)) {
                    Toast.makeText(ChangePswActivity.this, "请输入原来的密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(newpsw)) {
                    Toast.makeText(ChangePswActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(newpsw_again)) {
                    Toast.makeText(ChangePswActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!newpsw.equals(newpsw_again)) {
                    Toast.makeText(ChangePswActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                }
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader reader = null;
                            JSONObject userJSON = new JSONObject();
                            userJSON.put("user_id", MainActivity.global_login_id);
                            userJSON.put("identity", MainActivity.global_login_type);
                            userJSON.put("old_password", oldpsw);
                            userJSON.put("new_password", newpsw);

                            String content = String.valueOf(userJSON);
                            HttpURLConnection connection = (HttpURLConnection) new URL(MainActivity.global_url+"/changepassword").openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);
                            connection.setDoInput(true);
                            connection.setRequestProperty("Connection", "Keep-Alive");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setRequestProperty("Charset", "UTF-8");
                            connection.setRequestProperty("accept", "application/json");
                            if (content != null && !TextUtils.isEmpty(content)) {
                                byte[] writebytes = content.getBytes();
                                connection.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                                OutputStream os = connection.getOutputStream();
                                os.write(content.getBytes());
                                os.flush();
                                os.close();
                            }
                            if (connection.getResponseCode() == 200) {
                                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                result = reader.readLine();
                                System.out.println(result);
                            }
                        } catch (Exception e) {
                            System.out.println("failed");
                        }
                    }
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Integer.valueOf(result) == 1) {
                    Toast.makeText(ChangePswActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();

                    //销毁登录界面
                    ChangePswActivity.this.finish();
                    //跳转到主界面，登录成功的状态传递到 MainActivity 中
                    startActivity(new Intent(ChangePswActivity.this, LoginActivity.class));
                    return;
                }
                else {
                    Toast.makeText(ChangePswActivity.this, "账号或原密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
