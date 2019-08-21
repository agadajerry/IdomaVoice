package com.jerrysoft;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  ImageView image = (ImageView) findViewById(R.id.imageView);
        new Handler().postDelayed(new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, IdomaVoiceHome.class);
                startActivity(intent);
                finish();
            }
        }, 1600);
    }
}
