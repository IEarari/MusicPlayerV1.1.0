package com.example.android.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        Button button = (Button) findViewById(R.id.rts);


        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Details.this, MainActivity.class);
                startActivity(intent);

            }


        });

        return true;

    }

}
