package com.MobileCourse.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.MobileCourse.R;

public class ShowListViewAdapter extends BaseAdapter{
    private ArrayList<HashMap<String, Object>> data;
    private LayoutInflater layoutInflater;

    public ShowListViewAdapter(Context context,
                           ArrayList<HashMap<String, Object>> data) {
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void modifyStates(int position) {

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewholder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.show_more_list, null);
            viewHolder = new MyViewholder();

            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView1);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewholder) convertView.getTag();
        }

        viewHolder.textView.setText((String) data.get(position).get("text"));

        return convertView;
    }


}

class MyViewholder{
    public TextView textView;

}
