package com.example.getdataapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.txtView, listItems);
        sp.setAdapter(adapter);
    }

    public void onStart(){
        super.onStart();
        BackTask bt=new BackTask();
        bt.execute();
    }

    private class BackTask extends AsyncTask<Void, Void, Void>{
        ArrayList<String> list;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();

        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://192.168.43.215/getHospitals.php");
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line=null;
                while ((line= bufferedReader.readLine()) != null){
                    result+=line;
                }
                is.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(jsonObject.getString("name"));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

}
