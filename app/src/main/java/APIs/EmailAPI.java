package APIs;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailAPI extends Thread {
    private String gmail;
    private long code;
    private String mailHeader = " Appname Authentication Code";
    private String mailBody = "Your code is:\n  ";
    private Context c;
    public EmailAPI(String mail,long code, Context c) {
        this.gmail = mail;
        this.code = code;
        this.mailBody += code;
        this.c = c;
    }

    public void run() {
        String sender = "tp2soa@gmail.com";
        String pass = "unlam12345";

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
                m.setSubject(mailHeader);
                m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(gmail));
                m.setContent(mailBody, "text/plain");

                Transport.send(m);
                //Looper.prepare();
                //Toast.makeText(this.c, "Enviado.", Toast.LENGTH_LONG).show();
                //Looper.loop();
                Log.e("Estado", "Termine A");
            }
        } catch(MessagingException e) {
            Log.e("error", e.toString());
            Looper.prepare();
            Toast.makeText(this.c, "Error: mail inexistente", Toast.LENGTH_LONG).show();
            Looper.loop();
        }
    }
}
