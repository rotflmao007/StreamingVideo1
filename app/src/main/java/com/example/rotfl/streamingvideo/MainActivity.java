package com.example.rotfl.streamingvideo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/*
* Główny ekran po uruchomieniu aplikacji
* */
public class MainActivity extends AppCompatActivity {

    ListView cameraList;


    int i = 0;


    AlertDialog.Builder alertDialog;
    Context context = this;
    SQLiteDatabase cameraInfo;
    Cursor coursor;
    ArrayList<DataClass> dataClasses = new ArrayList<>();
    ArrayList<String> dataClassesString = new ArrayList<>();

    ArrayList<String> menuArrayList = new ArrayList<>();    //lista z elementami opcji menu
    CharSequence[] cameraMenuList = new CharSequence[3];    // sluzy do wyswietlenia listy na ekranie


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraInfo = openOrCreateDatabase("cameraInfo", MODE_PRIVATE, null);//stworzenie lub otworzenie lokalnej bazy danych

        cameraList  = (ListView) findViewById(R.id.cameraList);

        alertDialog = new AlertDialog.Builder(context);



    // TODO******************* Dodanie opcji do menu kamer - wyswietlane po ***************************
    // TODO******************** długim przytrzymaniu odpowiedniej kamery ******************************
        menuArrayList.add("Edytuj");
        menuArrayList.add("Usuń");
        cameraMenuList = menuArrayList.toArray(new CharSequence[menuArrayList.size()]);
    //TODO****************************************************************************************


        createTableOrDatabase();    // tworzy tabelę i lokalą bazę danych jeśli nie została wcześniej stworzona
        getAllCamera();             //pobiera i zapisuje wszystkie kamery zapisane w bazie sqlite - pewnie da się to poprawić jakoś

