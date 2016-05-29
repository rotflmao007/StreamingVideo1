package com.example.rotfl.streamingvideo;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by rotfl on 06.05.2016.
 * Klasa ta decyduje jaki ekran i ile tabelek ma się wyswietlić na następnym ekranie
 */
public class SettingActivity extends TabActivity {

    String opcja = "dodaj";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabHost tabHost = getTabHost();

        Intent i = getIntent();
        Bundle pobierzDane = i.getExtras();
        opcja = pobierzDane.getString("opcja");

        if (opcja.equals("dodaj")) {
            tabHost.addTab(tabHost.newTabSpec("first")
                    .setIndicator("Connection")
                    .setContent(new Intent(this, SettingsCameraActivity.class)
                    ));
            tabHost.addTab(tabHost.newTabSpec("seckond")
                    .setIndicator("Notification")
                    .setContent(new Intent(this, HistoryNotificationActivity.class)
                    ));
        }else if(opcja.equals("edytuj")){
            tabHost.addTab(tabHost.newTabSpec("third")
                    .setIndicator("Connection")
                    .setContent(new Intent(this, ConnectionSettings.class)
                            .putExtra("idCamera", pobierzDane.getString("idCamera"))
                            .putExtra("nameCamera", pobierzDane.getString("nameCamera"))
                            .putExtra("rstpAdress", pobierzDane.getString("rstpAdress"))
                            .putExtra("login", pobierzDane.getString("login"))
                            .putExtra("password", pobierzDane.getString("password"))
                            .putExtra("port", pobierzDane.getString("port"))
                            .putExtra("portHTTP", pobierzDane.getString("portHTTP"))
            ));
            tabHost.addTab(tabHost.newTabSpec("fourth")
                    .setIndicator("IMG Setting")
                    .setContent(new Intent(this, SettingImageActivity.class)
                            .putExtra("idCamera",pobierzDane.getString("idCamera"))
                            .putExtra("rstpAdress", pobierzDane.getString("rstpAdress"))
                            .putExtra("login", pobierzDane.getString("login"))
                            .putExtra("password", pobierzDane.getString("password"))
                            .putExtra("port", pobierzDane.getString("port"))
                    ));
            tabHost.addTab(tabHost.newTabSpec("fifth")
                    .setIndicator("Notification")
                    .setContent(new Intent(this, HistoryNotificationActivity.class)
                    ));
        }


        tabHost.setCurrentTab(0);

    }
}
