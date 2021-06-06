package com.example.tp2soa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.SendFailedException;

public class GmailAuthentication extends AppCompatActivity {
    private static final long MILLISTOSECONDS = 1000;
    private static final long SECONDSTIMEOUT = 1;
    private final String regExMail = "[\\w\\-.]*@[\\w]*(\\.[a-z]{2,3})+";

    private static EditText inputGmail;
    private static EditText inputCode;
    private Button sendCodeButton;
    private static Button authButton;
    private static long actualCode;

    private static int sendCodeDisabled = 0;
    private long startTime = 0;
    private long endTime = 0;

    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail_authentication);
        Button authButton = findViewById(R.id.authButton);
        EditText inputCode = findViewById(R.id.editTextCode);
        this.inputCode = inputCode;
        this.authButton = authButton;
        authButton.setEnabled(false);
        inputCode.setEnabled(false);
        authButton = findViewById(R.id.authButton);

        inputGmail = findViewById(R.id.editTextMail);

        h = new Handler();

        sendCodeButton = findViewById(R.id.sendCodeButton);
        sendCodeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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
                        startTime = System.currentTimeMillis()/MILLISTOSECONDS;
                        sendCodeDisabled=1;
                        actualCode = generateCode();
                        Log.e("code2:",String.valueOf(actualCode));
                        sendMail(gmail, actualCode);
                        Toast.makeText(getApplicationContext(), "Enviando mail...", Toast.LENGTH_SHORT).show();
                        //GmailAuthentication.authButton.setEnabled(true);
                        GmailAuthentication.inputCode.setEnabled(true);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Formato mail incorrecto", Toast.LENGTH_LONG).show();
                    GmailAuthentication.authButton.setEnabled(false);
                }
            }
        });

        authButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                long userCodeInput;
                Toast.makeText(getApplicationContext(), "Boton activado", Toast.LENGTH_SHORT).show();
                userCodeInput = Long.valueOf(inputCode.getText().toString());
                if(userCodeInput == actualCode){
                    Intent loginScreen = new Intent(GmailAuthentication.this,MainActivity.class);
                    loginScreen.putExtra("mail",GmailAuthentication.inputGmail.getText().toString());
                    startActivity(loginScreen);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "El codigo es incorrecto o caduco", Toast.LENGTH_SHORT).show();
                    inputCode.setText("");
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
            Log.e("Estado", "Empieza A");
            try {
                a.start();
                a.join();
                Log.e("Estado", "Ya espere a A");
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Estado", "Voy a activarlo");
                        authButton.setEnabled(true);

                    }
                });
            } catch (InterruptedException e) {
                Log.e("Estado", "Falle en algo");
                authButton.setEnabled(false);
                e.printStackTrace();
            }
        }
    }

    private long generateCode(){
        int min = 1;
        int max = 999999;
        long authCode = (System.currentTimeMillis() + ((long)Math.random()*(max-min+1)+min))%999999;
        Log.e("code:",String.valueOf(authCode));
        return authCode;
    }





    /*
    private boolean sendMail2(String mail){

    }
    */
}