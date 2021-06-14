package com.example.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import Utils.Acelerometro;

public class HomeMenuActivity extends AppCompatActivity {

    WebView miVisorWeb;
    //public static Activity home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //home = this;
        SensorManager a = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        Acelerometro speedWagon = new Acelerometro();
        speedWagon.setSensorManager(a);
        speedWagon.setShake(this.getApplicationContext());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_menu);
        miVisorWeb = (WebView) findViewById(R.id.visorWeb);
        final WebSettings ajustesVisorWeb = miVisorWeb.getSettings();
        ajustesVisorWeb.setJavaScriptEnabled(true);
        miVisorWeb.getSettings().setBuiltInZoomControls(true);
        miVisorWeb.getSettings().setUseWideViewPort(true);
        miVisorWeb.getSettings().setLoadWithOverviewMode(true);
        miVisorWeb.loadUrl("https://news.google.com/search?q=covid&hl=es-419&gl=AR&ceid=AR%3Aes-419");

        miVisorWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(miVisorWeb.canGoBack()){
            miVisorWeb.goBack();
        }else{
            super.onBackPressed();
        }
    }

}