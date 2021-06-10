package com.example.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import APIs.RetrofitAPI;
import Models.APIResponse;
import Models.LoginRequest;
import Models.SessionInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText inputUsername;
    private  EditText inputPassword;
    private Button loginButton;
    private Button registerButton;
    private ProgressBar pb;

    static Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        thisActivity = this;
        loginButton = findViewById(R.id.loginButton);

        registerButton = findViewById(R.id.registerButton);

        inputUsername = findViewById(R.id.EditTextUserName);
        inputPassword = findViewById(R.id.EditTextPassword);
        Intent nombreSimpatico = getIntent();
        inputUsername.setText(nombreSimpatico.getExtras().getString("mail"));
        loginButton = findViewById(R.id.loginButton);
        pb = findViewById(R.id.progressBar3);
        pb.setVisibility(View.INVISIBLE);
        inputPassword.setText("unlam12345");
        inputUsername.setText("gonzacastro99@gmail.com");
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String username = getInputUsername();
                String password = getInputPassword();
                postData(username, password);
                pb.setVisibility(View.VISIBLE);
                registerButton.setEnabled(false);
                loginButton.setEnabled(false);
                inputPassword.setEnabled(false);
                inputUsername.setEnabled(false);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);

            }
        });
    }

    private  String getInputUsername(){
        return inputUsername.getText().toString();
    }

    public String getInputPassword(){
        return inputPassword.getText().toString();
    }

    private void postData(String email, String password) {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("http://so-unlam.net.ar/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI rfApi = rf.create(RetrofitAPI.class);
        LoginRequest lr = new LoginRequest(email, password);
        Call<APIResponse> call = rfApi.postLogin(lr);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                pb.setVisibility(View.INVISIBLE);
                if(response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_LONG).show();
                    APIResponse ar = response.body();
                    Log.e("response login", ar.toString());
                    SessionInfo.authToken = ar.getToken();
                    SessionInfo.refreshToken = ar.getToken_refresh();
                    Intent sig = new Intent(LoginActivity.this, HomeMenuActivity.class);
                    startActivity(sig);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Error de autenticacion", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                pb.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "Error en la peticion, intente mas tarde", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static Activity returnInstance() {
        return thisActivity;
    }

}