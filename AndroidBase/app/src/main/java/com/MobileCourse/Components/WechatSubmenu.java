package com.MobileCourse.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.MobileCourse.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WechatSubmenu extends RelativeLayout {

    @BindView(R.id.sub_menu_icon) ImageView sub_menu_icon;
    @BindView(R.id.sub_menu_text) TextView sub_menu_text;

    private View mView;

    public WechatSubmenu(Context context) {
        this(context, null);
    }

    public WechatSubmenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WechatSubmenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.component_wechat_submenu,this,true);
        ButterKnife.bind(mView);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.wechat_submenu);
        sub_menu_icon.setImageResource(a.getResourceId(R.styleable.wechat_submenu_src, R.drawable.ic_social_circle));
        sub_menu_text.setText(a.getString(R.styleable.wechat_submenu_text));
    }
}
