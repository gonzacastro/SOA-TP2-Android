package com.example.tp2soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static javax.mail.internet.InternetAddress.*;

public class GmailAuthentication extends AppCompatActivity {
    private EditText inputGmail;
    private EditText inputCode;
    private Button authButton;
    private Button sendCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail_authentication);
        inputGmail = findViewById(R.id.editTextMail);
        inputCode = findViewById(R.id.editTextCode);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        sendCodeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String gmail = inputGmail.getText().toString();
                sendMail(gmail);

            }
        });
        authButton = findViewById(R.id.authButton);
        authButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });
    }

    private void sendMail(String mail){
        /*
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setDataAndType(Uri.parse("mailto:"),"text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mail); // * configurar email aquí!
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MyApp Authentication Code");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your code is: xd");

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email."));
            Toast.makeText(getApplicationContext(), "Enviando mail...", Toast.LENGTH_SHORT).show();
        }
        catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Error al enviar el mail", Toast.LENGTH_SHORT).show();
        }
        */
        EmailAPI a = new EmailAPI(mail);
        a.start();


    }

    /*
    private boolean sendMail2(String mail){

    }
    */
}