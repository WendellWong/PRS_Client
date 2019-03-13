package com.wangz.prs_client;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.content_fragment_manager, null);
//        return view;
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//    }

    ListView list;

        private SimpleAdapter adapter;



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
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject obj = new JSONObject();
                    obj.put("jwt", userData.getJwt());
                    String jsonobj = obj.toString();
                    RequestBody body = RequestBody.create(JSON, jsonobj);
                    Request request = new Request.Builder()
                            .url("http://47.100.99.193:80/php/query/UserProperty.php")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
//            parseJsonWithJsonObject(responseData);
                    JSONObject jsonObject = new JSONObject(responseData);
                    String result = jsonObject.getString("result");
                    if(result.equals("success")){
                        UserProperty userProperty = new UserProperty();
                        String propertyString = jsonObject.getString("payload");
                        JSONArray propertyArray = new JSONArray(propertyString);
                        UserProperty.PayloadBean payloadBean = new UserProperty.PayloadBean();
                        List<UserProperty.PayloadBean> payload = new ArrayList<>();
                        payload = (List)JSON.parseArray(propertyArray, UserProperty.PayloadBean.class);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

            String[] listItem = { "房间", "床位", "储物柜", "休息间", "卫生间" };
            int[] iconItem = { R.drawable.logo, R.drawable.logo,
                    R.drawable.logo, R.drawable.logo,
                    R.drawable.logo };
            String[] listButton = {"解锁","解锁","解锁","解锁","解锁"};
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
            for (int i = 0; i < 5; i++) {
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
