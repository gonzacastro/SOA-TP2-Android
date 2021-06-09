package com.example.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import APIs.RetrofitAPI;
import Models.APIResponse;
import Models.SessionInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeMenuActivity extends AppCompatActivity {

    Button botonPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);
        botonPrueba = findViewById(R.id.button);

        botonPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("token viejo", SessionInfo.authToken);
                HashMap<String, String> hm = new HashMap<>();
                hm.put("Content-Type", "application/json");
                hm.put("Authorization", "Bearer " + SessionInfo.refreshToken);
                putData(hm);
            }
        });
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