package com.example.gocar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class Maps extends AppCompatActivity {
    TextView ALL;
    ImageView  IMAGE;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String id = getIntent().getStringExtra("ID");
        String name = getIntent().getStringExtra("NAME");
        String year = getIntent().getStringExtra("YEAR");
        String latitude = getIntent().getStringExtra("LATITUDE");
        String longitude = getIntent().getStringExtra("LONGITUDE");
        String image = getIntent().getStringExtra("IMAGE");
        String fuel = getIntent().getStringExtra("FUEL");
        IMAGE = findViewById(R.id.image);
        String path = "http://192.168.1.12/" + image;
        Picasso.get().load(path).into(IMAGE);
        String text ="ID : " + id
                + "\n" +
        "CAR NAME : " + name
                + "\n" +
        "CAR MODEL : " + year
                + "\n" +
        "CAR LATITIDE : " + latitude
                + "\n" +
        "CAR LOGNITUDE : " + longitude
                + "\n" +
        "FUEL CAPACITY : " + fuel +" % ";
        ALL= findViewById(R.id.ALL);
        ALL.setText(text);
    }
}
