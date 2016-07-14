package com.example.user.userver;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Request_List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        List<CustomDataModel> list = new ArrayList<>();
//        String company;
//        String explanation;
//        String date;
//        String situation;
//        String money;

        list.add(new CustomDataModel("엉터리생고기1", "홍길동외 2인 식대", "20160607", "승인대기", "23500"));
        list.add(new CustomDataModel("엉터리생고기2", "홍길동외 3인 식대", "20160407", "승인대기", "29500"));
        list.add(new CustomDataModel("엉터리생고기3", "홍길동외 2인 식대", "20160307", "승인대기", "45500"));
        list.add(new CustomDataModel("엉터리생고기4", "홍길동외 3인 식대", "20160207", "승인대기", "500"));
        list.add(new CustomDataModel("엉터리생고기5", "홍길동외 2인 식대", "20160107", "승인대기", "23500"));
        list.add(new CustomDataModel("엉터리생고기6", "홍길동외 3인 식대", "20160610", "승인대기", "20500"));
        list.add(new CustomDataModel("엉터리생고기7", "홍길동외 2인 식대", "20160702", "승인대기", "23500"));

        ListView listView = (ListView)findViewById(R.id.LV1);

        CustomListViewAdapter adapter = new CustomListViewAdapter(this);
        adapter.setListData(list);

        listView.setAdapter(adapter);
    }

}
