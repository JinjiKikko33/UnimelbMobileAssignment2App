package com.example.mobileassignment2;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.mobileassignment2.VolleyUtil.IP_;

public class AddFriendReceiveActivity extends AppCompatActivity {
    private String userid;
    private ListView receiveList;
    private List<AddRequest> data=new ArrayList<AddRequest>();
    private AddFriendReceiveAdapter addFriendReceiveAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_receive);
        userid=getIntent().getExtras().getString("userid");

        receiveList=findViewById(R.id.list_add_requests);
        addFriendReceiveAdapter=new AddFriendReceiveAdapter(data,AddFriendReceiveActivity.this);
        receiveList.setAdapter(addFriendReceiveAdapter);
        initData();
    }

    private void initData(){
        VolleyUtil.goVolley("http://" + IP_ + ":8080/AndroidServiceHi/userServlet?action=getFriendAddList&fid=" + userid, AddFriendReceiveActivity.this,
                new VolleyUtil.VolleyCallback() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONArray jsonArray=new JSONArray(s);
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                data.add(new AddRequest(jsonObject.getString("id"),
                                        jsonObject.getString("fid"),
                                        jsonObject.getString("uid"),
                                        jsonObject.getString("username"),
                                        jsonObject.getString("imgurl")));
                            }
                            addFriendReceiveAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
