package com.MobileCourse.FalseWechat;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.MobileCourse.Fragments.Fragment1;
import com.MobileCourse.R;
import com.MobileCourse.utils.CommonInterface;

public class ShowMorePage extends Activity implements View.OnClickListener {

    //@BindView(R.id.img_social_circle)ImageView social_circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat);
        String TargetID = getID();
        // 网络请求

        ButterKnife.bind(this);

        CommonInterface.addViewsListener(this, new int[]{
                R.id.research_dir,
                R.id.skill,
                R.id.exp
            }, this);

//        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
//        if (rotate != null) {
//            social_circle.startAnimation(rotate);
//        }  else {
//            social_circle.setAnimation(rotate);
//            social_circle.startAnimation(rotate);
//        }
    }


    @Override
    public void onClick(View v) {
        Log.e("id", v.getId() + "");
        Toast.makeText(this,v.getId()+"",Toast.LENGTH_SHORT).show();
    }
    public String getID() {
        Intent intent = getIntent();
        String message = intent.getStringExtra(Fragment1.ID_MESSAGE);
        //TextView showMessage = findViewById(R.id.showmessage);
        //showMessage.setText(message);
        System.out.println(message);
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        return message;
    }
}
