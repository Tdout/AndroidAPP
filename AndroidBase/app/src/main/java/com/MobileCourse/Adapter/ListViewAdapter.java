package com.MobileCourse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.MobileCourse.R;

import java.util.List;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public ListViewAdapter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }
    /**
     * 组件集合，对应list.xml中的控件
     * @author Administrator
     */
    public final class Zujian{
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
        Zujian zujian=null;
        if(convertView==null){
            zujian=new Zujian();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.listview, null);
            zujian.image=(ImageView)convertView.findViewById(R.id.image);
            zujian.list_name=(TextView)convertView.findViewById(R.id.list_name);
            zujian.list_major=(TextView)convertView.findViewById(R.id.list_major);
            zujian.list_class=(TextView)convertView.findViewById(R.id.list_class);
            zujian.list_button=(Button)convertView.findViewById(R.id.list_button);
            zujian.info=(TextView)convertView.findViewById(R.id.info);
            convertView.setTag(zujian);
        }else{
            zujian=(Zujian)convertView.getTag();
        }
        //绑定数据

        zujian.image.setBackgroundResource((Integer)data.get(position).get("image"));
        zujian.list_name.setText((String)data.get(position).get("list_name"));
        zujian.list_major.setText((String)data.get(position).get("list_major"));
        zujian.list_class.setText((String)data.get(position).get("list_class"));
        zujian.info.setText((String)data.get(position).get("info"));
        return convertView;
    }

}