package com.example.rotfl.streamingvideo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by rotfl on 13.04.2016.
 * Klasa odpowiada za dodawanie kamery do bazy sqlite
 */
public class SettingsCameraActivity extends Activity {

    EditText nameCamera;
    EditText rtspAdress;
    EditText login;
    EditText password;
    EditText port;
    EditText portHttp;
    Button save;

    int i=0;



    SQLiteDatabase cameraInfo;
    Cursor coursor;
    Context context = this;

    int columnCount;
    int licznikColumnCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_camera);

        nameCamera = (EditText) findViewById(R.id.nameCamera);
        rtspAdress = (EditText) findViewById(R.id.rtspAdress);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        port = (EditText) findViewById(R.id.port);
        save = (Button) findViewById(R.id.save);
        portHttp = (EditText) findViewById(R.id.portHTTP);

        cameraInfo = openOrCreateDatabase("cameraInfo", MODE_PRIVATE, null);//stworzenie lokalnej bazy danych
        coursor = cameraInfo.rawQuery("Select * from cameraSettings", null);



    save.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                dodajKamere();
        }
    });



    }


    public void dodajKamere(){
               String a =
                        "INSERT INTO cameraSettings('nameCamera','rtspAdress','login','password','port','portHTTP','brightness','contrast','saturation','hue') VALUES" +
                                "(" +
                                "'" + nameCamera.getText() + "'," +
                                "'" + rtspAdress.getText() + "'," +
                                "'" + login.getText() + "'," +
                                "'" + password.getText() + "'," +
                                "" + port.getText() + "," +
                                "" + portHttp.getText() + "," +
                                "70," +
                                "35," +
                                "12," +
                                "13" +
                                "); ";
        System.out.println("qqqqqqqqqqqqqqqqqqqqqqqq" + a);
                if(rtspAdress.getText().toString().indexOf("/") >= 0) { //sprawdzenie czy w adresie podano rozszerzenie
                    try {
                        cameraInfo.execSQL(a);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } catch (Exception e) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setMessage("Wszystkie pola muszą być wypełnione" + e);
                        dialog.show();
                    }
                }else if(rtspAdress.getText().toString().indexOf("/") < 0){
                    AlertDialog.Builder infoDialog = new AlertDialog.Builder(context);

                    infoDialog.setMessage("Adres RTSP musi mieć składnię: \n" +
                            "adres_ip/plik.rozszerzenie \n" +
                            "Przykład: \n" +
                            "192.168.0.1\\video.3gp \n")
                            .setTitle("Informacja");
                    infoDialog.show();
                }
    }




}
