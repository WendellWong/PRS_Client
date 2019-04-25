package com.wangz.prs_client;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import okhttp3.Response;

public class ContentFragmentTrans extends ListFragment {

    ListView list;
    private SimpleAdapter adapter;
    private Button button;
    public  List<UserProperty > payload = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_content_fragment_trans, container, false);
        list =  view.findViewById(android.R.id.list);
        button =(Button)view.findViewById(R.id.DoExchange);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DoExchange.class);
                getActivity().startActivity(intent);
            }
        });
        return view ;
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread thread =  new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    String jwt = Login.userData.getJwt();
                    Request request = new Request.Builder()
                            .url("http://47.100.99.193/api/web/request/list-exchanges?size=10&amp;page=1")
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
                            String appendix = token.getString("appendix");
                            if(!appendix.equals(null)){
                                payloadBean.setAppendixinfo(token.getString("appendix"));
                            }else
                                payloadBean.setAppendixinfo("null");
                            payloadBean.setId(Integer.parseInt(token.getString("requestObjId")));
                            payloadBean.setEndtime(token.getString("endTime"));
                            payloadBean.setStarttime(token.getString("startTime"));
//                            payloadBean.setLocation(Integer.parseInt(token.getString("location")));
//                            payloadBean.setName(token.getString("name"));
//                            payloadBean.setOccupier(Integer.parseInt(token.getString("occupier")));
//                            payloadBean.setType(Integer.parseInt(token.getString("type")));
//                            payloadBean.setStatus(Integer.parseInt(token.getString("status")));
                            payload.add(payloadBean);
                        }
                    }
                    else if(status.equals("404")){
                        Looper.prepare();
                        Toast.makeText(getContext(), getString(R.string.e404), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }else if(status.equals("400")){
                        Looper.prepare();
                        Toast.makeText(getContext(), getString(R.string.e400), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }else if(status.equals("401")){
                        Looper.prepare();
                        Toast.makeText(getContext(), getString(R.string.e401), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }else if(status.equals("500")){
                        Looper.prepare();
                        Toast.makeText(getContext(), getString(R.string.e500), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }else if(status.equals("501")){
                        Looper.prepare();
                        Toast.makeText(getContext(), getString(R.string.e501), Toast.LENGTH_SHORT).show();
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

        int [] listItem = new int[payload.size()];
        String[] listItemext = new String[payload.size()];
        String[] listItemstime = new String[payload.size()];
        String[] listItemetime = new String[payload.size()];
        int[] iconItem = new int[payload.size()];
        String[] listButton =new String[payload.size()];
        for (int i=0;i< payload.size();i++){
            listItem[i]=payload.get(i).getId();
            listItemext[i]=payload.get(i).getAppendixinfo();
            listItemstime[i]=payload.get(i).getStarttime();
            listItemetime[i]=payload.get(i).getEndtime();
            iconItem[i]= R.drawable.avatar;
            listButton[i]=getString(R.string.doexchange);
        }


        adapter = new SimpleAdapter(getActivity(), getData(listItem, listItemext,listItemstime,listItemetime,iconItem,listButton),
                R.layout.listview_trans, new String[] { "id", "appendix" ,"stime","etime","icon","button"},
                new int[] { R.id.functionName, R.id.functionAppendix ,R.id.functionStartTime,R.id.functionEndTime,R.id.functionIcon,R.id.functionDoexchange});
        setListAdapter(adapter);

    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//    }

    private List<? extends Map<String, ?>> getData(int[] strs, String[] listapd, String[] stime,String[] etime, int[] icon,String[] butt) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i <  payload.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", strs[i]);
            map.put("appendix", listapd[i]);
            map.put("stime",stime[i]);
            map.put("etime", etime[i]);
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

