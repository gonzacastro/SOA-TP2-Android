package com.example.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import APIs.RetrofitAPI;
import Models.APIResponse;
import Models.Acelerometro;
import Models.SessionInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeMenuActivity extends AppCompatActivity {

    Button botonIzq;
    Button botonDer;
    WebView miVisorWeb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Acelerometro speedWagon = new Acelerometro(HomeMenuActivity.this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_menu);
        miVisorWeb = (WebView) findViewById(R.id.visorWeb);
        final WebSettings ajustesVisorWeb = miVisorWeb.getSettings();
        ajustesVisorWeb.setJavaScriptEnabled(true);
        miVisorWeb.getSettings().setBuiltInZoomControls(true);
        miVisorWeb.getSettings().setUseWideViewPort(true);
        miVisorWeb.getSettings().setLoadWithOverviewMode(true);
        miVisorWeb.loadUrl("https://news.google.com/search?q=covid&hl=es-419&gl=AR&ceid=AR%3Aes-419");
        //botonDer = findViewById(R.id.botonDer);
        //botonIzq = findViewById(R.id.botonIzq);
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

    private void putData(Map<String, String> headers) {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("http://so-unlam.net.ar/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI rfApi = rf.create(RetrofitAPI.class);
        Call<APIResponse> call = rfApi.putRefreshToken(headers);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.code() == 200) {
                    SessionInfo.authToken = response.body().getToken();
                    Log.e("status", String.valueOf(response.body().isSuccess()));
                    Log.e("token nuevo", SessionInfo.authToken);
                } else {
                    Log.e("status", String.valueOf(response.body().isSuccess()));
                    Log.e("error", "a");
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.e("error", "b");
            }
        });
    }

}