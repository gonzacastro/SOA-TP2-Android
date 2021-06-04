package com.example.tp2soa;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailAPI extends Thread {
    private String mail;
    public EmailAPI(String mail) {
        this.mail = mail;
    }

    public void run() {
        String sender = "coltaconsulting@gmail.com";
        String pass = "Mom,Ipuked00";

        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.googlemail.com");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.port", "465");

        Session s;
        try {
            s = Session.getDefaultInstance(p, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender, pass);
                }
            });
            if(s != null) {
                Message m = new MimeMessage(s);
                m.setFrom(new InternetAddress(sender));
                m.setSubject("Prueba SOA");
                m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
                m.setContent("me anduvo, ahora si a dormir :)", "text/plain");

                Transport.send(m);

            }
        } catch(Exception e) {
            Log.e("error", e.toString());
        }
    }
}
