package com.example.mobileassignment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.example.mobileassignment2.VolleyUtil.IP_;

public class AddFriendReceiveAdapter extends BaseAdapter {

    private List<AddRequest> data;
    private Context context;

    public AddFriendReceiveAdapter(List<AddRequest> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
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
        ViewHolder viewHolder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_add_receive,null);
            viewHolder=new ViewHolder();
            viewHolder.userImg=convertView.findViewById(R.id.item_receive_img);
            viewHolder.name=convertView.findViewById(R.id.name_item_add_receive);
            viewHolder.btnyes=convertView.findViewById(R.id.btn_accept_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        final ImageView img=viewHolder.userImg;
        VolleyUtil.imageVolley("http://"+IP_+":3000/"+data.get(position).getImgurl(), context, new VolleyUtil.ImgCallBack() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                img.setImageBitmap(bitmap);
            }
        });
//        viewHolder.userImg.setBackgroundResource(R.mipmap.ic_launcher);
        viewHolder.name.setText(data.get(position).getFname());
        viewHolder.btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyUtil.goVolley("http://" + IP_ + ":3000/userServlet?action=addFriendReceive&id=" + data.get(position).getRequestid() + "&uid=" + data.get(position).getUid() + "&fid=" + data.get(position).getFid(),
                        context, new VolleyUtil.VolleyCallback() {
                            @Override
                            public void onSuccess(String s) {
                                Toast.makeText(context,"Request approved",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        return convertView;
    }


    private static class ViewHolder{
        ImageView userImg;
        TextView name;
        Button btnyes;
    }
}
