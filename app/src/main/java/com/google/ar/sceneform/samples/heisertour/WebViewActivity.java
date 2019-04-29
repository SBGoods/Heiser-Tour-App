package com.google.ar.sceneform.samples.heisertour;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {


    WebView mWebView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        String sName = getIntent().getStringExtra("NAME");

        mWebView = findViewById(R.id.webview);

        switch (sName){
            case "Carson.jpg":
                mWebView.loadUrl("https://youtu.be/SeJNRaE11A0");
                break;

            case "Curie.jpg":
                mWebView.loadUrl("https://youtu.be/ZEV4KJBJvEg");
                break;

            case "Earle.jpg":
                mWebView.loadUrl("https://youtu.be/VecmIbBjyIo");
                break;
        }




    }



}
