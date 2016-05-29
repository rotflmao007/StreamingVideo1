package com.example.rotfl.streamingvideo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;

import org.apache.http.Header;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by rotfl on 10.05.2016.
 */
public class SettingImageActivity extends Activity {

    SeekBar seekBarJasnosc;
    TextView textViewJasnosc;
    SeekBar seekBarKontrast;
    TextView textViewKontrast;
    SeekBar seekBarNasycenie;
    TextView textViewNasycenie;
    SeekBar seekBarBarwa;
    TextView textViewBarwa;
    Button zapisz;
    Button podglad;
    Button domyslne;
    Button zastosuj;
    ImageView image;

    int progressJasnosc;
    int progressKontrast;
    int progressNasycenie;
    int progressBarwa;

    String idCamera;
    String url;


    SQLiteDatabase cameraInfo;
    Cursor coursor;
    Context context;
    ArrayList<DataClass> dataClasses = new ArrayList<>();

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_image);

        cameraInfo = openOrCreateDatabase("cameraInfo",MODE_PRIVATE, null);//stworzenie lub otworzenie lokalnej bazy danych

        seekBarJasnosc = (SeekBar) findViewById(R.id.seekBarJasnosc);
        seekBarKontrast = (SeekBar) findViewById(R.id.seekBarKontrast);
        seekBarNasycenie = (SeekBar) findViewById(R.id.seekBarNasycenie);
        seekBarBarwa = (SeekBar) findViewById(R.id.seekBarBarwa);
        textViewJasnosc = (TextView) findViewById(R.id.textViewJasnosc);
        textViewKontrast = (TextView) findViewById(R.id.textViewKontrast);
        textViewNasycenie = (TextView) findViewById(R.id.textViewNasycenie);
        textViewBarwa = (TextView) findViewById(R.id.textViewBarwa);

        zapisz = (Button) findViewById(R.id.save);
        podglad = (Button) findViewById(R.id.podglad);
        domyslne = (Button) findViewById(R.id.domyslne);
        zastosuj = (Button) findViewById(R.id.zastosuj);
        image = (ImageView) findViewById(R.id.imageView);

        context = this;

        Intent i = getIntent();
        Bundle pobierzDane = i.getExtras();
        idCamera = pobierzDane.getString("idCamera");
        getInfoCamera(idCamera);                        //Zapisanie do listy wybranej kamery

        new DownloadImageTask((ImageView)  image).execute("http://192.168.0.112:80/jpg/image.jpg");
        //Z adresu rtsp rozdzielone jest np. adresIP od np /video.3gp
        String adresIP = "";
        String ipAdres = dataClasses.get(0).getRtspAdress();
        int pozycja = ipAdres.indexOf("/");
        adresIP =  ipAdres.substring(0, pozycja);

        url = adresIP + ":" + dataClasses.get(0).getPortHTTP();
     //   dataClasses.get(0).getLogin() + ":" + dataClasses.get(0).getPassword() + "@" +
                System.out.println("||||||||||| " + url);

    //Ustawienie seekBar'ow wg. danych zapisanych w bazie telefonu
        for(DataClass dt : dataClasses){
            seekBarJasnosc.setProgress(Integer.parseInt(dt.getBrightness()));
            seekBarKontrast.setProgress(Integer.parseInt(dt.getContrast()));
            seekBarBarwa.setProgress(Integer.parseInt(dt.getHue()));
            seekBarNasycenie.setProgress(Integer.parseInt(dt.getSaturation()));
        }

       podglad.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(context, PodgladActivity.class);
                System.out.println("url");
//               i.putExtra("url", url);
                startActivity(i);
           }
       });

        progressJasnosc = 0;
        progressKontrast = 0;
        progressNasycenie = 0;
        progressBarwa = 0;

        context = getApplicationContext();

        zastosuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zapiszWkamerze();
                Toast toast = Toast.makeText(context, "Zaktualizowano dane",Toast.LENGTH_SHORT);
            }
        });

        domyslne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarJasnosc.setProgress(50);
                seekBarKontrast.setProgress(35);
                seekBarNasycenie.setProgress(50);
                seekBarBarwa.setProgress(50);
            }
        });
        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zapiszWkamerze();

            }
        });

//TODO------------------- Seek bar do ustawiania właściwości obrazu -------------------------------
        seekBarJasnosc.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progressJasnosc = progressValue;
                textViewJasnosc.setText(progressJasnosc + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("start start " + progressJasnosc);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewJasnosc.setText(progressJasnosc + "");
            }
        });

        seekBarKontrast.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progressKontrast = progressValue;
                textViewKontrast.setText(progressKontrast + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("start start " + progressKontrast);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewKontrast.setText(progressKontrast + "");
            }
        });

        seekBarNasycenie.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progressNasycenie = progressValue;
                textViewNasycenie.setText(progressNasycenie + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("start start " + progressNasycenie);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewNasycenie.setText(progressNasycenie + "");
            }
        });

        seekBarBarwa.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progressBarwa = progressValue;
                textViewBarwa.setText(progressBarwa + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("start start " + progressBarwa);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewBarwa.setText(progressBarwa + "");
            }
        });
    }
