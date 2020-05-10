package com.discussiongroup.voting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewElectionActivity extends AppCompatActivity {
    final OkHttpClient client = new OkHttpClient();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_election);
        Intent intent = getIntent();
        TextView electionNameView = findViewById(R.id.electionName);
        TextView startView = findViewById(R.id.start);
        TextView endView = findViewById(R.id.end);
        int electionId = intent.getIntExtra("electionId", 0);
        electionNameView.setText(intent.getStringExtra("electionName"));
        int startTimestamp = intent.getIntExtra("startTimestamp", 0);
        Date date = new Date();
        date.setTime((long) startTimestamp*1000);
        startView.setText("Start: " + date.toString());
        int endTimestamp = intent.getIntExtra("endTimestamp", 0);
        date.setTime((long) endTimestamp*1000);
        endView.setText("End: " + date.toString());
        try {
            fetchElection(intent.getIntExtra("electionId", 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchElection(final int electionId) throws Exception {
        Request request = new Request.Builder()
                .url("https://votingtest.herokuapp.com/api/election/" + electionId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("hey", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    final List<Party> parties = new ArrayList<>();
                    JSONObject responseObj = new JSONObject(Objects.requireNonNull(Objects.requireNonNull(response.body()).string()));
                    JSONObject dataObj = responseObj.getJSONObject("parties");
                    Iterator<String> keys = dataObj.keys();

                    while (keys.hasNext()) {
                        JSONObject partyInfo = dataObj.getJSONObject(keys.next());
                        Party party = new Party(partyInfo.getInt("id"), electionId,
                                partyInfo.getString("name"), partyInfo.getString("logo"),
                                partyInfo.getString("slogan"), partyInfo.getString("chairman"),
                                partyInfo.getString("treasurer"), partyInfo.getString("sec_gen"));
                        parties.add(party);
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView partyListView = findViewById(R.id.partiesList);
                            partyListView.setLayoutManager(new LinearLayoutManager(ViewElectionActivity.this));
                            PartyAdapter adapter = new PartyAdapter(ViewElectionActivity.this, parties, electionId);
                            partyListView.setAdapter(adapter);
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