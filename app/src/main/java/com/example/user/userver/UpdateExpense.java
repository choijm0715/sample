package com.example.user.userver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by user on 2016-09-01.
 */
public class UpdateExpense extends AppCompatActivity implements onNetworkResponseListener{

    private EditText etPaymentStoreName;    // 지급처
    private EditText etPaymentAmount;       // 지급액
    private EditText etSummary;             // 적요(메모)

    private JSONObject viewJsonObject;

    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.content_expense_request);
            initializeView();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_main:
                finish();
        }
        return (super.onOptionsItemSelected(item));
    }

    private void initializeView() throws Exception {

        etPaymentStoreName = (EditText) findViewById(R.id.place);
        etPaymentAmount = (EditText) findViewById(R.id.money);
        etSummary = (EditText) findViewById(R.id.memo);

        if ( !getIntent().hasExtra("json_data") || TextUtils.isEmpty(getIntent().getStringExtra("json_data"))) {
            Toast.makeText(this, "수정할 수 없습니다. 데이터를 확인하세요", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewJsonObject = new JSONObject(getIntent().getStringExtra("json_data"));

        etPaymentStoreName.setText(viewJsonObject.getString("PAYMENT_STORE_NM"));
        etPaymentAmount.setText(viewJsonObject.getString("PAYMENT_AMT"));
        etSummary.setText(viewJsonObject.getString("SUMMARY"));

        // 계정코드 조회
        getAccounttitleCodes();
    }





    private void getAccounttitleCodes() throws Exception {

        CommNetwork network = new CommNetwork(this, this);

        JSONObject requestObject = new JSONObject();
        network.requestToServer("ACCOUNT_L001", requestObject);
    }

    public void saveButton(View v) {
        try {

            EditText paymentStoreNm = (EditText) findViewById(R.id.place);
            EditText paymentAmount = (EditText) findViewById(R.id.money);
            EditText summary = (EditText) findViewById(R.id.memo);

            if ( TextUtils.isEmpty(paymentStoreNm.getText().toString())
                    || TextUtils.isEmpty(paymentAmount.getText().toString())
                    || TextUtils.isEmpty(summary.getText().toString()) ) {
                Toast.makeText(this, "필수입력 값이 누락되었습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 서버에 저장하기
            JSONObject requestObject = new JSONObject();
            requestObject.put("PAYMENT_STORE_NM", paymentStoreNm.getText().toString());
            requestObject.put("PAYMENT_AMT", paymentAmount.getText().toString());
            requestObject.put("SUMMARY", summary.getText().toString());
            requestObject.put("USER_ID", "test_user1");
            requestObject.put("EXPENSE_SEQ", viewJsonObject.getString("EXPENSE_SEQ"));

            CommNetwork network = new CommNetwork(this, this);
            network.requestToServer("EXPENSE_U001", requestObject);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSuccess(String api_key, JSONObject response) {


        try {

            if("ACCOUNT_L001".equals(api_key)) {
                //
                // 계정코드 조회
                //
                JSONArray array = response.getJSONArray("EXPENSE_REC");
            } else if ("EXPENSE_U001".equals(api_key)) {
                setResult(RESULT_OK);
                finish();
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

