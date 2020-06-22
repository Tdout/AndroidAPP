package com.MobileCourse.FalseWechat;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.MobileCourse.Adapter.ListViewAdapter;
import com.MobileCourse.Adapter.ShowListViewAdapter;
import com.MobileCourse.Fragments.Fragment1;
import com.MobileCourse.MainActivity;
import com.MobileCourse.MessageActivity;
import com.MobileCourse.R;
import com.MobileCourse.utils.CommonInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowMorePage extends Activity implements View.OnClickListener {

    //@BindView(R.id.img_social_circle)ImageView social_circle;
    private TextView name;
    private TextView id;
    private Button subscribe;
    private Button message;
    private int followType = 0;
    private String nameText =  " ";
    private String photoUrl;
    private ImageView image;
    private TextView test;
    private ArrayList<String> Exp;
    private ArrayList<String> Res;
    private ArrayList<String> Skill;

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("----------------R---------------");
        try{
            MessageActivity.msgAct.finish();
        }catch(Exception e){
            System.out.println("----------------R---------------");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_more_page);
        String TargetID = getID();
        photoUrl = MainActivity.global_url + "/static/" + TargetID + "/img.jpg";
        image=(ImageView)findViewById(R.id.userImage);
        name =(TextView) findViewById(R.id.show_name);
        //photoUrl = (ImageView) findViewById(R.id.userImage);
        id =(TextView) findViewById(R.id.show_ID);
        id.setText("ID: " + TargetID);
        subscribe = findViewById(R.id.sub_btn);
        message = findViewById(R.id.to_msg_page_btn);
        if(MainActivity.global_login_type == 1)//1是老师
        {
            subscribe.setVisibility(View.INVISIBLE);
        }
        ButterKnife.bind(this);
        //获取数据
        Glide.with(this).load(photoUrl).
                apply(new RequestOptions().
                        placeholder(R.drawable.login).
                        error(R.drawable.login))
                .into(image);
        Thread t = new Thread(){
            @Override
            public void run() {
                System.out.println("URL-TEST");
                String jsonData = get_Showlist_from_DB(TargetID);
                System.out.println("jsonData");
                System.out.println(jsonData);
                System.out.println("jsonData");
                if(jsonData==null)
                {
                    Toast.makeText(getApplicationContext(),"网络连接错误！",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        JSONObject user_list=new JSONObject(jsonData);
                        //JSONArray Exp_list = user_list.getJSONArray("EXP");
                        JSONArray Res_list = user_list.getJSONArray("keyword");
                        //JSONArray Skill_list = user_list.getJSONArray("skill");
                        //JSONArray user_list=new JSONArray(jsonData);
                        // 数据分配
                        Exp = new ArrayList<>();
                        Skill = new ArrayList<>();
                        Res = new ArrayList<>();
                        // follow
                        followType = user_list.getInt("isfollow");
                        // EXP
                        int num = 0;
                        nameText = user_list.getString("name");
                        String experience = user_list.getString("info");
                        Exp.add(experience);
                        // RES
                        num = Res_list.length();
                        if (num == 0){
                            Res.add("none");
                        }
                        else{
                            for (int i = 0;i<num;i++){
                                String kw = Res_list.getString(i);
                                Res.add(kw);
                            }
                        }
                        String skill = user_list.getString("skill");
                        Skill.add(skill);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println(e.toString());
                        Exp = new ArrayList<>();
                        Skill = new ArrayList<>();
                        Res = new ArrayList<>();
                        Exp.add("none");
                        Res.add("none");
                        Skill.add("none");
                    }
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 设置内容
        if(followType == 1){
            subscribe.setText("已关注");
        }else if(followType == -1){
            subscribe.setText("关 注");
        }else {
            subscribe.setText("ERROR");
            Toast.makeText(getApplicationContext(),"关注按钮值错误！",Toast.LENGTH_SHORT).show();
        }
        name.setText(nameText);
        initView();
        addResListview();
        addSkillListview();
        addExpListview();
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t2 = new Thread(){
                    @Override
                    public void run() {
                        System.out.println("URL-SUB");
                        String jsonData = sub_send_to_DB(TargetID);
                        System.out.println("jsonData");
                        System.out.println(jsonData);
                        System.out.println("jsonData");
                        if(jsonData==null)
                        {
                            Toast.makeText(getApplicationContext(),"网络连接错误！",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            try {
                                JSONObject result=new JSONObject(jsonData);
                                // 数据分配
                                followType = result.getInt("isfollow");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println(e.toString());
                            }
                        }
                    }
                };
                t2.start();
                try {
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(followType == 1){
                    Toast.makeText(getApplicationContext(),"已关注",Toast.LENGTH_SHORT).show();
                    subscribe.setText("已关注");
                }
                else if(followType == -1){
                    Toast.makeText(getApplicationContext(),"取消关注",Toast.LENGTH_SHORT).show();
                    subscribe.setText("关 注");
                }
                else{
                    Toast.makeText(getApplicationContext(),"按钮属性值错误",Toast.LENGTH_SHORT).show();
                    subscribe.setText("ERROR");
                }
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent msgIntent = new Intent();
                //startActivity(new Intent(ShowMorePage.this, MessageActivity.class));
                //System.out.println("获取的ID： " + ListData.get(i).ID);
                Intent intent = new Intent(ShowMorePage.this, MessageActivity.class);
                intent.putExtra(Fragment1.ID_MESSAGE, TargetID);
                startActivity(intent);
            }
        });
    }

    ScrollView scrollView;
    LinearLayout all;
    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        //LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.main, null);
        //layout_topic = (LinearLayout) linearLayout.findViewById(R.id.linearlayout);

        all = (LinearLayout) findViewById(R.id.linearlayout);
        scrollView = (ScrollView) findViewById(R.id.scrollview);


    }
    private void addResListview( ){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText("研究方向");
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(18);
        textView.setTextColor(Color.BLACK);
        all.addView(textView);


        ArrayList<HashMap<String, Object>> arraylist = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        for (int i = 0; i < Res.size(); i ++) {
            map = new HashMap<String, Object>();
            map.put("text", Res.get(i));

            arraylist.add(map);
        }

        ShowListViewAdapter listViewAdapter = new ShowListViewAdapter(this, arraylist);
        ListView listView = new ListView(this);
        //int height = arraylist.size() * (int) getResources().getDimension(R.dimen.listview_item_height);
        int height = arraylist.size() * 100;
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        listView.setDividerHeight(1);

        listView.setAdapter(listViewAdapter);
        all.addView(listView);
    }

    private void addSkillListview( ){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText("掌握技能");
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(18);
        textView.setTextColor(Color.BLACK);
        all.addView(textView);


        ArrayList<HashMap<String, Object>> arraylist = new ArrayList<HashMap<String, Object>>(); // ���ɶ�̬����
        HashMap<String, Object> map;
        for (int i = 0; i < Skill.size(); i ++) {
            map = new HashMap<String, Object>();
            map.put("text", Skill.get(i) );

            arraylist.add(map);
        }

        ShowListViewAdapter listViewAdapter = new ShowListViewAdapter(this, arraylist);
        ListView listView = new ListView(this);
        //int height = arraylist.size() * (int) getResources().getDimension(R.dimen.listview_item_height);
        int height = arraylist.size() * 100;
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setDividerHeight(1);

        listView.setAdapter(listViewAdapter);
        all.addView(listView);
    }

    private void addExpListview( ){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText("研究经历");
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(18);
        textView.setTextColor(Color.BLACK);
        all.addView(textView);


        ArrayList<HashMap<String, Object>> arraylist = new ArrayList<HashMap<String, Object>>(); // ���ɶ�̬����
        HashMap<String, Object> map;
        for (int i = 0; i < Exp.size(); i ++) {
            map = new HashMap<String, Object>();
            map.put("text", Exp.get(i) );

            arraylist.add(map);
        }

        ShowListViewAdapter listViewAdapter = new ShowListViewAdapter(this, arraylist);
        ListView listView = new ListView(this);
        //int height = arraylist.size() * (int) getResources().getDimension(R.dimen.listview_item_height);
        //int height = arraylist.size() * 100;
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setDividerHeight(1);

        listView.setAdapter(listViewAdapter);
        all.addView(listView);
    }


    @Override
    public void onClick(View v) {
        Log.e("id", v.getId() + "");
        Toast.makeText(this, "id" + v.getId()+"",Toast.LENGTH_SHORT).show();
    }
    public String getID() {
        Intent intent = getIntent();
        String message = intent.getStringExtra(Fragment1.ID_MESSAGE);
        //TextView showMessage = findViewById(R.id.showmessage);
        //showMessage.setText(message);
        System.out.println(message);
        return message;
    }

    public static String get_Showlist_from_DB(String target_id){
        //String params = "{\"user_id\":"+ "\"aaaa\"" + ",\"identity\":" + 2 +  "}";// 参数
        String urlStr = MainActivity.global_url + "/details"; // list
        System.out.println("URL-TEST-SHOW");
        try {
            JSONObject userJSON = new JSONObject();
            userJSON.put("identity",MainActivity.global_login_type);
            userJSON.put("user_id",MainActivity.global_login_id);
            userJSON.put("aim_id", target_id);
            String params = String.valueOf(userJSON);
            URL url=new URL(urlStr);
            HttpURLConnection connect=(HttpURLConnection)url.openConnection();
            connect.setDoInput(true);
            connect.setDoOutput(true);
            connect.setRequestMethod("GET");
            connect.setUseCaches(false);
            connect.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            OutputStream outputStream = connect.getOutputStream();
            outputStream.write(params.getBytes());

            int response = connect.getResponseCode();
            if (response== HttpURLConnection.HTTP_OK)
            {
                System.out.println(response);
                InputStream input=connect.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
            else {
                System.out.println(response);
                return "not exsits";
            }
        } catch (Exception e) {
            Log.e("e:", String.valueOf(e));
            return e.toString();
        }
    }

    public static String sub_send_to_DB(String target_id){
        String urlStr = MainActivity.global_url + "/follow"; // list
        System.out.println("URL-TEST-SUB");
        try {
            JSONObject userJSON = new JSONObject();
            userJSON.put("user_id",MainActivity.global_login_id);
            userJSON.put("teacher_id",target_id);
            String params = String.valueOf(userJSON);
            URL url=new URL(urlStr);
            HttpURLConnection connect=(HttpURLConnection)url.openConnection();
            connect.setDoInput(true);
            connect.setDoOutput(true);
            connect.setRequestMethod("GET");
            connect.setUseCaches(false);
            connect.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            OutputStream outputStream = connect.getOutputStream();
            outputStream.write(params.getBytes());

            int response = connect.getResponseCode();
            if (response== HttpURLConnection.HTTP_OK)
            {
                System.out.println(response);
                InputStream input=connect.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
            else {
                System.out.println(response);
                return "not exsits";
            }
        } catch (Exception e) {
            Log.e("e:", String.valueOf(e));
            return e.toString();
        }
    }

}
