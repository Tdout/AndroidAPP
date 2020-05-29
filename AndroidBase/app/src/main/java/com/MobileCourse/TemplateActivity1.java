package com.MobileCourse;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.MobileCourse.Fragments.Fragment1;
import com.MobileCourse.Fragments.Fragment2;
import com.MobileCourse.Fragments.Fragment3;
import com.MobileCourse.Fragments.Fragment4;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TemplateActivity1 extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigationMenu;

    FragmentManager fm = getSupportFragmentManager();

    Fragment fragment1 = new Fragment1();
    Fragment fragment2 = new Fragment2(false);
    Fragment fragment3 = new Fragment3(false);
    Fragment fragment4 = new Fragment4(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        // 自动绑定view
        ButterKnife.bind(this);

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content, fragment1);
        transaction.add(R.id.content, fragment2);
        transaction.add(R.id.content, fragment3);
        transaction.add(R.id.content, fragment4);
        transaction.show(fragment1).hide(fragment2).hide(fragment3).hide(fragment4).commit();

        navigationMenu.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction trans = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation1:
                    trans.show(fragment1).hide(fragment2).hide(fragment3).hide(fragment4).commit();
                    return true;
                case R.id.navigation2:
                    trans.show(fragment2).hide(fragment1).hide(fragment3).hide(fragment4).commit();
                    return true;
                case R.id.navigation3:
                    trans.show(fragment3).hide(fragment1).hide(fragment2).hide(fragment4).commit();
                    return true;
                case R.id.navigation4:
                    trans.show(fragment4).hide(fragment1).hide(fragment2).hide(fragment3).commit();
                    return true;
            }
            return false;
        });

        Log.e("test", "tst");
    }
}
