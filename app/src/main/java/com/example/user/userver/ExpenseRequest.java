package com.example.user.userver;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class ExpenseRequest extends AppCompatActivity implements onNetworkResponseListener {

    AccountTitleSpinnerList spinnerList;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_request);
        getAccountList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("경비신청");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        ImageView save = (ImageView)findViewById(R.id.saveButton);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Request_List.class);
//                startActivity(intent);

                EditText place = (EditText)findViewById(R.id.place);
                EditText money = (EditText)findViewById(R.id.money);
                EditText memo = (EditText)findViewById(R.id.memo);
                TextView dateShow = (TextView)findViewById(R.id.dateShow);
                Spinner account = (Spinner)findViewById(R.id.account);



                if (TextUtils.isEmpty(place.getText()) || TextUtils.isEmpty(money.getText()) || TextUtils.isEmpty(memo.getText()) ) {
                    Toast.makeText(ExpenseRequest.this, "공백을 채워주세요", Toast.LENGTH_SHORT).show();

                    return;

                } else {

                    JSONObject requestObject = new JSONObject();
                    try {
                        requestObject.put("PAYMENT_STORE_NM", place.getText().toString());
                        requestObject.put("PAYMENT_AMT", money.getText().toString());
                        requestObject.put("PAYMENT_DTTM", dateShow.getText().toString());
                        requestObject.put("SUMMARY", memo.getText().toString());
                        requestObject.put("ACCOUNT_TTL_CD", spinnerList.getAccountTitleCd(account.getSelectedItemPosition()));
                        requestObject.put("USER_ID", "test_user1");

                        Log.d("ddd", requestObject.toString());

                        CommNetwork network = new CommNetwork(ExpenseRequest.this, ExpenseRequest.this);
                        network.requestToServer("EXPENSE_I001", requestObject);


                    } catch (Exception e) {

                        e.printStackTrace();
                    }


                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        Spinner spinner1 = (Spinner)findViewById(R.id.account);
        mDateShow = (TextView) findViewById(R.id.dateShow);
        mDateShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        updateDisplay();



        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerList);
        //spinner1.setAdapter(adapter);









    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }



    private  void updateDisplay() {
        mDateShow.setText(new StringBuilder()
                .append(mYear).append("-")
                .append(mMonth + 1).append("-")
                .append(mDay));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch(id)
        {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }


    private TextView mDateShow;
    private int mYear;
    private int mMonth;
    private int mDay;

    static final int DATE_DIALOG_ID=0;


    public void getAccountList() {
        JSONObject req_data = new JSONObject();

        try {
            req_data.put("USER_ID", "test_user1");


            CommNetwork commNetwork = new CommNetwork(this, this);
            commNetwork.requestToServer("ACCOUNT_L001", req_data);
        } catch (Exception e) {
            ErrorUtils.AlertException(this, "오류가 발생했습니다", e);
        }
    }


    @Override
    public void onSuccess(String api_key, JSONObject response) {
        // 성공시
//        Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show();
//
          JSONArray array = null;

        try {
            if("ACCOUNT_L001".equals(api_key)) {
                // 계정코드조회

                array = response.getJSONArray("REC");
                spinnerList = new AccountTitleSpinnerList(array);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExpenseRequest.this, android.R.layout.simple_spinner_item, spinnerList.getArrayList());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner = (Spinner) findViewById(R.id.account);
                spinner.setAdapter(adapter);
            } else if ("EXPENSE_I001".equals(api_key)) {
                // 경비신청
                if (!TextUtils.isEmpty(response.getString("EXPENSE_SEQ"))) {
                    Intent intent = new Intent(ExpenseRequest.this, Request_List.class);
                    intent.putExtra("EXPENSE_SEQ", response.getString("EXPENSE_SEQ"));
                    startActivity(intent);
                    finish();
                }
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
