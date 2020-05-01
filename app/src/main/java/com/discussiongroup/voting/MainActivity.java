package com.discussiongroup.voting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView regNoView = findViewById(R.id.regno);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("profile", MODE_PRIVATE);
        String regNo = pref.getString("reg_no", "-1-");

        if (regNo.equals("-1-")) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        regNoView.setText(regNo);

        try {
            fetchElectionsList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchElectionsList() throws Exception {
        Request request = new Request.Builder()
                .url("https://votingtest.herokuapp.com/api/elections")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("hey", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    final List<Election> elections = new ArrayList<>();
                    JSONObject responseObj = new JSONObject(Objects.requireNonNull(Objects.requireNonNull(response.body()).string()));
                    JSONObject dataObj = responseObj.getJSONObject("data");
                    Iterator<String> keys = dataObj.keys();

                    while (keys.hasNext()) {
                        JSONObject electionInfo = dataObj.getJSONObject(keys.next());
                        Election election = new Election(electionInfo.getInt("id"), electionInfo.getString("name"), electionInfo.getInt("start"), electionInfo.getInt("end"));
                        elections.add(election);
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView electionsListView = findViewById(R.id.electionsList);
                            electionsListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            ElectionsAdapter adapter = new ElectionsAdapter(MainActivity.this, elections);
                            electionsListView.setAdapter(adapter);
                        }
                    });
                } catch (JSONException e) {
                    Log.e("hey", e.toString());
                    e.printStackTrace();
                }
            }
        });
    }
}