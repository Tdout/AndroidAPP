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
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class RegisterActivity extends AppCompatActivity {
    private TextView tv_main_title;//标题
    private TextView tv_back;//返回按钮
    private Button btn_register;//注册按钮
    private RadioButton teacher, student;//身份选择按钮
    private int identify_num;//身份数：老师为1，学生为2
    //用户名，密码，再次输入的密码的控件
    private EditText et_user_name,et_psw,et_psw_again,et_username, et_school;
    //用户名，密码，再次输入的密码的控件的获取值
    private String userName,psw,pswAgain,Name,school;
    //标题布局
    private RelativeLayout rl_title_bar;
    private String RegisterAddress = "http://47.99.112.121/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置页面布局 ,注册界面
        setContentView(R.layout.activity_register);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        init();
    }

    private void init() {
        //从main_title_bar.xml 页面布局中获取对应的UI控件
        tv_main_title=findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        tv_back=findViewById(R.id.tv_back);
        //布局根元素
        rl_title_bar=findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);
        //从activity_register.xml 页面中获取对应的UI控件
        btn_register=findViewById(R.id.btn_register);
        et_user_name=findViewById(R.id.et_user_name);
        et_username = findViewById(R.id.et_username);
        et_school = findViewById(R.id.et_school);
        et_psw=findViewById(R.id.et_psw);
        et_psw_again=findViewById(R.id.et_psw_again);
        teacher = findViewById(R.id.teacher);
        student = findViewById(R.id.student);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回键
                RegisterActivity.this.finish();
            }
        });
        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入在相应控件中的字符串
                userName=et_user_name.getText().toString().trim();
                psw=et_psw.getText().toString().trim();
                pswAgain=et_psw_again.getText().toString().trim();
                Name = et_username.getText().toString().trim();
                school = et_school.getText().toString().trim();

                //判断输入框内容
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(pswAgain)){
                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(Name)) {
                    Toast.makeText(RegisterActivity.this, "请输入真实姓名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(school)) {
                    Toast.makeText(RegisterActivity.this, "请输入学校", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!psw.equals(pswAgain)){
                    Toast.makeText(RegisterActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                    /**
                     *从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
                     */

                }else if(!teacher.isChecked() && !student.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "请选择身份", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //把账号、密码和账号标识保存到sp里面
                    /**
                     * 保存账号和密码到SharedPreferences中
                     */
                    saveRegisterInfo(userName, psw);
                    //注册成功后把账号传递到LoginActivity.java中
                    // 返回值到loginActivity显示
                    Intent data = new Intent();
                    data.putExtra("userName", userName);
                    setResult(RESULT_OK, data);
                    //RESULT_OK为Activity系统常量，状态码为-1，
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    RegisterActivity.this.finish();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader reader = null;
                            JSONObject userJSON = new JSONObject();
                            userJSON.put("user_id", userName);
                            userJSON.put("name", Name);
                            userJSON.put("password", psw);
                            userJSON.put("school", school);
                            if(teacher.isChecked()) {
                                userJSON.put("identity",1);
                            }else if(student.isChecked()) {
                                userJSON.put("identity",2);
                            }
                            String content = String.valueOf(userJSON);
                            HttpURLConnection connection = (HttpURLConnection) new URL(RegisterAddress).openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);
                            connection.setDoInput(true);
                            connection.setRequestProperty("Connection", "Keep-Alive");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setRequestProperty("Charset", "UTF-8");
                            connection.setRequestProperty("accept","application/json");
                            if(content != null && !TextUtils.isEmpty(content)) {
                                byte[] writebytes = content.getBytes();
                                connection.setRequestProperty("Content-Length",String.valueOf(writebytes.length));
                                OutputStream os = connection.getOutputStream();
                                os.write(content.getBytes());
                                os.flush();
                                os.close();
                            }
                            if(connection.getResponseCode() == 200) {
                                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                String result = reader.readLine();
                                System.out.println(result);
                            }
                        }catch (Exception e){
                            System.out.println("failed");
                        }
                    }
                }).start();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * 从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
     */
    private boolean isExistUserName(String userName){
        boolean has_userName=false;
        //mode_private SharedPreferences sp = getSharedPreferences( );
        // "loginInfo", MODE_PRIVATE
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取密码
        String spPsw=sp.getString(userName, "");//传入用户名获取密码
        //如果密码不为空则确实保存过这个用户名
        if(!TextUtils.isEmpty(spPsw)) {
            has_userName=true;
        }
        return has_userName;
    }
    /**
     * 保存账号和密码到SharedPreferences中SharedPreferences
     */
    private void saveRegisterInfo(String userName,String psw){
        String md5Psw = MD5Utils.md5(psw);//把密码用MD5加密
        //loginInfo表示文件名, mode_private SharedPreferences sp = getSharedPreferences( );
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器， SharedPreferences.Editor  editor -> sp.edit();
        SharedPreferences.Editor editor=sp.edit();
        //以用户名为key，密码为value保存在SharedPreferences中
        //key,value,如键值对，editor.putString(用户名，密码）;
        editor.putString(userName, md5Psw);
        //提交修改 editor.commit();
        editor.commit();
    }
}

