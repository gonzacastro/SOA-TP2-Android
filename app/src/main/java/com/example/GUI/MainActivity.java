package com.example.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText inputUsername;
    private  EditText inputPassword;
    private static Button loginButton;
    private Button registerButton;
//todas putas
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = findViewById(R.id.loginButton);
        this.loginButton  = loginButton;

        registerButton = findViewById(R.id.registerButton);

        inputUsername = findViewById(R.id.EditTextUserName);
        inputPassword = findViewById(R.id.EditTextPassword);
        Intent nombreSimpatico = getIntent();
        inputUsername.setText(nombreSimpatico.getExtras().getString("mail"));
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String username = getInputUsername();
                String password = getInputPassword();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
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

}