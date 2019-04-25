package com.wangz.prs_client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoExchange extends AppCompatActivity {
    private List<UserProperty > payload = new ArrayList<>();
    List<HashMap<String, Object>> data = new ArrayList<>();
    ExcAdapter mOLAdapter;
    RecyclerView mRVPropety;
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
                    String jwt = Login.userData.getJwt();
                    Request request = new Request.Builder()
                            .url("http://47.100.99.193/api/web/property/list-my")
                            .addHeader("Authorization","Bearer " + jwt)
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    String status = jsonObject.getString("status");
                    if(status.equals("200")){
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
                    else if(status.equals("404")){
                        Looper.prepare();
                        Toast.makeText(DoExchange.this, getString(R.string.e404), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }else if(status.equals("400")){
                        Looper.prepare();
                        Toast.makeText(DoExchange.this, getString(R.string.e400), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }else if(status.equals("401")){
                        Looper.prepare();
                        Toast.makeText(DoExchange.this, getString(R.string.e401), Toast.LENGTH_SHORT).show();
                    }else if(status.equals("500")){
                        Looper.prepare();
                        Toast.makeText(DoExchange.this, getString(R.string.e500), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }else if(status.equals("501")){
                        Looper.prepare();
                        Toast.makeText(DoExchange.this, getString(R.string.e501), Toast.LENGTH_SHORT).show();
                        Looper.loop();
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

        String[] listItem = new String[payload.size()];
        int[] iconItem = new int[payload.size()];
        String[] listButton =new String[payload.size()];
        for (int i=0;i< payload.size();i++){
            listItem[i]=payload.get(i).getName();
            iconItem[i]= R.drawable.logo;
            listButton[i]=getString(R.string.doexchange);
        }
        if(data == null) {
            data = new ArrayList<>();
            HashMap<String, Object> map = new HashMap<>();
            map.put("name",    "无数据");
            map.put("icon",    R.drawable.avatar);
            map.put("button",  "交易");
            data.add(map);
        }else{
            HashMap<String, Object> map = new HashMap<>();
            for(int i=0;i< payload.size();i++){
                map.put("name",payload.get(i).getName());
                map.put("icon",R.drawable.logo);
                map.put("button",getString(R.string.doexchange));
                data.add(map);
            }
        }
        mRVPropety = findViewById(R.id.list_view);
        mOLAdapter = new ExcAdapter(mRVPropety);
        mOLAdapter.setData(data);
        mRVPropety.setAdapter(mOLAdapter);
        mRVPropety.setLayoutManager(    new LinearLayoutManager(this));
        mRVPropety.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mOLAdapter.setDoExchangeClickListener(new ExcAdapter.DoExchangeClickListener() {
            @Override
            public void OnDoExchangeClickListener(final int position) {
                new AlertDialog.Builder(DoExchange.this)
                        .setTitle("确认交易")
                        .setMessage("是否确认发出交易请求？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Thread thread2 =  new Thread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void run() {
                                        try {
                                            OkHttpClient client = new OkHttpClient();
                                            JSONObject obj = new JSONObject();
                                            JSONObject appendix =new JSONObject();
                                            JSONObject textinfoJson = new JSONObject();
                                            //获取系统的日期
                                            Date currentTime = new Date();
                                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String startTime = formatter.format(currentTime);
                                            String endTime = "";
                                            String delay= "3";
                                            ParsePosition pos = new ParsePosition(0);
                                            Date d = formatter.parse(startTime, pos);
                                            long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
                                            d.setTime(myTime * 1000);
                                            endTime = formatter.format(d);
                                            String textinfo = "自定义";
                                            appendix.put("OtherInfo",textinfo);
                                            int requestType = 3;
                                            UserProperty excpayload = payload.get(position);
                                            int requestObjId =excpayload.getId();
                                            String jwt = Login.userData.getJwt();
                                            obj.put("requestType",requestType);
                                            obj.put("requestObjId",requestObjId);
                                            obj.put("startTime",startTime);
                                            obj.put("endTime",endTime);
                                            obj.put("appendix",appendix);
                                            String jsonobj = obj.toString();
                                            RequestBody body = RequestBody.create(JSON, jsonobj);
                                            Request request = new Request.Builder()
                                                    .url("http://47.100.99.193/api/web/request/new")
                                                    .addHeader("Authorization","Bearer " + jwt)
                                                    .post(body)
                                                    .build();
                                            Response response = client.newCall(request).execute();
                                            String responseData = response.body().string();
                                            JSONObject jsonObject = new JSONObject(responseData);
                                            String status = jsonObject.getString("status");

                                            if(status.equals("200")){
//                                                Intent intent = new Intent(DoExchange.this,DoExchange.class);
//                                                DoExchange.this.startActivity(intent);
                                                Looper.prepare();
                                                Toast.makeText(DoExchange.this, getString(R.string.DoExcSuccess), Toast.LENGTH_SHORT).show();  //发起交易成功
                                                Looper.loop();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                thread2.start();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

    }
}

