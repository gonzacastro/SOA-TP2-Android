package com.example.tp2soa;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GmailAuthentication extends AppCompatActivity {
    private static final long MILLISTOSECONDS = 1000;
    private static final long SECONDSTIMEOUT = 1;

    private EditText inputGmail;
    private EditText inputCode;
    private Button sendCodeButton;
    private static Button authButton;

    private static int sendCodeDisabled = 0;
    private long startTime = 0;
    private long endTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail_authentication);
        Button authButton = findViewById(R.id.authButton);
        this.authButton = authButton;
        authButton.setEnabled(false);
        authButton = findViewById(R.id.authButton);

        inputGmail = findViewById(R.id.editTextMail);

        inputCode = findViewById(R.id.editTextCode);

        sendCodeButton = findViewById(R.id.sendCodeButton);
        sendCodeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String gmail = inputGmail.getText().toString();
                long actualCode;

                //Toast.makeText(getApplicationContext(), "Enviando mail", Toast.LENGTH_SHORT).show();

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
                    GmailAuthentication.authButton.setEnabled(true);
                }
            }
        });

        authButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Boton activado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMail(String gmail, long code){
        EmailAPI a = new EmailAPI(gmail,code);
        a.start();


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