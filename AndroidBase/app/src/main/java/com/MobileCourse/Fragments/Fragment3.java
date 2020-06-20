package com.MobileCourse.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.MobileCourse.R;
import com.MobileCourse.TemplateActivity1;

public class Fragment3 extends Fragment {

    @BindView(R.id.search_btn)
    Button btn;
    View mView;

    private boolean showBtn = true;

    public Fragment3() {
        // Required empty public constructor
    }

    // 控制是否展示对应的按钮
    public Fragment3(boolean showBtn) {
        this.showBtn = showBtn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_2, container, false);

        return mView;
    }
}
