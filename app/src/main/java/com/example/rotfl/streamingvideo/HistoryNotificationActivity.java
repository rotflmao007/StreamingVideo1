package com.example.rotfl.streamingvideo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;


public class HistoryNotificationActivity extends Activity {

    Button klik;
    TextView ilosc;
    ProgressBar progressBar;
    Context context = this;
    int progressI = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_notification_layout);

            klik = (Button) findViewById(R.id.klik);
            ilosc = (TextView) findViewById(R.id.iloscWiad);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
//progressBar.setProgress
        klik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setMax(200);
                Messages m = new Messages();
                Thread getMessage = new Thread(m);
                getMessage.start();
                do {
                    try {
                        Thread.sleep(800);
                        progressBar.setProgress(progressI++);
                        //System.out.println(progressI++ + "wwwwwwwwwwwgfwwwwww");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (getMessage.isAlive());

            }
        });

    }


    private class Messages implements Runnable {
        //Zadaniem klasy jest utworzenie wątku, który będzie pobierał wiadomości email wysłane z kamery
        String temp = "";


        @Override
        public void run() {
            try {

                //TODO Zdefiniowanie protokołu (hosta poczty)
                Properties properties = new Properties();
                properties.setProperty("mail.store.protocol", "imaps");
                try {


                    //TODO Pobranie instancji sesji do czytania wiadomości e-mail
                    Session emailSession = Session.getInstance(properties, null);
                    //TODO dostęp do wiadoości za pomocją klasy Store
                    // Store class - klasa abstrakcyjna, w której modele wiadomości oraz
                    //protokół dostępu służący do przechowywania i pobierania wiadomości
                    Store store = emailSession.getStore();
                    store.connect("imap.gmail.com", "abbb4088@gmail.com", "czarek123");
                    //TODO odczytanie folderu z wiadomościami w skrzynce odbiorczej
//                    progressDialog.setMax();


                    Folder emailFolder = store.getFolder("INBOX");
                    emailFolder.open(Folder.READ_WRITE);
                    System.out.println("zzzzzzzzzzzz " + emailFolder.getMessageCount());


                    Message messages = emailFolder.getMessage(emailFolder.getMessageCount());


                    Address[] in = messages.getFrom();// zapisanie od kogo pochodzą wiadomości
                    for (Address address : in) {
                        System.out.println("FROM:" + address.toString());
                    }
                    String result = "";
                    Multipart mp = (Multipart) messages.getContent();
                    BodyPart bp = mp.getBodyPart(0);

                    for (int i = 1; i <= emailFolder.getMessageCount(); i++) {
                        messages = emailFolder.getMessage(i);
                        if (messages.getSubject().equals("Wykryto ruch")) {
                            System.out.println("SENT DATE:" + messages.getSentDate());
                            System.out.println("SUBJECT :" + i + "    " + messages.getSubject());
                            System.out.println("CONTENT:" + bp.getContent()
                                            + messages.getSubject() +
                                            messages.getFrom() + " from \n" +
                                            messages.getMessageNumber() + " messageNumber \n" +
                                            messages.getSentDate() + " setn date \n" +
                                            messages.getSession().toString() + "getsession"
                            );

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
//TODO ------------------------ POłącenie przy użyciu POP3
//                //create properties field
//                Properties properties = new Properties();
//
//                properties.put("mail.pop3.host", "pop.gmail.com");
//                properties.put("mail.pop3.port", "995");
//                properties.put("mail.pop3.starttls.enable", "true");
//                Session emailSession = Session.getDefaultInstance(properties);
//
//                //create the POP3 store object and connect with the pop server
//                Store store = emailSession.getStore("pop3s");
//
//                store.connect("pop.gmail.com",995, "abbb4088@gmail.com", "czarek123");
//
//                //create the folder object and open it
//                Folder emailFolder = store.getFolder("INBOX");
//                emailFolder.open(Folder.READ_ONLY);
//
//                // retrieve the messages from the folder in an array and print it
//                Message[] messages = emailFolder.getMessages();
//                System.out.println("messages.length---" + messages.length);
//
//                for (int i = 0, n = messages.length; i < n; i++) {
//                    Message message = messages[i];
//                    System.out.println("---------------------------------");
//                    System.out.println("Email Number " + (i + 1));
//                    System.out.println("Subject: " + message.getSubject());
//                    System.out.println("From: " + message.getFrom()[0]);
//                    System.out.println("Text: " + message.getContent().toString());
//
//                }
//
//                //close the store and folder objects
//                emailFolder.close(false);
//                store.close();
//
            } catch (Exception e) {
                e.printStackTrace();
            }
// catch (MessagingException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

//        public class progress extends
//    }

        //  private  class progressMessageP

    }
}

