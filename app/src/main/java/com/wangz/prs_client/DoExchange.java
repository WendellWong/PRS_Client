package com.wangz.prs_client;

import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoExchange extends AppCompatActivity {
    private List<UserProperty > payload = new ArrayList<>();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.do_exchange);

        //发送占用物品查询请求
        Thread thread =  new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject obj = new JSONObject();
                    String pos ="0";
                    obj.put("jwt", Login.userData.getJwt());
                    obj.put("pos",pos);
                    String jsonobj = obj.toString();
                    RequestBody body = RequestBody.create(JSON, jsonobj);
                    Request request = new Request.Builder()
                            .url("http://47.100.99.193:80/php/query/UserProperty.php")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    String result = jsonObject.getString("result");
                    if(result.equals("success")){
                        UserProperty userProperty = new UserProperty();
                        String propertyString = jsonObject.getString("payload");
                        JSONArray propertyArray = new JSONArray(propertyString);

                        for (int i = 0; i < propertyArray.length(); ++i) {
                            UserProperty payloadBean = new UserProperty();
                            JSONObject token = propertyArray.getJSONObject(i);
                            payloadBean.setId(Integer.parseInt(token.getString("id")));
                            payloadBean.setLocation(Integer.parseInt(token.getString("location")));
                            payloadBean.setName(token.getString("name"));
                            payloadBean.setOccupier(Integer.parseInt(token.getString("occupier")));
                            payloadBean.setType(Integer.parseInt(token.getString("type")));
                            payloadBean.setStatus(Integer.parseInt(token.getString("status")));
                            payload.add(payloadBean);
                        }
                    }
                    else if(result.equals("fail")){
                        String failcode =jsonObject.getString("code");
                        if(failcode.equals("74")){
                            Looper.prepare();
                            Toast.makeText(getBaseContext(), getString(R.string.noproperty), Toast.LENGTH_SHORT).show();  //没有物品提示
                            Looper.loop();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ExcAdapter adapter =new ExcAdapter(DoExchange.this,R.layout.listview_manager,payload);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

    }
}

