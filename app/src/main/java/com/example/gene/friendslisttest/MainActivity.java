package com.example.gene.friendslisttest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ArrayList<FriendModel> friends = new ArrayList<>();
    private SwipeRefreshLayout SRL;

    private String URL = "https://randomuser.me/api/?page=1&results=30&seed=testApp";

   private static Comparator<FriendModel> alphabeticalComparator = new Comparator<FriendModel>() {
       @Override
       public int compare(FriendModel friendModel, FriendModel t1) {
           String str1 = friendModel.name,
                   str2 = t1.name;

           int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
           return (res != 0) ? res : str1.compareTo(str2);
       }
   };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SRL = findViewById(R.id.swipeGesture);
        SRL.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener(){
                    @Override
                    public void onRefresh() { switchCacheData(); }
                }
        );

        switchCacheData();
    }

    private void switchCacheData () {
        if(Cacher.readCache(URL, getBaseContext()).length() > 1 && !isInternetAvailable() ) {
            parseJSON(Cacher.readCache(URL, getBaseContext()));
        } else {
            new GetJSON().execute(URL);
        }

        setDisplay();
    }

    private void setDisplay () {
        UsersAdapter adapter = new UsersAdapter(getBaseContext(), friends);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    private void parseJSON(String arg) {
        String jsonStr = arg;
        JSONObject jsonObj;

        SRL.setRefreshing(false);
        friends.clear();

        try{
            jsonObj = new JSONObject(jsonStr);
            JSONArray friendsData = jsonObj.getJSONArray("results");

            for (int i = 0; i < friendsData.length(); i++) {
                JSONObject f = friendsData.getJSONObject(i);

                FriendModel tempFM = new FriendModel(f.getJSONObject("name").getString("first") + " " +
                        f.getJSONObject("name").getString("last"),
                        f.getString("email"),
                        f.getString("dob"),
                        f.getJSONObject("picture").getString("thumbnail"),
                        f.getJSONObject("picture").getString("large"));

                friends.add(tempFM);
            }

            Log.d("cacheLog", " " + friends.size());

        } catch (Exception e) { }

        Collections.sort(friends, alphabeticalComparator);
    }

    private class GetJSON extends AsyncTask<String, Void, String> {

        private boolean isSucces = false;
        private StringBuilder dta = new StringBuilder();

        private URL url;

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);

                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                }
                isSucces = true;

            } catch (Exception e) { isSucces = false; }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            parseJSON(dta.toString());

            Cacher.writeCache(dta.toString(),getBaseContext(),url.getPath());
        }
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}