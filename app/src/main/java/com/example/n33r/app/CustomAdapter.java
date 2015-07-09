package com.example.n33r.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by N33R on 6/19/2015.
 */
public class CustomAdapter<C> extends BaseAdapter {
    String name;
    String phone;
    int image;
    Context context;
    ArrayList<CustomUser> list = new ArrayList<>();

    public CustomAdapter(Context context, ArrayList<CustomUser> list) {
        this.list = list;
        this.context = context;
    }

    static class ViewHolder {
        public TextView name, phone;
        public CircleImageView image;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.customview, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) v.findViewById(R.id.name);
            holder.phone = (TextView) v.findViewById(R.id.phone);
            holder.image=(CircleImageView)v.findViewById(R.id.profile_image);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        CustomUser user = list.get(position);
        if (user != null) {
            holder.name.setText(user.getName());
            holder.phone.setText(user.getPhoneNumber());
            holder.image.setImageBitmap(user.getImage());
        }
        return v;
    }
}
