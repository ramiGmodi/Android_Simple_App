package com.apps.ramin.android_simple_app;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class MainActivity extends ListActivity {

    Context mCtx;
    private RequestQueue mQueue;
    JSONArray json_arr;
    String albums_url, photos_url;
    {
        albums_url = "https://jsonplaceholder.typicode.com/albums";
        photos_url = "https://jsonplaceholder.typicode.com/photos";
    }

    ArrayList<String> ALBUMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            mQueue = VolleyRequestQueue.getInstance(this.getApplicationContext())
                    .getRequestQueue();
            mCtx = this;
            final VolleyRequest stringRequest = new VolleyRequest(Request.Method.GET, albums_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        json_arr = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ALBUMS = new ArrayList<>();
                    for(int i = 0; i < json_arr.length(); i++){
                        try {
                            JSONObject json_obj = json_arr.getJSONObject(i);
                            ALBUMS.add(json_obj.get("title").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    setListAdapter(new ArrayAdapter<String>(mCtx, R.layout.activity_main,ALBUMS));

                    ListView listView = getListView();
                    listView.setTextFilterEnabled(true);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            // When clicked, show a toast with the TextView text
                            //Toast.makeText(getApplicationContext(),
                            //        ((TextView) view).getText() + " at " + position, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(), GridAlbumActivity.class);
                            intent.putExtra("albumId", "" + position);
                            startActivity(intent);
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            stringRequest.setTag("MainActivity");
            mQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
