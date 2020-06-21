package com.MobileCourse.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.MobileCourse.ChangePswActivity;
import com.MobileCourse.FalseWechat.ShowMorePage;
import com.MobileCourse.FlagActivity;
import com.MobileCourse.LoginActivity;
import com.MobileCourse.MainActivity;
import com.MobileCourse.R;
import com.MobileCourse.TemplateActivity1;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
//import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Fragment4 extends Fragment {

    View mView;
    private ImageView mHBack;
    private ImageView mHHead;
    private ImageView mUserLine;
    private TextView mUserName;
    private TextView mUserEmail;

    private ItemView centerUserInfo;
    private ItemView mSex;
    private ItemView mSignName;
    private ItemView centerPass;
    private ItemView centerFlag;
    private ItemView mAbout;


    public Fragment4() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_4, container, false);
        initView();
        System.out.println("---------------------3---------------------");
        setData();
        System.out.println("---------------------4---------------------");
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("---------------------R---------------------");
        Glide.with(this).load(MainActivity.global_url+"/static/"+MainActivity.global_login_id+"/img.jpg").
                apply(new RequestOptions().
                        placeholder(R.drawable.ic_people_nearby).
                        error(R.drawable.ic_people_nearby))
                .apply(new RequestOptions().transform(new CircleCrop()))
                .into(mHHead);
    }

    private void setData() {
        //设置背景磨砂效果
//        Glide.with(this).load(R.drawable.back_gradient)
//                .apply(new RequestOptions().bitmapTransform(new BlurTransformation(25,1)))
//                .apply(new RequestOptions().centerCrop())
//                .into(mHBack);
        //设置圆形图像
//        Glide.with(this).load(R.drawable.head)
//                .apply(new RequestOptions().transform(new CircleCrop()))
//                .into(mHHead);
//        Glide.with(mHBack.getContext()).load(R.drawable.head)
//                .bitmapTransform(new BlurTransformation(mHBack.getContext(), 25), new CenterCrop(mHBack.getContext()))
//                .into(mHBack);
//        Glide.with(mHHead.getContext()).load(R.drawable.head)
//                .bitmapTransform(new CropCircleTransformation(mHHead.getContext()))
//                .into(mHHead);
        Glide.with(this).load(MainActivity.global_url+"/static/"+MainActivity.global_login_id+"/img.jpg").
                apply(new RequestOptions().
                        placeholder(R.drawable.ic_people_nearby).
                        error(R.drawable.ic_people_nearby))
                .apply(new RequestOptions().transform(new CircleCrop()))
                .into(mHHead);

        //设置用户名整个item的点击事件
        centerFlag.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), FlagActivity.class));
            }
        });

        centerPass.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), ChangePswActivity.class));
            }
        });

        //修改用户名item的左侧图标
      /* mNickName.setLeftIcon(R.drawable.ic_phone);
        //
        mNickName.setLeftTitle("修改后的用户名");
        mNickName.setRightDesc("名字修改");
        mNickName.setShowRightArrow(false);
        mNickName.setShowBottomLine(false);

        //设置用户名整个item的点击事件
        mNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "我是onclick事件显示的", Toast.LENGTH_SHORT).show();
            }
        });
*/

    }
    private void initView() {
        //顶部头像控件
        System.out.println("---------------------1---------------------");
        mHBack =mView.findViewById(R.id.h_back);
        mHHead = mView.findViewById(R.id.h_head);
        mUserLine = mView.findViewById(R.id.user_line);
        mUserName = mView.findViewById(R.id.center_user_name);
        mUserEmail = mView.findViewById(R.id.center_user_email);
        //下面item控件
        System.out.println("---------------------2---------------------");
        centerUserInfo = mView.findViewById(R.id.center_user_info);
        mSex = mView.findViewById(R.id.sex);
        mSignName = mView.findViewById(R.id.signName);
        centerPass = mView.findViewById(R.id.center_pass);
        centerFlag = mView.findViewById(R.id.center_flag);
        mAbout = mView.findViewById(R.id.about);
    }
}
