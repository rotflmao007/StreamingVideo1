package com.example.rotfl.streamingvideo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

public
class VideoActivity extends Activity implements MediaController.MediaPlayerControl
{

    VideoView videoView;

    String      idCamera;
    String      rtspAdress;
    String      login;
    String      password;
    String      port;
    int         buffer;

    MediaPlayer mediaPlayer;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Create a VideoView widget in the layout file
        //use setContentView method to set content of the activity to the layout file which contains videoView
        this.setContentView(R.layout.video_layout);

        videoView = (VideoView) findViewById(R.id.videoplayer);

        //add controls to a MediaPlayer like play, pause.
        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);

        //pobieranie danych z MainActivity
        Intent i = getIntent();
        Bundle pobierzDane = i.getExtras();
        idCamera = pobierzDane.getString("idCamera");
        rtspAdress = pobierzDane.getString("rtspAdress");
        login = pobierzDane.getString("login");
        password = pobierzDane.getString("password");
        port = pobierzDane.getString("port");
        buffer = 0;

                    //Z adresu rtsp rozdzielone jest np. adresIP od np /video.3gp
        String adresIP = "";
        int pocztekSmieci = rtspAdress.indexOf("/");
        adresIP =  rtspAdress.substring(0,pocztekSmieci);
                    // **********************************************************
        String nazwaPliku;
        nazwaPliku = rtspAdress.substring(pocztekSmieci, rtspAdress.length());
        System.out.println( "rtsp://" + login + ":" + password + "@" + adresIP + ":" + port + nazwaPliku);
        try{
            //Set the path of Video or URI
            videoView.setVideoURI(Uri.parse("rtsp://" + login + ":" + password + "@" + adresIP + ":" + port + nazwaPliku));
            System.out.println( videoView.getBufferPercentage() + "asdasdasd:==" );
            videoView.requestFocus();

        }catch (Exception e){

        }


    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {

        return 0;
    }

    @Override
    public int getCurrentPosition() {

        return 0;
        //mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    public int getBufferPercentage() {
        System.out.println();
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

}