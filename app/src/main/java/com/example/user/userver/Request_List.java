package com.example.user.userver;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Request_List extends AppCompatActivity implements onNetworkResponseListener {

    ArrayList<ExpenseListModel> listDatas;
    ListView listView;
    ExpenseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("경비관리");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        List<CustomDataModel> list = new ArrayList<>();
//        String company;
//        String explanation;
//        String date;
//        String situation;
//        String money;

//        list.add(new CustomDataModel("엉터리생고기1", "홍길동외 2인 식대", "20160607", "승인대기", "23500"));
//        list.add(new CustomDataModel("엉터리생고기2", "홍길동외 3인 식대", "20160407", "승인대기", "29500"));
//        list.add(new CustomDataModel("엉터리생고기3", "홍길동외 2인 식대", "20160307", "승인대기", "45500"));
//        list.add(new CustomDataModel("엉터리생고기4", "홍길동외 3인 식대", "20160207", "승인대기", "500"));
//        list.add(new CustomDataModel("엉터리생고기5", "홍길동외 2인 식대", "20160107", "승인대기", "23500"));
//        list.add(new CustomDataModel("엉터리생고기6", "홍길동외 3인 식대", "20160610", "승인대기", "20500"));
//        list.add(new CustomDataModel("엉터리생고기7", "홍길동외 2인 식대", "20160702", "승인대기", "23500"));

        listView = (ListView)findViewById(R.id.LV1);
        adapter = new ExpenseListAdapter(this);
        listDatas = new ArrayList<>();

//        CustomListViewAdapter adapter = new CustomListViewAdapter(this);
//        adapter.setListData(list);
//        listView.setAdapter(adapter);

        ImageView add = (ImageView)findViewById(R.id.addButton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExpenseRequest.class);
                startActivity(intent);
            }
        });

        JSONObject viewObject = new JSONObject();
        try {
            viewObject.put("USER_ID", "test_user1");
            viewObject.put("PAGE_NO", "1");
            viewObject.put("PAGE_CNT", "20");

            CommNetwork commNT = new CommNetwork(Request_List.this, Request_List.this);
            commNT.requestToServer("EXPENSE_L001", viewObject);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Request_List.this, ExpenseDetail.class);
                    intent.putExtra("EXPENSE_SEQ", listDatas.get(position).expenseSeq);
                    startActivity(intent);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String api_key, JSONObject response) {


        try {
            if("EXPENSE_L001".equals(api_key)) {
                JSONArray array = response.getJSONArray("REC");

                for (int i=0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    ExpenseListModel data = new ExpenseListModel();
                    data.expenseSeq = object.getString("EXPENSE_SEQ");
                    data.paymentStoreName = object.getString("PAYMENT_STORE_NM");
                    data.status = object.getString("ADMISSION_STATUS_NM");
                    data.summary = object.getString("SUMMARY");
                    data.paymentDate = object.getString("PAYMENT_DTTM");
                    data.paymentAmount = object.getString("PAYMENT_AMT");

                    listDatas.add(data);
                }
                adapter.setList(listDatas);
                listView.setAdapter(adapter);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFailure(String api_key, String error_cd, String error_msg) {
        // 실패시
        Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show();
    }

}
