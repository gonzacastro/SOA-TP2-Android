package com.example.tp2soa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
        });

    }

    private void postData(String name, String apellido, String dni, String email, String password, String comision, String grupo) {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("http://so-unlam.net.ar/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI rfApi = rf.create(RetrofitAPI.class);
        DatoRegistro dr = new DatoRegistro(name, apellido, Integer.valueOf(dni), email, password, Integer.valueOf(comision), Integer.valueOf(grupo));
        Call<RegistroResponse> call = rfApi.post(dr);
        call.enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                Toast.makeText(RegisterActivity.this, "Registro completado", Toast.LENGTH_LONG).show();
                Log.e("res", response.toString());
                RegistroResponse reg = response.body();
                int code = response.code();
                Log.e("code", String.valueOf(code));
                Log.e("res", reg.toString());
                progressBar.setVisibility(View.INVISIBLE);
                nombreET.setEnabled(true);
                apellidoET.setEnabled(true);
                dniET.setEnabled(true);
                emailET.setEnabled(true);
                passwordET.setEnabled(true);
                comisionET.setEnabled(true);
                grupoET.setEnabled(true);
                boton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
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