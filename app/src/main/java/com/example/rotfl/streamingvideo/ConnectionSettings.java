package com.example.rotfl.streamingvideo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by rotfl on 24.04.2016.
 * Klasa Edycji (zakładka Connection) edytuje i uzupełni apola ustawień -
 */
public class ConnectionSettings extends Activity {

    EditText nameCamera;
    EditText rtspAdress;
    EditText login;
    EditText password;
    EditText port;
    EditText portHttp;

    Button update;

    String idCamera;

    SQLiteDatabase cameraInfo;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        cameraInfo = openOrCreateDatabase("cameraInfo",MODE_PRIVATE, null);//stworzenie lubotworzenie lokalnej bazy danych


        nameCamera = (EditText) findViewById(R.id.nameCamera);
        rtspAdress = (EditText) findViewById(R.id.rtspAdress);
        login      = (EditText) findViewById(R.id.login);
        password   = (EditText) findViewById(R.id.password);
        port       = (EditText) findViewById(R.id.port);
        portHttp   = (EditText) findViewById(R.id.portHTTP);

        update     = (Button) findViewById(R.id.update);


        Intent i = getIntent();
        Bundle pobierzDane = i.getExtras();
        idCamera = pobierzDane.getString("idCamera");
        nameCamera.setText(pobierzDane.getString("nameCamera"));
        rtspAdress.setText(pobierzDane.getString("rstpAdress"));
        login.setText(pobierzDane.getString("login"));
        password.setText(pobierzDane.getString("password"));
        port.setText(pobierzDane.getString("port"));
        portHttp.setText(pobierzDane.getString("portHTTP"));


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void update(){
        final Intent inetnt = new Intent(getApplicationContext(), MainActivity.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder .setMessage("Czy chcesz edytować wpis?")
                .setTitle("Edytuj");

        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (rtspAdress.getText().toString().indexOf("/") >= 0) {
                    try {
                        cameraInfo.execSQL("UPDATE cameraSettings SET " +
                                        "nameCamera = '" + nameCamera.getText() + "'," +
                                        "rtspAdress = '" + rtspAdress.getText() + "'," +
                                        "login = '" + login.getText() + "'," +
                                        "password = '" + password.getText() + "'," +
                                        "port = " + port.getText() + ", " +
                                        "portHTTP = " + portHttp.getText() + " " +
                                        "where id = " + idCamera
                        );
                        finish();
                        startActivity(inetnt);
                    } catch (Exception e) {
                        System.out.println("wwwwwwwwww " + e);
                    }
                } else if(rtspAdress.getText().toString().indexOf("/") < 0){
                    AlertDialog.Builder infoDialog = new AlertDialog.Builder(context);

                    infoDialog.setMessage("Adres RTSP musi mieć składnię: \n" +
                            "adres_ip/plik.rozszerzenie \n" +
                            "Przykład: \n" +
                            "192.168.0.1\\video.3gp \n")
                    .setTitle("Informacja");
                    infoDialog.show();
                }

            }
        })
        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialgo, int id) {
                finish();
                startActivity(inetnt);
            }
        });
        builder.show();
    }
}
