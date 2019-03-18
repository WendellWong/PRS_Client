package com.wangz.prs_client;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.media.Image;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentFragmentTrans extends ListFragment {

    ListView list;
    private SimpleAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_content_fragment_trans, container, false);
        list =  view.findViewById(android.R.id.list);
        return view ;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Button buttonFooter = new Button(this);
//        buttonFooter.setText("下面的按钮");
//        buttonFooter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//        list.addFooterView(buttonFooter);


        String[] listItem ={"1","2","3","4","5"};
        String[] listItem2 ={"1","2","3","4","5"};
        int[] iconItem = {R.drawable.logo,R.drawable.logo,R.drawable.logo,R.drawable.logo,R.drawable.logo};
        int[] iconItem2 = {R.drawable.logo,R.drawable.logo,R.drawable.logo,R.drawable.logo,R.drawable.logo};
        int[] listExclog ={R.drawable.discover,R.drawable.discover,R.drawable.discover,R.drawable.discover,R.drawable.discover};
        String[] listButton1 ={getString(R.string.doexchange),getString(R.string.doexchange),getString(R.string.doexchange),getString(R.string.doexchange),getString(R.string.doexchange)};
        String[] listButton2 ={getString(R.string.cancel),getString(R.string.cancel),getString(R.string.cancel),getString(R.string.cancel),getString(R.string.cancel)};
//        for (int i=0;i<5;i++){
////            listItem[i]=ContentFragmentManager.payload.get(i).getName();
//            iconItem[i]= R.drawable.logo;
//            listExclog[i]=R.drawable.discover;
//            iconItem2[i]= R.drawable.logo;
//            listButton1[i]=getString(R.string.doexchange);
//            listButton2[i]=getString(R.string.cancel);
//
//        }
        adapter = new SimpleAdapter(getActivity(), getData(listItem, iconItem,listExclog,listItem2, iconItem2,listButton1,listButton2),
                R.layout.listview_trans, new String[] { "name", "icon" ,"exclogo","name2","icon2","button1","button2"},
                new int[] { R.id.functionName, R.id.functionIcon ,R.id.functionExc,R.id.functionName2,R.id.functionIcon2,R.id.functionDoexchange,R.id.functionCancel});
        setListAdapter(adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    private List<? extends Map<String, ?>> getData(String[] strs, int[] icon, int[] exlog,String[] strs2, int[] icon2,String[] butt1,String[] butt2) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", strs[i]);
            map.put("icon", icon[i]);
            map.put("exclogo",exlog[i]);
            map.put("name2", strs2[i]);
            map.put("icon2", icon2[i]);
            map.put("button1",butt1[i]);
            map.put("button2",butt2[i]);
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

