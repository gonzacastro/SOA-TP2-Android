package com.example.GUI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

import APIs.RetrofitAPI;
import Models.APIResponse;
import Utils.Acelerometro;
import Utils.SessionInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeMenuActivity extends AppCompatActivity {

    WebView miVisorWeb;
    Handler hand;
    //public static Activity home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //home = this;
        hand = new Handler();
        SensorManager a = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        Acelerometro speedWagon = new Acelerometro();
        speedWagon.setSensorManager(a);
        speedWagon.setShake(this.getApplicationContext());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_menu);

        Runnable crono = new Cronometro(this.hand);
        Thread t = new Thread(crono);
        t.start();

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

    class Cronometro implements Runnable {

        Handler h;

        public Cronometro(Handler h) {
            this.h = h;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(420000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            h.post(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(HomeMenuActivity.this)
                            .setTitle("Sesion expirada")
                            .setMessage("Para continuar navegando en la aplicacion es necesario refrescar el token")
                            .setPositiveButton("Refrescar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e("token ._.XD", SessionInfo.authToken);
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("Content-Type", "application/json");
                                    hm.put("Authorization", "Bearer " + SessionInfo.refreshToken);
                                    putData(hm);
                                    Runnable r = new Cronometro(h);
                                    Thread t = new Thread(r);
                                    t.start();
                                }
                            })
                            .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();

                }
            });
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
                    //Log.e("status", String.valueOf(response.body().isSuccess()));
                    //Log.e("token nuevo", SessionInfo.authToken);
                } else {
                    //Log.e("status", String.valueOf(response.body().isSuccess()));
                    //Log.e("error", "a");
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.e("error", "b");
            }
        });
    }

}