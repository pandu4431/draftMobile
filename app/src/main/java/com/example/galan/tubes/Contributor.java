package com.example.galan.tubes;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.galan.tubes.adapterView.RecyclerContributor;
import com.example.galan.tubes.setter_getter.isiContributor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Contributor extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_pengumuman);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        doLoad();

    }

    public void doLoad() {
        new BackgroundCont(Contributor.this).execute();
    }


    class BackgroundCont extends AsyncTask<Void, isiContributor, Void> {

        private Context ctx;
        private Activity activity;
        private RecyclerView recyclerView;
        private RecyclerView.Adapter adapter;
        private RecyclerView.LayoutManager layoutManager;
        private ArrayList<isiContributor> arrayList = new ArrayList<>();
        private String URLdata = "http://pandumalik.esy.es/UserRegistration/contributor.php?type=dosen";

        public BackgroundCont(Context ctx) {
            this.ctx = ctx;
            activity = (Activity) ctx;
        }

        @Override
        protected void onPreExecute() {
            layoutManager = new LinearLayoutManager(ctx);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new RecyclerContributor(arrayList);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onProgressUpdate(isiContributor... values) {
            arrayList.add(values[0]);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(URLdata);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                httpURLConnection.disconnect();
                String json_string = stringBuilder.toString().trim();

                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.optJSONArray("server_response");
                int count = 0;
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    count++;
                    isiContributor cont = new isiContributor(JO.getString("name"), JO.getString("contact"), JO.getString("photos"), JO.getString("cover"));
                    publishProgress(cont);
                }
                Log.d("JSON STRING", json_string);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
