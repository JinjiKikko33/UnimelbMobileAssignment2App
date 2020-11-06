package com.example.mobileassignment2;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.mobileassignment2.VolleyUtil.IP_;


public class AddFriendActivity extends AppCompatActivity {

    private Button bt_add_search;
    private EditText et_add_usrname;
    private String userid;
    private AddFriendAdapter addFriendAdapter;
    private ListView searchResultList;
    private List<Friends> listData = new ArrayList<Friends>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_friend);

        // initialize view
        initView();

        initListener();
    }

    private void initListener() {
        // search button event
        bt_add_search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                search();

            }
        });


    }

    // search button action
    private void search() {
        // access to username
        String name = et_add_usrname.getText().toString();

        // check the input username
        if (TextUtils.isEmpty(name)){
            Toast.makeText(AddFriendActivity.this, "Username should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        // verify the existence of username
        if (!"".equals(name)){
            try {
                name = java.net.URLEncoder.encode(java.net.URLEncoder.encode(name, "utf-8"));
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            VolleyUtil.goVolley("http://" + IP_ + ":3000//users/search-user?user_name=" + name, AddFriendActivity.this,
                    new VolleyUtil.VolleyCallback() {
                        @Override
                        public void onSuccess(String s) throws JSONException {
                            listData.clear();
                            JSONArray jsonArray=new JSONArray(s);
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                listData.add(new Friends(jsonObject.getInt("id"),jsonObject.getString("username"),jsonObject.getString("imgurl")));
                            }
                            addFriendAdapter.notifyDataSetChanged();
                        }

                    });
        }
    }


    private void initView() {
        bt_add_search = (Button) findViewById(R.id.bt_add_search);
        et_add_usrname = (EditText) findViewById(R.id.et_add_usrname);
        userid = getIntent().getExtras().getString("user_id");
        addFriendAdapter = new AddFriendAdapter(AddFriendActivity.this, listData, userid);
        searchResultList.setAdapter(addFriendAdapter);
    }


}
