package com.MobileCourse.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.MobileCourse.Adapter.ListViewAdapter;
import com.MobileCourse.FalseWechat.ShowMorePage;
import com.MobileCourse.MainActivity;
import com.MobileCourse.R;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

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


public class Fragment1 extends Fragment {

    View mView;
    //轮播页
    private Banner mBanner;
    private LocalImageLoader mImageLoader;
    private ArrayList<String> imagePath;
    private ArrayList<String> imageTitle;
    public static final String ID_MESSAGE
            = "com.example.android.showmore.extra.MESSAGE";
    // 列表
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
    //private List<Map<String, Object>> list;
    //private Button mButton;

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_1, container, false);
        // 绑定视图
        //轮播页功能
        initData();
        initView();
        //轮播页结束
        //列表
        ButterKnife.bind(this, mView);
        return mView;
    }

    public void initData(){
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        //imagePath.add("http://e.hiphotos.baidu.com/image/pic/item/a1ec08fa513d2697e542494057fbb2fb4316d81e.jpg");
        //imagePath.add("http://c.hiphotos.baidu.com/image/pic/item/30adcbef76094b36de8a2fe5a1cc7cd98d109d99.jpg");
        imagePath.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592752110327&di=a8c76ca025551f650b6d1210f04c9f91&imgtype=0&src=http%3A%2F%2Fm.tuniucdn.com%2Ffilebroker%2Fcdn%2Fvnd%2Fde%2F3c%2Fde3c52582a605da681a810fec1c86f46_w500_h280_c1_t0.jpg");
        imagePath.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592752297191&di=51b0eb5abb2586caa36e4d1e3d6cf2dc&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20171017%2Fb49231b96ba649388aca20187b947dd2.jpeg");
        imageTitle.add("图片1");
        imageTitle.add("图片2");
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取数据
        Thread t = new Thread(){
            @Override
            public void run() {
                System.out.println("URL-TEST");
                String jsonData = get_list_from_DB();
                System.out.println("jsonData");
                System.out.println(jsonData);
                System.out.println("jsonData");
                if(jsonData==null)
                {
                    Looper.prepare();
                    Toast.makeText(getContext(),"网络连接错误！",Toast.LENGTH_SHORT).show();
                    Looper.loop();
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println(e.toString());
                        Looper.prepare();
                        Toast.makeText(getContext(),"文件解析错误！",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            }
        };
        t.start();


        try {
            t.join();
            System.out.println(ListData.get(0).list_name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mListView = (ListView)mView.findViewById(R.id.list);
        List<Map<String, Object>> list=getData();
        mListView.setAdapter(new ListViewAdapter(getActivity(), list));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "点击时间-SHOWMORE: " + ListData.get(i).ID, Toast.LENGTH_SHORT).show();
                System.out.println("获取的ID： " + ListData.get(i).ID);
                Intent intent = new Intent(getActivity(), ShowMorePage.class);
                intent.putExtra(ID_MESSAGE, ListData.get(i).ID);
                startActivity(intent);
            }
        });
    }

    public void initView() {
        mImageLoader = new LocalImageLoader();
        mBanner = (Banner)mView.findViewById(R.id.banner);
        //mBanner.findViewById(R.id.banner);
        //样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //加载器
        mBanner.setImageLoader(mImageLoader);
        //动画效果
        mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        //图片标题
        mBanner.setBannerTitles(imageTitle);
        //间隔时间
        mBanner.setDelayTime(4500);
        //自动轮播
        mBanner.isAutoPlay(true);
        //小点位置
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //图片地址
        mBanner.setImages(imagePath);
        //启动
        mBanner.start();
        //监听
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播页点击事件--跳转页面
                Toast.makeText(getActivity(), "点击事件-轮播页" + (position+1), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Fragment1.this, "sa"+(position+1), Toast.LENGTH_LONG);
            }
        });
    }
    public static class LocalImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }
    // list 数据获取
    // 向数据库请求数据


    public static String get_list_from_DB(){
        //String params = "{\"user_id\":"+ "\"aaaa\"" + ",\"identity\":" + 2 +  "}";// 参数
        String urlStr = MainActivity.global_url + "/recommend";
        System.out.println("URL-TEST");
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


    public List<Map<String, Object>> getData(){
//        ListData = new ArrayList<>();
//        // 数据分配
//        for (int i = 0;i<10;i++){
//            Data e = new Data();
//            e.list_name = "Name"+i;
//            e.list_major = "Major";
//            e.list_class = "Class";
//            e.ID = "ID_NUM";
//            e.info = "INFO";
//            ListData.add(e);
//        }
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < ListData.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            //map.put("image", R.drawable.ic_people_nearby); // 图片获取
//            final Bitmap[] image = new Bitmap[1];
//            Thread t0 = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try{
//                        image[0] = getImage(url);
//                    }catch (Exception e){
//                        image[0] = getImage("default");
//                    }
//                }
//            });
//            t0.start();
//        try {
//            t0.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
            String path = MainActivity.global_url + "/static/" + ListData.get(i).ID + "/img.jpg";
            //String path = MainActivity.global_url + "/static/default/img.jpg";
            //String path = "http://e.hiphotos.baidu.com/image/pic/item/a1ec08fa513d2697e542494057fbb2fb4316d81e.jpg";
            map.put("image", path);
            map.put("list_name","Name: " + ListData.get(i).list_name);
            map.put("list_major", "Major: " + ListData.get(i).list_major);
            map.put("list_class", ListData.get(i).list_class);
            if(ListData.get(i).info.length() > 20){
                map.put("info", "info: " + ListData.get(i).info.substring(0,20));
            }
            else
                map.put("info", "INFO: " + ListData.get(i).info);
            list.add(map);
        }
        return list;
    }

    public Bitmap getImage(String ID){
        String path = MainActivity.global_url + "/static/" + ID + "/img.jpg";
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
            return null;
        }
        catch (Exception e) {
            Log.e("e:", String.valueOf(e));
            System.out.println(e.toString());
            return null;
        }
    }

}

