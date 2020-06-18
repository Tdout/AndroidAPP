package com.MobileCourse.FalseWechat;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.MobileCourse.Fragments.Fragment1;
import com.MobileCourse.R;
import com.MobileCourse.utils.CommonInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowMorePage extends Activity implements View.OnClickListener {

    //@BindView(R.id.img_social_circle)ImageView social_circle;
    private TextView name;
    private TextView id;

    private TextView test;
    private ArrayList<String> Exp;
    private ArrayList<String> Res;
    private ArrayList<String> Skill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_more_page);
        String TargetID = getID();
        name =(TextView) findViewById(R.id.show_name);
        id =(TextView) findViewById(R.id.show_ID);
        id.setText("ID: " + TargetID);
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        // 网络请求
        for(int i=0; i<3;i++)
        {
            Exp = new ArrayList<>();
            Exp.add("EXP: " + i);
        }
        for(int i=0;i<Exp.size();i++){
            //定义一个临时的hashMap
            HashMap<String,String> hashMap = new HashMap<String, String>();
            hashMap.put("Exp","user_"+ Exp.get(i));
            //讲hashMap存入List
            list.add(hashMap);
        }


        ButterKnife.bind(this);

//        CommonInterface.addViewsListener(this, new int[]{
//                R.id.research_dir,
//                R.id.skill,
//                R.id.exp
//            }, this);
        SimpleAdapter listAdpter = new SimpleAdapter(
                this,
                list,
                R.layout.show_more_list,
                new String[] {"List_INFO"},
                new int[] {R.id.list_info}
        );


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

    public List<Map<String, Object>> getDataExp(){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < Exp.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("info", " " + Exp.get(i));
            list.add(map);
        }
        return list;
    }
    public List<Map<String, Object>> getDataRes(){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < Res.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("info", " " + Res.get(i));
            list.add(map);
        }
        return list;
    }
    public List<Map<String, Object>> getDataSkill(){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < Skill.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("info", " " + Skill.get(i));
            list.add(map);
        }
        return list;
    }
    
}
