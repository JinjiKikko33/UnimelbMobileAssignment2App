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


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddFriendActivity extends AppCompatActivity {

    private Button bt_add_search;
    private EditText et_add_usrname;
    private String userid;
    private AddFriendAdapter addFriendAdapter;
    private ListView searchResultList;
    private List<Friends> listData = new ArrayList<Friends>();
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_friend);

        // initialize view
        initView();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // get the user's id from their email by sending the relevant GET request
        String email = currentUser.getEmail();
        String url = "http://" + getString(R.string.host_name) + "/users/get-id-from-email/?email=" + email;
        Log.d("email", email);

        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String userid = null;

                try {
                    userid = Integer.toString(response.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                addFriendAdapter = new AddFriendAdapter(AddFriendActivity.this, listData, userid);
                searchResultList.setAdapter(addFriendAdapter);
                initListener();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);



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

            // request a list of possible users
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://52.189.254.126:3000/users/search-user?user_name=" + name;

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        Log.d("result", response.toString());
                        listData.clear();

                        for (int i = 0; i < response.length(); i++){
                            JSONObject jsonObject=response.getJSONObject(i);
                            listData.add(new Friends(jsonObject.getInt("id"),jsonObject.getString("name"),jsonObject.getString("photo_url")));
                        }
                        addFriendAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.d("ERROR", e.toString());
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(request);

        }
    }


    private void initView() {
        bt_add_search = (Button) findViewById(R.id.bt_add_search);
        et_add_usrname = (EditText) findViewById(R.id.et_add_usrname);
        searchResultList=findViewById(R.id.list_add_friends);
    }


}