//TODO-------------------Za[osamoe damuch w kamerze ip --------------------------------------------
    public void zapiszWkamerze(){

            //Zapytanie zapisuje formatowanie obrazu : Jasność
            zapytanieURL("http://" + url + "/cgi-bin/operator/param?action=update&ImageSource.I0.Sensor.Brightness=" + seekBarJasnosc.getProgress());

            //Zapytanie zapisuje formatowanie obrazu : Kontrast
            zapytanieURL("http://"+url+"/cgi-bin/operator/param?action=update&ImageSource.I0.Sensor.Contrast=" + seekBarKontrast.getProgress());

            //Zapytanie zapisuje formatowanie obrazu : Nasycenie
            zapytanieURL("http://"+url+"/cgi-bin/operator/param?action=update&ImageSource.I0.Sensor.Saturation="+seekBarNasycenie.getProgress());

            //Zapytanie zapisuje formatowanie obrazu : Barwa
            zapytanieURL("http://" + url + "/cgi-bin/operator/param?action=update&ImageSource.I0.Sensor.Hue=" + seekBarBarwa.getProgress());


    }

//TODO-------------------Metoda pobiera wszystkie informacje o kamerze edytowanej-----------------
    public void getInfoCamera(String idCam){
        dataClasses.clear();

        coursor = cameraInfo.rawQuery("Select * from cameraSettings WHERE id = " + idCam, null);

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

                dataClasses.add(dtC);
                coursor.moveToNext();
                i++;
            }while(coursor.getCount()!=i);
        }
    }

    public void updateImageSettings(final String idCam){
        final Intent inetnt = new Intent(getApplicationContext(), MainActivity.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder .setMessage("Czy chcesz edytować wpis?")
                .setTitle("Edytuj");

        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    try {
                        cameraInfo.execSQL("UPDATE cameraSettings SET " +
                                        "brightness = " + seekBarJasnosc.getProgress() + "," +
                                        "contrast = " + seekBarKontrast.getProgress() + "," +
                                        "saturation = " + seekBarNasycenie.getProgress() + "," +
                                        "hue = " + seekBarBarwa.getProgress() + "  " +
                                        "where id = " + idCam

                        );
                        finish();
                        startActivity(inetnt);
                    } catch (Exception e) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Błąd")
                                .setMessage("Sprawdź adres lub port połączenia kamery");

                        alertDialog.show();
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


//TODO -----------------Zadaniem metody jest zapisanie w kamerze ustawień obrazu--------------------
//TODO----------------takich jak: Kontrast, nasycenie, barwa, jasność----------------------
    public void zapytanieURL(String url){

        AsyncHttpClient client = new AsyncHttpClient();
    //TODO --------------------------- Autoryzacja loginem i hasłem --------------------------------
        client.addHeader("Authorization",
                "Basic " + Base64.encodeToString(
                        (dataClasses.get(0).getLogin() + ":" + dataClasses.get(0).getPassword()).getBytes(), Base64.NO_WRAP));
    //TODO -----------------------------------------------------------------------------------------
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        i++;
                        if (i == 3) {
                            Toast toast = Toast.makeText(context, "Zmiany zostały zapisane", Toast.LENGTH_SHORT);
                            toast.show();
                            updateImageSettings(idCamera);
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
//
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//                        alertDialog.setItems(, new AlertDialog.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        alertDialog.setTitle("Błąd");
//                        alertDialog.show();
// Toast toast = Toast.makeText(context, "Nie zaktualizowano! \n Wystąpił błąd!\n " + System.err,Toast.LENGTH_SHORT);

                    }
                });

    }


    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            //pDlg.dismiss();
            bmImage.setImageBitmap(result);
            bmImage.setRotationX(90);

        }};


}



/*    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        showProgressDialog();
    }*/



//
//    public void getImageDialog(){
//        Dialog dialog = new Dialog(SettingImageActivity.this);
//        dialog.setContentView(R.layout.setting_image);
//        dialog.setTitle("This is my custom dialog box");
//        dialog.setCancelable(true);
//
//        ImageView img = (ImageView) dialog.findViewById(R.id.imageView2);
//
//        try {
//            URL url = new URL("http://192.168.0.112:3333/jpg/image.jpg");
//            URLConnection conn = url.openConnection();
//            HttpURLConnection httpConn = (HttpURLConnection)conn;
//            httpConn.setRequestMethod("GET");
//            httpConn.connect();
//            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                InputStream inputStream = httpConn.getInputStream();
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                inputStream.close();
//                img.setImageBitmap(bitmap);
//            }
//        } catch (MalformedURLException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        //img.setImageResource(R.drawable.ic_launcher);
//
//        dialog.show();
//    }
//}