package com.wangz.prs_client;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TestTrans extends Fragment {
    private Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bd = getArguments( ) ;
        View view = inflater.inflate(R.layout.test_trans, null) ;
        button =(Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DoExchange.class);
                getActivity().startActivity(intent);
            }
        });
        return view ;
    }



}
