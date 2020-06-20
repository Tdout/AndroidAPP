package com.MobileCourse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.MobileCourse.Adapter.MyFragmentAdapter;
import com.MobileCourse.Fragments.Fragment1;
import com.MobileCourse.Fragments.Fragment2;
import com.MobileCourse.Fragments.Fragment3;
import com.MobileCourse.Fragments.Fragment4;
import com.MobileCourse.utils.CommonInterface;
import com.MobileCourse.utils.WebSocket;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.navigation)
    BottomNavigationView navigationMenu;

    // 全局变量设置
    public static String global_url = "http://47.99.112.121";
    public static String global_port = "80";
    public static int global_login_type ;
    public static String global_login_id ;


    public static Handler msgHandler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 自动绑定view
        ButterKnife.bind(this);

        // 以下为fragment相关设置
        FragmentManager fm = getSupportFragmentManager();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new Fragment3());
        fragments.add(new Fragment4());

        viewPager.setAdapter(new MyFragmentAdapter(fm, fragments));
        viewPager.setOffscreenPageLimit(4);

        navigationMenu.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation1:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation2:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation3:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation4:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        });
        // 以上为fragment相关设置

        // 消息处理
        msgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        };

        // 关键权限必须动态申请
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

        // 初始化websocket
//        WebSocket.initSocket();
//
//        CommonInterface.sendOkHttpGetRequest("/hello", new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e("error", e.toString());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String resStr = response.body().string();
//                MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this, resStr, Toast.LENGTH_LONG).show());
//                Log.e("response", resStr);
//                try {
//                    // 解析json，然后进行自己的内部逻辑处理
//                    JSONObject jsonObject = new JSONObject(resStr);
//                } catch (JSONException e) {
//
//                }
//            }
//        });

    }

}
