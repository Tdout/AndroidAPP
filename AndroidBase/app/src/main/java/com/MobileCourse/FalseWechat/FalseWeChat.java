package com.MobileCourse.FalseWechat;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.MobileCourse.R;
import com.MobileCourse.utils.CommonInterface;

public class FalseWeChat extends Activity implements View.OnClickListener {

    @BindView(R.id.img_social_circle)ImageView social_circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat);

        ButterKnife.bind(this);

        CommonInterface.addViewsListener(this, new int[]{
                R.id.social_circle_menu, R.id.scan_menu, R.id.swip_menu,
                R.id.look_menu, R.id.search_menu, R.id.people_nearby_menu,
                R.id.shop_menu, R.id.game_menu, R.id.applets_menu
            }, this);

        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        if (rotate != null) {
            social_circle.startAnimation(rotate);
        }  else {
            social_circle.setAnimation(rotate);
            social_circle.startAnimation(rotate);
        }
    }

    @Override
    public void onClick(View v) {
        Log.e("id", v.getId() + "");
    }
}
