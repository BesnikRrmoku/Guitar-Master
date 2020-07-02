package com.example.android.guitar.web;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.guitar.R;

import java.util.ArrayList;
import java.util.List;

public class WebActivity extends AppCompatActivity {

    public static final String LOG_TAG = WebActivity.class.getName();
    public static final String GUITAR_REQUEST_URL = "https://raw.githubusercontent.com/BesnikRrmoku/json/master/json";
    private WebAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        ListView guitarListView = (ListView) findViewById(R.id.list);
        mAdapter = new WebAdapter(this, new ArrayList<web>());
        guitarListView.setAdapter(mAdapter);
        guitarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                web currentWeb = mAdapter.getItem(position);
                Uri guitarUri = Uri.parse(currentWeb.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, guitarUri);
                startActivity(websiteIntent);
            }
        });
        GuitarAsyncTask task = new GuitarAsyncTask();
        task.execute(GUITAR_REQUEST_URL);

        getSupportActionBar().setTitle("Learn Online");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private class GuitarAsyncTask extends AsyncTask<String, Void, List<web>> {

        @Override
        protected List<web> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<web> result = QueryUtils.fetchGuitarData(urls[0]);
            return result;
        }
        @Override
        protected void onPostExecute(List<web> data) {
            mAdapter.clear();
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }
}
