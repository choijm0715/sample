package com.example.user.userver;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class ExpenseDetail extends AppCompatActivity implements onNetworkResponseListener {

    private JSONObject responseObject;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("상세조회");
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


        JSONObject viewObject = new JSONObject();
        try {
            viewObject.put("USER_ID", "test_user1");
            viewObject.put("EXPENSE_SEQ", getIntent().getStringExtra("EXPENSE_SEQ"));

            CommNetwork commNT = new CommNetwork(this, this);
            commNT.requestToServer("EXPENSE_R001", viewObject);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_main:
                Intent intent = new Intent(getApplicationContext(), Request_List.class);
                startActivity(intent);

                return true;


            case R.id.action_modify:
//                Toast.makeText(getApplicationContext(), "수정 메뉴",
//                       Toast.LENGTH_SHORT).show();
                Intent intent2  = new Intent(this, UpdateExpense.class);
                intent2.putExtra("json_data", responseObject.toString() );
                startActivityForResult(intent2, 1000);


                return true;

            case R.id.action_delete:
//                Toast.makeText(getApplicationContext(), "삭제 메뉴",
//                        Toast.LENGTH_SHORT).show();
                JSONObject deleteObject = new JSONObject();
                try {
                    deleteObject.put("USER_ID", "test_user1");
                    deleteObject.put("EXPENSE_SEQ", getIntent().getStringExtra("EXPENSE_SEQ"));

                    CommNetwork commNT = new CommNetwork(this, this);
                    commNT.requestToServer("EXPENSE_D001", deleteObject);

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSuccess(String api_key, JSONObject response) {

        TextView status = (TextView)findViewById(R.id.status);
        TextView place = (TextView)findViewById(R.id.place);
        TextView money = (TextView)findViewById(R.id.money);
        TextView dateShow = (TextView)findViewById(R.id.dateShow);
        TextView memo = (TextView)findViewById(R.id.memo);
        TextView account = (TextView)findViewById(R.id.account);

        try {
            if("EXPENSE_R001".equals(api_key)) {

                response.getString("EXPENSE_SEQ");
                place.setText(response.getString("PAYMENT_STORE_NM"));
                memo.setText(response.getString("SUMMARY"));
                dateShow.setText(response.getString("PAYMENT_DTTM"));
                money.setText(response.getString("PAYMENT_AMT"));
                response.getString("ADMISSION_STATUS_CD");
                status.setText(response.getString("ADMISSION_STATUS_NM"));
                account.setText(response.getString("ACCOUNT_TTL_NM"));
                response.getString("RECEPT_PHOTO");
                response.getString("INSERT_USERID");
            } else if("EXPENSE_D001".equals(api_key)){
                response.getString("DELETE_RSLT");
                response.getString("DELETE_RSLT_MSG");

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