    //TODO**************** Zmiana ArrayList na ArayAdapter na potrzeby ****************************
    //TODO********************* dodania elementów do ListView *************************************
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dataClassesString
        );
        cameraList.setAdapter(arrayAdapter);    // wpisanie elementów do list view
    //TODO*****************************************************************************************
    //TODO************* Pobranie pozycji po wybraniu elementu z listy i przejscie do video*********
        cameraList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), VideoActivity.class);
                i.putExtra("idCamera", dataClasses.get(position).getIdCamera());
                i.putExtra("rtspAdress", dataClasses.get(position).getRtspAdress());
                i.putExtra("login", dataClasses.get(position).getLogin());
                i.putExtra("password", dataClasses.get(position).getPassword());
                i.putExtra("port", dataClasses.get(position).getPort());

                startActivity(i);
                System.out.println(position);

            }
        });

    //TODO****************************************************************************************
    //TODO ********* Event na dotknięcie i przytrzymanie przycisku kamery
    cameraList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
            alertDialog.setTitle(" ");
            menuCamera(position);
            return false;
        }
    });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.camera_settings) {
            Intent i = new Intent(getApplicationContext(), SettingActivity.class);
            i.putExtra("opcja", "dodaj");
            startActivity(i);
//            finish();
        }
        else if(id == R.id.cameraVideo){
            Intent i = new Intent(getApplicationContext(), VideoActivity.class);
            startActivity(i);
        }
        return false;
    }

    //********************** Metoda tworzy bazę danych - jesli nie istnieje i tworzy tabele   *****
    //********************** a jeśli istnieje to otwiera bazę *************************************
    public void createTableOrDatabase(){
        cameraInfo = openOrCreateDatabase("cameraInfo",MODE_PRIVATE, null);//stworzenie lokalnej bazy danych
//        cameraInfo.execSQL("DROP TABLE cameraSettings");
        cameraInfo.execSQL("CREATE TABLE IF NOT EXISTS cameraSettings(" +
                "id integer not null primary key AUTOINCREMENT , " +
                "nameCamera varchar(30) not null, " +
                "rtspAdress varchar(100) not null," +
                "login varchar(30) not null, " +
                "password varchar(30) not null, " +
                "port int not null," +
                "portHTTP int not null," +
                "brightness int not null, " +
                "contrast int not null, " +
                "saturation int not null, " +
                "hue int not null " +
        ");");
    }
    //*********************************************************************************************

    public void getAllCamera(){
        dataClasses.clear();
        dataClassesString.clear();
        coursor = cameraInfo.rawQuery("Select * from cameraSettings", null);

        if (coursor.getCount()!=0){
            coursor.moveToFirst();
            do{
                //TODO pobranie danych z bazy danych i zapisanie ich do obiektu,
                //TODO a nastepnie dodanie wszystkiego do listy
                DataClass dtC = new DataClass(
                        coursor.getString(0),
                       coursor.getString(1),
                        coursor.getString(2),
                        coursor.getString(3),
                        coursor.getString(4),
                        coursor.getString(5),
                        coursor.getString(6),
                        coursor.getString(7),
                        coursor.getString(8),
                        coursor.getString(9),
                        coursor.getString(10)
                );


                //TODO zapisanie informacji które mają zostać wyswietlone  w listView
                dataClassesString.add(coursor.getString(1) + "\n" + coursor.getString(2));

                dataClasses.add(dtC);
                coursor.moveToNext();
                i++;
                }while(coursor.getCount()!=i);
            for(DataClass dt : dataClasses){
                System.out.println("id " + dt.getIdCamera()
                + "nameCamera " + dt.getNameCamera()
                        + "rtspAdress " + dt.getRtspAdress()
                        + "login " + dt.getLogin()
                        + "password " + dt.getPassword()
                        + "port " + dt.getPort()
                        + "portHTTP " + dt.getPortHTTP()
                        + "brightness " + dt.getBrightness()
                        + "contrast " + dt.getContrast()
                        + "saturation " + dt.getSaturation()
                        + "hue " + dt.getHue() );
            }
        }
    }

    //TODO********** Wyswietlenie menu po przytrzymaniu "kamery" ******************************
    public void menuCamera(final int position){

        alertDialog.setItems(cameraMenuList, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {   //TODO jeśli wybrane zostało "Edytuj"
                    System.out.println("asdda " + which + position + dataClasses.get(position).getNameCamera().toString()
                            + dataClasses.get(position).getIdCamera()
                            +dataClasses.get(position).getRtspAdress().toString()
                            +dataClasses.get(position).getLogin().toString()
                            +dataClasses.get(position).getPassword().toString()) ;
                    try {
                        //Przejście do ConnectionSettings i przekazanie wartości
                        Intent i = new Intent(getApplicationContext(), SettingActivity.class);
                        i.putExtra("opcja", "edytuj");
                        i.putExtra("idCamera", dataClasses.get(position).getIdCamera());
                        i.putExtra("nameCamera", dataClasses.get(position).getNameCamera().toString());
                        i.putExtra("rstpAdress", dataClasses.get(position).getRtspAdress().toString());
                        i.putExtra("login", dataClasses.get(position).getLogin().toString());
                        i.putExtra("password", dataClasses.get(position).getPassword().toString());
                        i.putExtra("port", dataClasses.get(position).getPort().toString());
                        i.putExtra("portHTTP", dataClasses.get(position).getPortHTTP().toString());
                        i.putExtra("opcja", "edytuj");

                        startActivity(i);

                    }catch (Exception e){
                        System.out.println("asdda " + e);
                    }

                }
                else if(which==1){  //TODO jeśli jest wybrane "Usun"
                    deleteCamera(position);
                }
            }
        });
        alertDialog.show();

    }

    //TODO*************** Usuwanie kamery z listy ********************************************
    public void deleteCamera(final int   position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder .setMessage("Czy chcesz usunąć kamerę z listy?")
                .setTitle("Usuń");

        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){
                cameraInfo.execSQL(
                    "DELETE FROM cameraSettings "+
                    "WHERE id = "+dataClasses.get(position).getIdCamera()
                );
                //************************* Odświeżanie aktywności *****************************
                finish();                   // zamknięcie aktywności
        startActivity(getIntent()); // ponowne uruchomienie aktywności
        }
        })
        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialgo, int id) {
                // TODO tu można wpisać, jakie działanie ma się wykonać po wciśnieciu przycisku nie
            }
        });
        builder.show();
    }
}
