package com.MobileCourse.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.MobileCourse.R;
import com.MobileCourse.TemplateActivity1;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Fragment4 extends Fragment {

//    @BindView(R.id.btn)
//    Button btn;
//
//    private boolean showBtn = true;
//
    public Fragment4() {
        // Required empty public constructor
    }

    // 控制是否展示对应的按钮
//    public Fragment4(boolean showBtn) {
//        this.showBtn = showBtn;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_4, container, false);
        initView();
        setData();
        return inflate;
    }
    private ImageView mHBack;
    private ImageView mHHead;
    private ImageView mUserLine;
    private TextView mUserName;
    private TextView mUserVal;

    private ItemView mNickName;
    private ItemView mSex;
    private ItemView mSignName;
    private ItemView mPass;
    private ItemView mPhone;
    private ItemView mAbout;

    private void setData() {
        //设置背景磨砂效果
        Glide.with(this).load(R.drawable.head)
                .apply(new RequestOptions().transform(new BlurTransformation(25, 25)))
                .apply(new RequestOptions().transform(new CenterCrop()))
                .into(mHBack);
        //设置圆形图像
        Glide.with(this).load(R.drawable.head)
                .apply(new RequestOptions().transform(new CropCircleTransformation()))
                .into(mHHead);

        //设置用户名整个item的点击事件
        mNickName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {

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
        mHBack =mHBack.findViewById(R.id.h_back);
        mHHead = mHHead.findViewById(R.id.h_head);
        mUserLine = mUserLine.findViewById(R.id.user_line);
        mUserName = mUserName.findViewById(R.id.user_name);
        mUserVal = mUserVal.findViewById(R.id.user_val);
        //下面item控件
        mNickName = mNickName.findViewById(R.id.nickName);
        mSex = mSex.findViewById(R.id.sex);
        mSignName = mSignName.findViewById(R.id.signName);
        mPass = mPass.findViewById(R.id.pass);
        mPhone = mPhone.findViewById(R.id.phone);
        mAbout = mAbout.findViewById(R.id.about);
    }
}
