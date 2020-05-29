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

public class Fragment4 extends Fragment {

    @BindView(R.id.btn)
    Button btn;

    private boolean showBtn = true;

    public Fragment4() {
        // Required empty public constructor
    }

    // 控制是否展示对应的按钮
    public Fragment4(boolean showBtn) {
        this.showBtn = showBtn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_2, container, false);
        ButterKnife.bind(this, inflate);
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), TemplateActivity1.class);
            startActivity(intent);
        });
        if (!showBtn)
            btn.setVisibility(View.INVISIBLE);
        return inflate;
    }
}
