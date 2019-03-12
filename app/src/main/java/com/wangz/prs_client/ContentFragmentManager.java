package com.wangz.prs_client;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            String[] listItem = { "房间", "床位", "储物柜", "休息间", "卫生间" };
            int[] iconItem = { R.drawable.logo, R.drawable.logo,
                    R.drawable.logo, R.drawable.logo,
                    R.drawable.logo };
            adapter = new SimpleAdapter(getActivity(), getData(listItem, iconItem),
                    R.layout.listview_manager, new String[] { "name", "icon" ,"functionButton"},
                    new int[] { R.id.functionName, R.id.functionIcon ,R.id.functionButton});
            setListAdapter(adapter);
            //button解锁 待完善


        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
        }

        private List<? extends Map<String, ?>> getData(String[] strs, int[] icon) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", strs[i]);
                map.put("icon", icon[i]);
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
