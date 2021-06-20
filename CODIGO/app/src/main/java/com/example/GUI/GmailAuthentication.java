package com.example.GUI;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import APIs.EmailAPI;
import Utils.Acelerometro;
import Utils.ConnectionController;

public class GmailAuthentication extends AppCompatActivity {
    private static final long MILLISTOSECONDS = 1000;
    private static final long SECONDSTIMEOUT = 1;
    private final String regExMail = "[\\w\\-.]*@[\\w]*(\\.[a-zA-Z]{2,3})+";

    private static EditText inputGmail;
    private static EditText inputCode;
    private Button sendCodeButton;
    private static Button authButton;
    private static long actualCode;
    private static float percentage;

    private static int sendCodeDisabled = 0;
    private long startTime = 0;
    private long endTime = 0;

    private ProgressBar pb;

    Handler h;

    AlertDialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail_authentication);

        SensorManager a = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        Acelerometro speedWagon = new Acelerometro();
        speedWagon.setSensorManager(a);
        speedWagon.setShake(this.getApplicationContext());

        BroadcastReceiver batteryInfo = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) { }
        };

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null,iFilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

        GmailAuthentication.percentage = level *100 /(float) scale;

        new AlertDialog.Builder(this).setTitle("Nivel de bateria actual").setMessage(""+(int)GmailAuthentication.percentage + "%\nConectado: " + ConnectionController.checkConnection(getApplicationContext()) +"\nAgita el movil en cualquier parte de la app para salir\nEn la pantalla de noticias acerque algo para ver consejos relacionados con la pandemia")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
        Button authButton = findViewById(R.id.authButton);
        EditText inputCode = findViewById(R.id.editTextCode);
        this.inputCode = inputCode;
        this.authButton = authButton;
        authButton.setEnabled(false);
        inputCode.setEnabled(false);
        inputGmail = findViewById(R.id.editTextMail);
        pb = findViewById(R.id.progressBar2);
        pb.setVisibility(View.INVISIBLE);
        h = new Handler();
        sendCodeButton = findViewById(R.id.sendCodeButton);
        sendCodeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ConnectionController.checkConnection(getApplicationContext())) {
                    String gmail = inputGmail.getText().toString();
                    Pattern patron = Pattern.compile(regExMail);
                    Matcher m = patron.matcher(gmail);
                    if(m.find()) {
                        if (sendCodeDisabled != 0) {
                            endTime = System.currentTimeMillis()/MILLISTOSECONDS;
                            long secondsRunning = endTime - startTime;
                            if( (secondsRunning) < SECONDSTIMEOUT){
                                Toast.makeText(getApplicationContext(), "Tiene que esperar " + (SECONDSTIMEOUT-secondsRunning) + " segundos", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                sendCodeDisabled = 0;
                            }
                        }
                        if(sendCodeDisabled == 0){
                            pb.setVisibility(View.VISIBLE);
                            startTime = System.currentTimeMillis()/MILLISTOSECONDS;
                            sendCodeDisabled=1;
                            actualCode = generateCode();
                            sendMail(gmail, actualCode);
                            Toast.makeText(getApplicationContext(), "Enviando mail...", Toast.LENGTH_SHORT).show();
                            GmailAuthentication.inputCode.setEnabled(true);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Formato mail incorrecto", Toast.LENGTH_LONG).show();
                        GmailAuthentication.authButton.setEnabled(false);
                    }
                } else {
                    ad = new AlertDialog.Builder(GmailAuthentication.this).setTitle("Error de conexión")
                            .setMessage("Verifique conexión a internet y vuelva a iniciar la aplicación")
                            .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                }
            }
        });

        authButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                long userCodeInput;
                if(inputCode.getText().toString().length() != 0) {
                    userCodeInput = Long.valueOf(inputCode.getText().toString());
                    if(userCodeInput == actualCode){
                        Intent loginScreen = new Intent(GmailAuthentication.this, LoginActivity.class);
                        loginScreen.putExtra("mail",GmailAuthentication.inputGmail.getText().toString());
                        startActivity(loginScreen);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "El codigo es incorrecto o caduco", Toast.LENGTH_LONG).show();
                        inputCode.setText("");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Campo vacio", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void sendMail(String gmail, long code){
        EmailAPI a = new EmailAPI(gmail,code, getApplicationContext());
        Runnable at = new AuxThread(a, h);
        Thread t = new Thread(at);
        t.start();
    }

    class AuxThread implements Runnable {
        EmailAPI a;
        Handler h;
        public AuxThread(EmailAPI a, Handler h) {
            this.a = a;
            this.h = h;
        }
        @Override
        public void run() {
            try {
                a.start();
                a.join();
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Mail enviado", Toast.LENGTH_SHORT).show();
                        authButton.setEnabled(true);
                        pb.setVisibility(View.INVISIBLE);
                    }
                });
            } catch (InterruptedException e) {
                authButton.setEnabled(false);
                pb.setVisibility(View.INVISIBLE);
                e.printStackTrace();
            }

        }
    }

    private long generateCode(){
        int min = 1;
        int max = 999999;
        long authCode = (System.currentTimeMillis() + ((long)Math.random()*(max-min+1)+min))%999999;
        return authCode;
    }

}