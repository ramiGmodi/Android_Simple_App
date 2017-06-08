package com.apps.ramin.android_simple_app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

/**
 * Created by Ramin on 07/06/2017.
 */

public class PhotoActivity extends Activity {
    String photoURL;

    Context mCtx;
    private RequestQueue mQueue;

    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        image = (ImageView) findViewById(R.id.imageView1);
        photoURL = getIntent().getStringExtra("url");
        try {
            mQueue = VolleyRequestQueue.getInstance(this.getApplicationContext())
                    .getRequestQueue();
            mCtx = this;

            ImageRequest ir = new ImageRequest(photoURL, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    image.setImageBitmap(response);
                    image.invalidate();
                }
            }, 0, 0, null, null);
            ir.setTag("GridAlbumActivity");
            mQueue.add(ir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
