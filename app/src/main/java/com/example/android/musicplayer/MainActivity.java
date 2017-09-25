package com.example.android.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        Button gotodonate = (Button) findViewById(R.id.donateint);
        Button gotodetails = (Button) findViewById(R.id.ditals);


        gotodonate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent donate = new Intent(MainActivity.this, Donate.class);
                startActivity(donate);

            }


        });


        gotodetails.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent details = new Intent(MainActivity.this, Details.class);
                startActivity(details);

            }


        });
    }

    public void MediaPlayerStart(View view) {
        mediaPlayer.start();
    }

    public void MediaPlayerPause(View view) {
        mediaPlayer.pause();
    }

    public void MediaPlayerStop(View view) {
        mediaPlayer.stop();
        mediaPlayer.seekTo(0);
    }


}
