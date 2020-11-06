package com.example.mobileassignment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class VolleyUtil {
    public static final String IP_="52.189.254.126";

    public interface VolleyCallback {
        void onSuccess(String s) throws JSONException;
    }

    public interface ImgCallBack{
        void onSuccess(Bitmap bitmap);
    }

    public static void goVolley(String url, Context context1, final VolleyCallback callback){
        final Context context = context1;
        RequestQueue mQueue= Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            callback.onSuccess(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("TAG",volleyError.getMessage(),volleyError);
                    }
                }
        )
        {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString=new String(response.data,"utf-8");
                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }catch (Exception je){
                    return Response.error(new ParseError(je));
                }
            }
        };
        mQueue.add(stringRequest);
    }




    public static void imageVolley(String url, final Context context1, final ImgCallBack callback){
        RequestQueue requestQueue = Volley.newRequestQueue(context1);
        //实例ImageRequest，并设置参数，分别为地址，响应成功监听，最大宽、高，图片质量，网络异常监听
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        callback.onSuccess(bitmap);
                    }
                }, 500, 500, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(context1,"Network anomaly",Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(imageRequest);
    }

}
