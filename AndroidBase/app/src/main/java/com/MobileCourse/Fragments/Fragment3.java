package com.MobileCourse.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.MobileCourse.Adapter.ListViewAdapter;
import com.MobileCourse.FalseWechat.ShowMorePage;
import com.MobileCourse.MainActivity;
import com.MobileCourse.R;
import com.MobileCourse.TemplateActivity1;

import org.json.JSONArray;
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

public class Fragment3 extends Fragment {

    @BindView(R.id.follow_btn)
    Button btn;
    View mView;
    public final class Data{
        public String image;
        public String list_name;
        public String list_major;
        public String list_class;
        public String info;
        public String ID;

    }
    private ListView mListView;
    //private ArrayList<String> ListTitle;
    private ArrayList<Data> ListData;

    private boolean showBtn = true;

    public Fragment3() {
        // Required empty public constructor
    }

    // 控制是否展示对应的按钮
    public Fragment3(boolean showBtn) {
        this.showBtn = showBtn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_3, container, false);
        ButterKnife.bind(this, mView);
        mListView = (ListView)mView.findViewById(R.id.list_follow);
        initJsonData();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initJsonData();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowMorePage.class);
                intent.putExtra(Fragment1.ID_MESSAGE, ListData.get(i).ID);
                startActivity(intent);
            }
        });
        return mView;
    }

    public static String get_follow_list_from_DB(){
        //String params = "{\"user_id\":"+ "\"aaaa\"" + ",\"identity\":" + 2 +  "}";// 参数
        String urlStr = MainActivity.global_url + "/followlist";
        System.out.println("URL-FOLLOW");
        try {
            JSONObject userJSON = new JSONObject();
            userJSON.put("user_id", MainActivity.global_login_id);
            userJSON.put("identity",MainActivity.global_login_type);
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

            System.out.println("SHOW-PARAM-FOLLOW");
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

    public void initJsonData(){
        Thread t = new Thread(){
            @Override
            public void run() {
                System.out.println("URL-TEST");
                String jsonData = get_follow_list_from_DB();
                System.out.println("jsonData");
                System.out.println(jsonData);
                System.out.println("jsonData");
                if(jsonData==null)
                {
                    Toast.makeText(getContext(),"网络连接错误！",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        //JSONObject result_json=new JSONObject(jsonData);
                        JSONArray user_list=new JSONArray(jsonData);
                        // 数据分配
                        ListData = new ArrayList<>();
                        ListData.clear();
                        int num = 10;
                        if(user_list.length()<10){
                            num = user_list.length();
                        }
                        for (int i = 0;i<num;i++){
                            Data e = new Data();
                            JSONObject object=user_list.getJSONObject(i);
                            System.out.println(object);
                            e.image = object.getString("url");
                            e.list_name = object.getString("name");
                            e.list_major = object.getString("major");
                            //e.list_class = object.getString("class");
                            e.ID = object.getString("id");
                            e.info = object.getString("info");
                            ListData.add(e);
                            System.out.println(ListData.get(i).list_name);
                        }
                    } catch (Exception e) {
                        Log.e("e:", String.valueOf(e));
                        System.out.println(e.toString());
                        Toast.makeText(getContext(),"文件解析错误！",Toast.LENGTH_SHORT).show();
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
        if(ListData.size() != 0){
            List<Map<String, Object>> list=getData_fromFollow();
            mListView.setAdapter(new ListViewAdapter(getActivity(), list));
        }
        else {
            ListData.clear();
            List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
            list.clear();
            mListView.setAdapter(new ListViewAdapter(getActivity(), list));
        }
    }

    public List<Map<String, Object>> getData_fromFollow(){
        if(ListData.size() == 0) {
            ListData = new ArrayList<>();
            // 数据分配
        }
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < ListData.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            String path = MainActivity.global_url + "/static/" + ListData.get(i).ID + "/img.jpg";
            //map.put("image", R.drawable.ic_people_nearby); // 图片获取
            map.put("image", path); // 图片获取
            map.put("list_name","Name: " + ListData.get(i).list_name);
            map.put("list_major", "Major: " + ListData.get(i).list_major);
            map.put("list_class", ListData.get(i).list_class);
            if(ListData.get(i).info.length() > 20){
                map.put("info", "info: " + ListData.get(i).info.substring(0,20));
            }
            else
                map.put("info", "Interests: " + ListData.get(i).info);
            list.add(map);
        }
        return list;
    }
}
