package com.example.mobileassignment2;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.util.List;


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

        // download image of potential friend

        final ImageView img=viewHolder.userImg;
        String imageUrl = listData.get(position).getImgurl();

        new DownloadImageTask(img).execute(imageUrl);



//        viewHolder.userImg.setBackgroundResource(R.mipmap.ic_launcher);
        final int fid = listData.get(position).getFid();
        viewHolder.username.setText(listData.get(position).getFusername());
        // follow button action
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(context);
                String url = String.format("http://52.189.254.126:3000/users/add-friend-by-id?id=%s&friend_id=%d", userid, fid);

                Log.d("request url", url);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String userMessage = "You have added a friend!";
                                Toast toast = Toast.makeText(context, userMessage, Toast.LENGTH_LONG);
                                toast.show();
                                Log.d("Backend response: ", response);
                                viewHolder.btnAdd.setText("Followed");

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Backend response error: ", error.toString());
                    }
                });
                queue.add(stringRequest);

            }
        });
        return convertView;

    }

    private static class ViewHolder{
        ImageView userImg;
        TextView username;
        Button btnAdd;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView userImage;

        public DownloadImageTask(ImageView userImage) {
            this.userImage = userImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap icon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                icon = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());

            }
            return icon;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            userImage.setImageBitmap(result);
        }
    }


}




