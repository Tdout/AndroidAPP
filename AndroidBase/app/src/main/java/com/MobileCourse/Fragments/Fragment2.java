package com.MobileCourse.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.MobileCourse.Adapter.ListViewAdapter;
import com.MobileCourse.FalseWechat.ShowMorePage;
import com.MobileCourse.MainActivity;
import com.MobileCourse.R;

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

public class Fragment2 extends Fragment {

    @BindView(R.id.search_btn)
    Button btn;
    View mView;
    private TextView searchText;
    private RadioButton searchById;
    private RadioButton searchByName;
    private RadioButton searchByKw;
    private boolean showBtn = true;
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

    public Fragment2() {
        // Required empty public constructor
    }

    // 控制是否展示对应的按钮
    public Fragment2(boolean showBtn) {
        this.showBtn = showBtn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_2, container, false);
        ButterKnife.bind(this, mView);
        searchText = mView.findViewById(R.id.search_Edit);
        searchById = mView.findViewById(R.id.search_id);
        searchByKw = mView.findViewById(R.id.search_kw);
        searchByName = mView.findViewById(R.id.search_name);
        mListView = (ListView)mView.findViewById(R.id.list_search);
        btn.setOnClickListener(view -> {
            //Intent intent = new Intent(getActivity(), ShowMorePage.class);
            //startActivity(intent);
            String kw = searchText.getText().toString();
            System.out.println("-------------kw------------");
            System.out.println(kw);
            int search_type = -1;
            if(TextUtils.isEmpty(kw)){
                Toast.makeText(getContext(), "请输入搜索内容", Toast.LENGTH_SHORT).show();
                return;
            } else if(!searchByKw.isChecked() && !searchById.isChecked() && !searchByName.isChecked()){
                Toast.makeText(getContext(), "请选择搜索类型", Toast.LENGTH_SHORT).show();
                return;
            }
            if (searchByName.isChecked()) {
                search_type = 2;
            } else if (searchById.isChecked()) {
                search_type = 3;
            } else if (searchByKw.isChecked()) {
                search_type = 1;
            }
            //获取数据
            int finalSearch_type = search_type;
            Thread t = new Thread(){
                @Override
                public void run() {
                    System.out.println("URL-TEST");
                    String jsonData = get_search_list_from_DB(kw, finalSearch_type);
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
                List<Map<String, Object>> list=getData_fromSearch();
                mListView.setAdapter(new ListViewAdapter(getActivity(), list));
            }
            else {
                ListData.clear();
                List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
                list.clear();
                mListView.setAdapter(new ListViewAdapter(getActivity(), list));
                Toast.makeText(getContext(),"无搜索结果！",Toast.LENGTH_SHORT).show();
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
        if (!showBtn)
            btn.setVisibility(View.INVISIBLE);
        return mView;
    }

    public static String get_search_list_from_DB(String kw, int type){
        //String params = "{\"user_id\":"+ "\"aaaa\"" + ",\"identity\":" + 2 +  "}";// 参数
        String urlStr = MainActivity.global_url + "/search";
        System.out.println("URL-SEARCH");
        try {
            JSONObject userJSON = new JSONObject();
            userJSON.put("keyword", kw);
            userJSON.put("identity",MainActivity.global_login_type);
            userJSON.put("type", type);
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

            System.out.println("SHOW-PARAM-SEARCH");
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

    public List<Map<String, Object>> getData_fromSearch(){
        if(ListData.size() == 0) {
            ListData = new ArrayList<>();
            // 数据分配
        }
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < ListData.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("image", R.drawable.ic_people_nearby); // 图片获取
            //map.put("image", ListData.get(i).image); // 图片获取
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
