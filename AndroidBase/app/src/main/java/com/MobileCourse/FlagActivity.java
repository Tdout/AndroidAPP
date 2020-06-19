package com.MobileCourse;

import android.app.Activity;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Color;
import com.MobileCourse.Adapter.FlowLayout;
import com.MobileCourse.R;
import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlagActivity extends AppCompatActivity implements View.OnClickListener{
    FlowLayout flowLayout;
    private EditText flagedit;
    private  String flagtext;
    private String SetKeywordAddress = MainActivity.global_url+"/setKeyword";
    private String KeywordAddress = MainActivity.global_url + "/kwlist";
    private List<String> texts = new ArrayList<>();
    //private String[] texts = new String[] {};
    private int optionType;
    int length=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag);
        flowLayout = (FlowLayout) findViewById(R.id.flowlayout);
        flagedit = findViewById(R.id.flag_edit);
        findViewById(R.id.btn_add_random).setOnClickListener(this);
        findViewById(R.id.btn_relayout1).setOnClickListener(this);
        findViewById(R.id.btn_remove_all).setOnClickListener(this);
        Thread A = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = null;
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("user_id", MainActivity.global_login_id);
                    userJSON.put("identity", MainActivity.global_login_type);

                    String content = String.valueOf(userJSON);
                    HttpURLConnection connection = (HttpURLConnection) new URL(KeywordAddress).openConnection();
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
                        JSONArray jArray = new JSONArray(result);
                        List<String> list = new ArrayList<>();
                        for(int i=0;i<jArray.length();i++){
                            //jArray.optString(i);//等价于getXXX
                            list.add(jArray.getString(i));
                            texts.add(list.get(i));
                            //System.out.println(texts.get(i));
                            //System.out.println(list.get(i));
                        }
                    }

                }catch (Exception e){
                    System.out.println("failed");
                }
            }
        });
        A.start();
        try {
            A.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(texts.size());
        length = texts.size();
        for(int i=0; i<texts.size(); i++)
        {
            int ranHeight = dip2px(this, 30);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ranHeight);
            lp.setMargins(dip2px(this, 10), 0, dip2px(this, 10), 0);
            TextView tv = new TextView(this);
            tv.setPadding(dip2px(this, 15), 0, dip2px(this, 15), 0);
            tv.setTextColor(Color.parseColor("#FF3030"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            //int index = (int)(Math.random() * length);
            tv.setText(texts.get(i));
            System.out.println(texts.get(i));
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLines(1);
            tv.setBackgroundResource(R.drawable.bg_tag);
            flowLayout.addView(tv, lp);
        }
        //findViewById(R.id.btn_relayout2).setOnClickListener(this);
        //findViewById(R.id.btn_specify_line).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        flagtext = flagedit.getText().toString();
        switch (v.getId()) {
            case R.id.btn_add_random:
                if(!TextUtils.isEmpty(flagtext)) {
                    optionType = 1;
                    int ranHeight = dip2px(this, 30);
                    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ranHeight);
                    lp.setMargins(dip2px(this, 10), 0, dip2px(this, 10), 0);
                    TextView tv = new TextView(this);
                    tv.setPadding(dip2px(this, 15), 0, dip2px(this, 15), 0);
                    tv.setTextColor(Color.parseColor("#FF3030"));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    //int index = (int)(Math.random() * length);
                    tv.setText(flagtext);
                    tv.setGravity(Gravity.CENTER_VERTICAL);
                    tv.setLines(1);
                    tv.setBackgroundResource(R.drawable.bg_tag);
                    length=length + 1;
                    texts.add(flagtext);
                    System.out.println(texts);
                    flowLayout.addView(tv, lp);
                    flagedit.setText("");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                BufferedReader reader = null;
                                JSONObject userJSON = new JSONObject();
                                userJSON.put("user_id", MainActivity.global_login_id);
                                userJSON.put("identity", MainActivity.global_login_type);
                                userJSON.put("keyword", flagtext);
                                userJSON.put("type", optionType);

                                String content = String.valueOf(userJSON);
                                HttpURLConnection connection = (HttpURLConnection) new URL(SetKeywordAddress).openConnection();
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
                }
                break;
            case R.id.btn_remove_all:
                flowLayout.removeAllViews();
                optionType = 3;
                length = 0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader reader = null;
                            JSONObject userJSON = new JSONObject();
                            userJSON.put("user_id", MainActivity.global_login_id);
                            userJSON.put("identity", MainActivity.global_login_type);
                            //userJSON.put("keyword", flagtext);
                            userJSON.put("type", optionType);

                            String content = String.valueOf(userJSON);
                            HttpURLConnection connection = (HttpURLConnection) new URL(SetKeywordAddress).openConnection();
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
                break;
            case R.id.btn_relayout1:
                if(length !=0 ) {
                    optionType = 2;

                    flowLayout.removeViewAt(length - 1);
                    length--;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                BufferedReader reader = null;
                                JSONObject userJSON = new JSONObject();
                                userJSON.put("user_id", MainActivity.global_login_id);
                                userJSON.put("identity", MainActivity.global_login_type);
                                userJSON.put("type", optionType);
                                userJSON.put("keyword",texts.get(texts.size()-1));

                                String content = String.valueOf(userJSON);
                                HttpURLConnection connection = (HttpURLConnection) new URL(SetKeywordAddress).openConnection();
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
                }
                break;
//            case R.id.btn_relayout2:
//                flowLayout.relayoutToAlign();
//                break;
//            case R.id.btn_specify_line:
//                flowLayout.specifyLines(3);
//                break;
            default:
                break;
        }
    }
//    public static String unicodeStr2String(String unicodeStr) {
//        int length = unicodeStr.length();
//        int count = 0;
//        //正则匹配条件，可匹配“\\u”1到4位，一般是4位可直接使用 String regex = "\\\\u[a-f0-9A-F]{4}";
//        String regex = "\\\\u[a-f0-9A-F]{1,4}";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(unicodeStr);
//        StringBuffer sb = new StringBuffer();
//
//        while(matcher.find()) {
//            String oldChar = matcher.group();//原本的Unicode字符
//            String newChar = unicode2String(oldChar);//转换为普通字符
//            // int index = unicodeStr.indexOf(oldChar);
//            // 在遇见重复出现的unicode代码的时候会造成从源字符串获取非unicode编码字符的时候截取索引越界等
//            int index = matcher.start();
//
//            sb.append(unicodeStr.substring(count, index));//添加前面不是unicode的字符
//            sb.append(newChar);//添加转换后的字符
//            count = index+oldChar.length();//统计下标移动的位置
//        }
//        sb.append(unicodeStr.substring(count, length));//添加末尾不是Unicode的字符
//        return sb.toString();
//    }
//
//    public static String unicode2String(String unicode) {
//        StringBuffer string = new StringBuffer();
//        String[] hex = unicode.split("\\\\u");
//
//        for (int i = 1; i < hex.length; i++) {
//            // 转换出每一个代码点
//            int data = Integer.parseInt(hex[i], 16);
//            // 追加成string
//            string.append((char) data);
//        }
//
//        return string.toString();
//    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
