package com.MobileCourse.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.MobileCourse.Adapter.ListViewAdapter;
import com.MobileCourse.FalseWechat.FalseWeChat;
import com.MobileCourse.R;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

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
    private ArrayList<String> ListTitle;
    private ArrayList<Data> ListData;
    private Button mButton;

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
        imagePath.add("http://e.hiphotos.baidu.com/image/pic/item/a1ec08fa513d2697e542494057fbb2fb4316d81e.jpg");
        imagePath.add("http://c.hiphotos.baidu.com/image/pic/item/30adcbef76094b36de8a2fe5a1cc7cd98d109d99.jpg");
        imageTitle.add("图片1");
        imageTitle.add("图片2");
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (ListView)mView.findViewById(R.id.list);
        List<Map<String, Object>> list=getData();
        mListView.setAdapter(new ListViewAdapter(getActivity(), list));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "点击时间-SHOWMORE: " + ListData.get(i).ID, Toast.LENGTH_LONG).show();
                System.out.println("获取的ID： " + ListData.get(i).ID);
                Intent intent = new Intent(getActivity(), FalseWeChat.class);
                intent.putExtra("ID", ListData.get(i).ID);
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
                Toast.makeText(getActivity(), "点击事件-轮播页" + (position+1), Toast.LENGTH_LONG).show();
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

    public List<Map<String, Object>> getData(){
        ListTitle = new ArrayList<>();
        for (int i = 0;i<10;i++){
            ListTitle.add("Name");
            ListTitle.add("Major");
            ListTitle.add("Class");
        }
        ListData = new ArrayList<>();
        Data e = new Data();
        for (int i = 0;i<10;i++){
            e.list_name = "Name";
            e.list_major = "Major";
            e.list_class = "Class";
            e.ID = "ID_NUM";
            e.info = "INFO";
            ListData.add(e);
        }
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < ListData.size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("image", R.drawable.ic_people_nearby); // 图片获取
            map.put("list_name","Name: " + ListData.get(i).list_name);
            map.put("list_major", "Major: " + ListData.get(i).list_major);
            map.put("list_class", "Class: " + ListData.get(i).list_class);
            map.put("info", "Interests: " + ListData.get(i).info);
            list.add(map);
        }
        return list;
    }


}
