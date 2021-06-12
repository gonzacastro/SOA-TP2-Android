package com.example.GUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import APIs.RetrofitAPI;
import Models.APIResponse;
import Models.RegistroRequest;
import Utils.ConnectionController;
import Utils.SessionInfo;
import Utils.VerificadorCamposRegistro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    EditText nombreET;
    EditText apellidoET;
    EditText dniET;
    EditText emailET;
    EditText passwordET;
    EditText comisionET;
    EditText grupoET;
    Button boton;
    ProgressBar progressBar;

    AlertDialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombreET = findViewById(R.id.nombre);
        apellidoET = findViewById(R.id.apellido);
        dniET = findViewById(R.id.dni);
        emailET = findViewById(R.id.mail);
        passwordET = findViewById(R.id.password);
        comisionET = findViewById(R.id.comision);
        grupoET = findViewById(R.id.grupo);
        boton = findViewById(R.id.botonListo);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        /*
        nombreET.setText("Gonzalo");
        apellidoET.setText("Castro");
        dniET.setText("41639230");
        emailET.setText("gonzacastro99@gmail.com");
        passwordET.setText("unlam12345");
        grupoET.setText("1");
        comisionET.setText("2900");


         */
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConnectionController.checkConnection(getApplicationContext())) {
                    VerificadorCamposRegistro verificador = new VerificadorCamposRegistro(nombreET, apellidoET, dniET, emailET, passwordET, comisionET, grupoET);
                    if (verificador.verificarCampos()) {
                        postData(
                                nombreET.getText().toString(),
                                apellidoET.getText().toString(),
                                dniET.getText().toString(),
                                emailET.getText().toString(),
                                passwordET.getText().toString(),
                                comisionET.getText().toString(),
                                grupoET.getText().toString()
                        );
                        progressBar.setVisibility(View.VISIBLE);
                        nombreET.setEnabled(false);
                        apellidoET.setEnabled(false);
                        dniET.setEnabled(false);
                        emailET.setEnabled(false);
                        passwordET.setEnabled(false);
                        comisionET.setEnabled(false);
                        grupoET.setEnabled(false);
                        boton.setEnabled(false);
                    }
                } else {
                    ad = new AlertDialog.Builder(RegisterActivity.this).setTitle("Error de conexión")
                            .setMessage("Verifique conexión a internet y vuelva a iniciar la aplicación")
                            .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    LoginActivity.returnInstance().finish();
                                }
                            }).show();
                }
            }
        });

    }

    private void postData(String name, String apellido, String dni, String email, String password, String comision, String grupo) {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("http://so-unlam.net.ar/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI rfApi = rf.create(RetrofitAPI.class);
        RegistroRequest dr = new RegistroRequest(name, apellido, Integer.valueOf(dni), email, password, Integer.valueOf(comision), Integer.valueOf(grupo));
        Call<APIResponse> call = rfApi.postRegister(dr);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                Log.e("res", response.toString());
                APIResponse reg = response.body();
                int code = response.code();
                if(code == 200) {
                    Toast.makeText(RegisterActivity.this, "Registro completado", Toast.LENGTH_LONG).show();
                    Log.e("code", String.valueOf(code));
                    Log.e("res", reg.toString());

                    SessionInfo.authToken = reg.getToken();
                    SessionInfo.refreshToken = reg.getToken_refresh();
                } else {

                    Toast.makeText(RegisterActivity.this, "Error, revise los campos", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
                nombreET.setEnabled(true);
                apellidoET.setEnabled(true);
                dniET.setEnabled(true);
                emailET.setEnabled(true);
                passwordET.setEnabled(true);
                comisionET.setEnabled(true);
                grupoET.setEnabled(true);
                boton.setEnabled(true);

                LoginActivity.returnInstance().finish();
                Intent sig = new Intent(RegisterActivity.this, HomeMenuActivity.class);
                startActivity(sig);
                finish();
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                Log.e("err", t.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
                nombreET.setEnabled(true);
                apellidoET.setEnabled(true);
                dniET.setEnabled(true);
                emailET.setEnabled(true);
                passwordET.setEnabled(true);
                comisionET.setEnabled(true);
                grupoET.setEnabled(true);
            }
        });
    }

}