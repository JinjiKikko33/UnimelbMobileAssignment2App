package com.example.mobileassignment2;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.mobileassignment2.VolleyUtil.IP_;

public class AddFriendAdapter extends BaseAdapter {

    List<Friends> listData;
    Context context;
    String userid;

    public AddFriendAdapter(Context context, List<Friends> listData, String userid) {
        this.listData = listData;
        this.context =  context;
        this.userid = userid;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_add_friend,null);
            viewHolder = new ViewHolder();
            viewHolder.userImg = convertView.findViewById(R.id.item_add_img);
            viewHolder.username = convertView.findViewById(R.id.item_add_name);
            viewHolder.btnAdd = convertView.findViewById(R.id.btn_add_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        final ImageView img=viewHolder.userImg;
        VolleyUtil.imageVolley("http://52.189.254.126:3000/users/photo_url="+listData.get(position).getImgurl(), context, new VolleyUtil.ImgCallBack() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                img.setImageBitmap(bitmap);
            }
        });

//        viewHolder.userImg.setBackgroundResource(R.mipmap.ic_launcher);
        final int fid = listData.get(position).getFid();
        viewHolder.username.setText(listData.get(position).getFusername());
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                VolleyUtil.goVolley(String.format("http://52.189.254.126:3000/add-friend-by-id?id=%s&friend_id=%d", userid, fid), context,
                        new VolleyUtil.VolleyCallback() {
                            @Override
                            public void onSuccess(String s) {
                                viewHolder.btnAdd.setText("Followed");
                            }
                        });
            }
        });
        return convertView;

    }

    private static class ViewHolder{
        ImageView userImg;
        TextView username;
        Button btnAdd;

    }


}
