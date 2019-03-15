package com.wangz.prs_client;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContentFragmentManager extends ListFragment {

    ListView list;
    private SimpleAdapter adapter;
    public  List<UserProperty > payload = new ArrayList<>();




        @Override

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.content_fragment_manager, container, false);
            list =  view.findViewById(android.R.id.list);
            return view;
        }


        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


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
                                Toast.makeText(getContext(), getString(R.string.noproperty), Toast.LENGTH_SHORT).show();  //没有物品提示
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

            String[] listItem = new String[payload.size()];
            int[] iconItem = new int[payload.size()];
            String[] listButton =new String[payload.size()];
            for (int i=0;i< payload.size();i++){
                listItem[i]=payload.get(i).getName();
                iconItem[i]= R.drawable.logo;
                listButton[i]=getString(R.string.unlock);
            }
            adapter = new SimpleAdapter(getActivity(), getData(listItem, iconItem,listButton),
                    R.layout.listview_manager, new String[] { "name", "icon" ,"button"},
                    new int[] { R.id.functionName, R.id.functionIcon ,R.id.functionButton});
            setListAdapter(adapter);
            //button解锁 待完善

        }
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
        }

        private List<? extends Map<String, ?>> getData(String[] strs, int[] icon,String[] butt) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < payload.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", strs[i]);
                map.put("icon", icon[i]);
                map.put("button",butt[i]);
                list.add(map);
            }
            return list;
        }


        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }



        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }

}
