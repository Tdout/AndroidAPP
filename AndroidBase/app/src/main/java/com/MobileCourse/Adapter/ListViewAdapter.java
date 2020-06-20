package com.MobileCourse.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.MobileCourse.MainActivity;
import com.MobileCourse.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private String path = MainActivity.global_url + "/static/default/img.jpg";
    private LayoutInflater layoutInflater;
    private Context context;
    public ListViewAdapter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }
    /**
     * 组件集合，对应list.xml中的控件
     */
    public final class myList{
        public ImageView image;
        public TextView list_name;
        public TextView list_major;
        public TextView list_class;
        public Button list_button;
        public TextView info;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        myList mList=null;
        if(convertView==null){
            mList=new myList();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.listview, null);
            mList.image=(ImageView)convertView.findViewById(R.id.image);
            mList.list_name=(TextView)convertView.findViewById(R.id.list_name);
            mList.list_major=(TextView)convertView.findViewById(R.id.list_major);
            mList.list_class=(TextView)convertView.findViewById(R.id.list_class);
            mList.list_button=(Button)convertView.findViewById(R.id.list_button);
            mList.info=(TextView)convertView.findViewById(R.id.info);
            convertView.setTag(mList);
        }else{
            mList=(myList) convertView.getTag();
        }
        //绑定数据
        //mList.image.setBackgroundResource((Integer)data.get(position).get("image"));
        //mList.image.setImageBitmap((String) data.get(position).get("image"));

        Glide.with(convertView).load((String) data.get(position).get("image")).
                apply(new RequestOptions().
                placeholder(R.drawable.ic_people_nearby).
                        error(R.drawable.ic_people_nearby))
                .into(mList.image);
        mList.list_name.setText((String)data.get(position).get("list_name"));
        mList.list_major.setText((String)data.get(position).get("list_major"));
        mList.list_class.setText((String)data.get(position).get("list_class"));
        mList.info.setText((String)data.get(position).get("info"));
        return convertView;
    }

}