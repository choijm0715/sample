package com.example.user.userver;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Context CONTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CONTEXT = this;
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

//        final EditText EditText_id = (EditText)findViewById(R.id.EditText_id);
//        final EditText EditText_pwd = (EditText)findViewById(R.id.EditText_pwd);
        Button login = (Button)findViewById(R.id.btn_login);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String show_id = EditText_id.getText().toString();
//                String show_pwd = EditText_pwd.getText().toString();

//                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
//                intent.putExtra("id", show_id);
//                intent.putExtra("pwd", show_pwd);

//                startActivity(intent);
                EditText id = (EditText)findViewById(R.id.EditText_id);
                EditText pwd = (EditText)findViewById(R.id.EditText_pwd);
                String strId = id.getText().toString();
                String strPwd = pwd.getText().toString();


                if(strId.equals("123")) {
                    if(strPwd.equals("000")) {
                        new httpTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "login_do_get.php?id=" + strId + "&pwd="+ strPwd, " ");
                    } else {
                        new httpTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "login_do_get.php?id=" + strId + "&pwd="+ strPwd, " ");
                    }
                } else {
                    new httpTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "login_do_get.php?id=" + strId + "&pwd="+ strPwd, " ");
                }





            }
        });



    }

    public void onClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    //AsyncTask<param,Progress,Result>
    private class httpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... args) {

            String returnValue = "";
            HttpURLConnection conn = null;
            try {
                Log.e("!!!", "args[0] = " + args[0]);
                Log.e("!!!", "args[1] = " + args[1]);
                String urlString = "http://www.matescorp.com/soyu/" + args[0];
                Log.e("!!!", "urlString = " + urlString);
                URL url = new URL(urlString);

                // open connection
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);            // 입력스트림 사용여부
                conn.setDoOutput(false);            // 출력스트림 사용여부
                conn.setUseCaches(false);        // 캐시사용 여부
                conn.setReadTimeout(3000);        // 타임아웃 설정 ms단위
                conn.setRequestMethod("GET");  // or GET
//                conn.setRequestMethod("POST");

//                // POST 값 전달 하기
//                StringBuffer params = new StringBuffer("");
////                params.append("name=" + URLEncoder.encode(name)); //한글일 경우 URL인코딩
//                params.append(args[1]);
//                PrintWriter output = new PrintWriter(conn.getOutputStream());
//                output.print(params.toString());
//                output.close();

                // Response받기
                StringBuffer sb = new StringBuffer();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                for (; ; ) {
                    String line = br.readLine();
                    if (line == null) break;
                    sb.append(line + "\n");
                }

                br.close();
                conn.disconnect();
                br = null;
                conn = null;

                returnValue = sb.toString();
            } catch (ConnectException e) {
                e.printStackTrace();
                return "ConnectException|" + args[0] + "|" + args[1];
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                return "SocketTimeoutException|" + args[0] + "|" + args[1];
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn!=null) {
                    conn.disconnect();
                }
            }
            return returnValue;
        }

        @Override
        protected void onPostExecute(String result) {
            result = result.trim();
            Log.e("!!!", "httpTask result = | " + result + " |");
            if (result.trim().equals("") || result.trim().equals("[]") || result.trim().equals("null")) {
                Log.e("!!!", "------");

                return;
            } else {
                try {

                    if(result.equals("success")) {

                        EditText id = (EditText)findViewById(R.id.EditText_id);
                        EditText pwd = (EditText)findViewById(R.id.EditText_pwd);
                        String strId = id.getText().toString();
                        String strPwd = pwd.getText().toString();

                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        intent.putExtra("id", strId);
                        intent.putExtra("pwd", strPwd);
                        startActivity(intent);
                    }


                    Toast.makeText(CONTEXT, result, Toast.LENGTH_SHORT).show();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }
    }



}
