package com.apps.ramin.android_simple_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ramin on 07/06/2017.
 */

public class GridAlbumActivity extends Activity  {

    int albumId;
    GridView gridView;
    ArrayList<Bitmap> thumbs;
    ArrayList<String> urls;

    Context mCtx;
    private RequestQueue mQueue;
    JSONArray json_arr;
    String photos_url;
    {
        photos_url = "https://jsonplaceholder.typicode.com/photos";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String s = getIntent().getStringExtra("albumId");
        albumId = Integer.parseInt(s);
        albumId++;
        try {
            mQueue = VolleyRequestQueue.getInstance(this.getApplicationContext())
                    .getRequestQueue();
            mCtx = this;

            final VolleyRequest stringRequest = new VolleyRequest(Request.Method.GET, photos_url + "/?albumId=" + albumId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        json_arr = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    thumbs = new ArrayList<>();
                    urls = new ArrayList<>();
                    Log.v("Volley","Number of Images" + json_arr.length());
                    for(int i = 0; i < json_arr.length(); i++){
                        try {
                            JSONObject json_obj = json_arr.getJSONObject(i);
                            String URLImage = json_obj.get("thumbnailUrl").toString();
                            urls.add(json_obj.get("url").toString());
                            ImageRequest ir = new ImageRequest(URLImage, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    thumbs.add(response);
                                    gridView.invalidateViews();
                                }
                            }, 0, 0, null, null);
                            ir.setTag("GridAlbumActivity");
                            mQueue.add(ir);
                            // Log.v("Volley","Sent Image Request.............................");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    setContentView(R.layout.grid_main);

                    gridView = (GridView) findViewById(R.id.gridView1);
                    GridViewAdapter customGridAdapter = new GridViewAdapter(mCtx, R.layout.grid_element, thumbs);
                    gridView.setAdapter(customGridAdapter);

                    gridView.invalidateViews();
                    gridView.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            Intent intent = new Intent(getBaseContext(), PhotoActivity.class);
                            intent.putExtra("url", urls.get(position));
                            startActivity(intent);
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            stringRequest.setTag("GridAlbumActivity");
            mQueue.add(stringRequest);
            Log.v("Volley","Sent Request.............................");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
