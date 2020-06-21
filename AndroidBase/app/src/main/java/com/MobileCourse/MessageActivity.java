package com.MobileCourse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.MobileCourse.Adapter.MsgAdapter;
import com.MobileCourse.Fragments.Fragment1;
import com.MobileCourse.utils.Msg;

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
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private ListView msgListView;
    public static AppCompatActivity msgAct;
    private EditText inputText;
    private Button send;
    private String TargetID;
    private String MsgStatic = "none";
    private TimeThread tt = new TimeThread();
    private MsgAdapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message hmsg){
            super.handleMessage(hmsg);
            switch (hmsg.what){
                case 1:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String jsonData = get_msg_from_DB(TargetID);
                            try {
                                //JSONObject result_json=new JSONObject(jsonData);
                                JSONArray user_list=new JSONArray(jsonData);
                                // 数据分配
                                msgList.clear();
                                for (int i = 0;i<user_list.length();i++){
                                    JSONObject object=user_list.getJSONObject(i);
                                    System.out.println(object);
                                    String content = object.getString("content");
                                    if(object.getString("sender").equals(MainActivity.global_login_id)){
                                        // 发送者为本人
                                        Msg msg = new Msg(content, Msg.TYPE_SEND);
                                        msgList.add(msg);
                                    }
                                    else if(object.getString("sender").equals(TargetID)){
                                        Msg msg = new Msg(content, Msg.TYPE_RECEIVED);
                                        msgList.add(msg);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println(e.toString());
                            }
                        }
                    }).start();
                    //adapter = new MsgAdapter(MessageActivity.this, R.layout.msg_item, msgList);
                    break;
                default:
                    break;
            }
            adapter.notifyDataSetChanged();
        }
    };
    Boolean up = false;
    int count = 0;
    private List<Msg> msgList = new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msgAct = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.msg_layout);
        TargetID = getID();


        initMsgs(TargetID);
        tt.start();
        adapter = new MsgAdapter(this, R.layout.msg_item, msgList);
        msgListView = (ListView)findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        System.out.println("MSG测试");
        for(int i=0;i< msgList.size();i++){
            System.out.println(msgList.get(i).getContent());
        }
        //System.out.println(msgList.get(0).getContent());
        //adapter = new MsgAdapter(this, R.layout.msg_item, msgList);
        inputText = (EditText)findViewById(R.id.input_text);
        send = (Button)findViewById(R.id.send);
//        msgListView = (ListView)findViewById(R.id.msg_list_view);
//        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if(!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());
                    inputText.setText("");
                    sendMessage(TargetID, content);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //this.finish();
        handler.removeCallbacks(tt);
        //super.onDestroy();
        //this.finish();
    }
    private void initMsgs(String TargetID) {
        Thread t = new Thread(){
            @Override
            public void run() {
                System.out.println("URL-TEST");
                String jsonData = get_msg_from_DB(TargetID);
                System.out.println("jsonData");
                System.out.println(jsonData);
                System.out.println("jsonData");
                if(jsonData==null)
                {
                    //Toast.makeText(getApplicationContext(),"网络连接错误！",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        //JSONObject result_json=new JSONObject(jsonData);
                        JSONArray user_list=new JSONArray(jsonData);
                        // 数据分配
                        int num = 10;
                        if(user_list.length()<10){
                            num = user_list.length();
                        }
                        for (int i = 0;i<num;i++){
                            JSONObject object=user_list.getJSONObject(i);
                            System.out.println(object);
                            String content = object.getString("content");
                            if(object.getString("sender").equals(MainActivity.global_login_id)){
                                // 发送者为本人
                                Msg msg = new Msg(content, Msg.TYPE_SEND);
                                msgList.add(msg);
                            }
                            else if(object.getString("sender").equals(TargetID)){
                                Msg msg = new Msg(content, Msg.TYPE_RECEIVED);
                                msgList.add(msg);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println(e.toString());
                        //Toast.makeText(getApplicationContext(),"文件解析错误！",Toast.LENGTH_SHORT).show();
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
//        Msg msg1 = new Msg("111", Msg.TYPE_RECEIVED);
//        msgList.add(msg1);
//        Msg msg2 = new Msg("222", Msg.TYPE_SEND);
//        msgList.add(msg2);
//        Msg msg3 = new Msg("33", Msg.TYPE_RECEIVED);
//        msgList.add(msg3);
    }
    //开一个线程继承Thread
    public class TimeThread extends Thread {
        //重写run方法
        @Override
        public void run() {
            super.run();
            do {
                try {
                    //每隔一秒 发送一次消息
                    Thread.sleep(1000);
                    Message msg = new Message();
                    //消息内容 为MSG_ONE
                    msg.what = 1;
                    //发送
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(msgAct.isFinishing()){
                    break;
                }
            } while (true);
        }
    }
    private String get_msg_from_DB(String TargetID){
        String urlStr = MainActivity.global_url + "/getmsglist";
        System.out.println("URL-GETMSG");
        try {
            JSONObject userJSON = new JSONObject();
            userJSON.put("user_id", MainActivity.global_login_id);
            //userJSON.put("identity",MainActivity.global_login_type);
            userJSON.put("aim_id",TargetID);
            String params = String.valueOf(userJSON);
            URL url=new URL(urlStr);
            HttpURLConnection connect=(HttpURLConnection)url.openConnection();
            connect.setDoInput(true);
            connect.setDoOutput(true);
            connect.setRequestMethod("POST");
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

    private String send_msg_to_DB(String TargetID, String msg){
        String urlStr = MainActivity.global_url + "/send";
        System.out.println("URL-SEND");
        try {
            JSONObject userJSON = new JSONObject();
            userJSON.put("user_id", MainActivity.global_login_id);
            userJSON.put("aim_id",TargetID);
            userJSON.put("content",msg);
            String params = String.valueOf(userJSON);
            URL url=new URL(urlStr);
            HttpURLConnection connect=(HttpURLConnection)url.openConnection();
            connect.setDoInput(true);
            connect.setDoOutput(true);
            connect.setRequestMethod("POST");
            connect.setUseCaches(false);
            connect.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            OutputStream outputStream = connect.getOutputStream();
            outputStream.write(params.getBytes());
            System.out.println("SHOW-PARAM");
            System.out.println(params.getBytes());
            System.out.println(outputStream);
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

    private void sendMessage(String TargetID, String msg){
        Thread t = new Thread(){
            @Override
            public void run() {
                System.out.println("URL-TEST");
                String jsonData = send_msg_to_DB(TargetID, msg);
                System.out.println("jsonData");
                System.out.println(jsonData);
                System.out.println("jsonData");
                MsgStatic = jsonData;
            }
        };
        t.start();
        try {
            t.join();
            Toast.makeText(getApplicationContext(),MsgStatic,Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getID() {
        Intent intent = getIntent();
        String message = intent.getStringExtra(Fragment1.ID_MESSAGE);
        //TextView showMessage = findViewById(R.id.showmessage);
        //showMessage.setText(message);
        System.out.println(message);
        return message;
    }


}