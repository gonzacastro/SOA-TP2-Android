package com.example.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import APIs.RetrofitAPI;
import Models.APIResponse;
import Utils.Acelerometro;
import Models.EventRequest;
import Models.EventResponse;
import Models.LoginRequest;
import Utils.ConnectionController;
import Utils.SessionInfo;
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

    AlertDialog ad;
    AlertDialog alertLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SensorManager a = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        Acelerometro speedWagon = new Acelerometro();
        speedWagon.setSensorManager(a);
        speedWagon.setShake(this.getApplicationContext());
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
                if(ConnectionController.checkConnection(getApplicationContext())) {
                    String username = getInputUsername();
                    String password = getInputPassword();
                    postData(username, password);
                    pb.setVisibility(View.VISIBLE);
                    registerButton.setEnabled(false);
                    loginButton.setEnabled(false);
                    inputPassword.setEnabled(false);
                    inputUsername.setEnabled(false);
                } else {
                    ad = new AlertDialog.Builder(LoginActivity.this).setTitle("Error de conexión")
                            .setMessage("Verifique conexión a internet y vuelva a iniciar la aplicación")
                            .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                }
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
                    SharedPreferences sp = getSharedPreferences("log", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(getInputUsername(), sp.getInt(getInputUsername(), 0) + 1);
                    editor.commit();
                    int cant = sp.getInt(getInputUsername(),0);
                    APIResponse ar = response.body();
                    Log.e("response login", ar.toString());
                    SessionInfo.authToken = ar.getToken();
                    SessionInfo.refreshToken = ar.getToken_refresh();
                    postEv();
                    alertLog = new AlertDialog.Builder(LoginActivity.this).setTitle("Registro de actividad")
                            .setMessage("Usted se ha logueado " + cant + " veces en la aplicacion")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent sig = new Intent(LoginActivity.this, HomeMenuActivity.class);
                                    startActivity(sig);
                                    LoginActivity.returnInstance().finish();
                                    finish();
                                }
                            })
                            .show();
               } else {
                    pb.setVisibility(View.INVISIBLE);
                    registerButton.setEnabled(true);
                    loginButton.setEnabled(true);
                    inputPassword.setEnabled(true);
                    inputUsername.setEnabled(true);
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

    private void postEv() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + SessionInfo.authToken);
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("http://so-unlam.net.ar/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI rfApi = rf.create(RetrofitAPI.class);
        EventRequest er = new EventRequest("Login", "Se ha registrado un logueo a la aplicacion.");
        Call<EventResponse> call = rfApi.postEvent(headers, er);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                JSONObject json = null;
                if(response.code() == 201) {
                    Log.e("st", "SUCCESS");
                    Log.e("res", response.body().toString());
                } else {
                    Log.e("a", String.valueOf(response.code()));
                    Log.e("res", response.errorBody().toString());
                    try {
                        json = new JSONObject(response.errorBody().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("json", json.toString());
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Log.e("err", "no anda na");
            }
        });
    }

}