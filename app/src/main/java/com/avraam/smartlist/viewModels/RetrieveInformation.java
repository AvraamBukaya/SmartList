package com.avraam.smartlist.viewModels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.avraam.smartlist.R;
import com.avraam.smartlist.models.JsoupInformation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RetrieveInformation extends AppCompatActivity {

    public static TextView description;
    public static ImageView prodcutPic;
    public static String barcode;
    private  String words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_information);
        description = findViewById(R.id.description);
        prodcutPic = findViewById(R.id.product_image);
        setTitle("Product Information");
        Intent intent = getIntent();
        barcode = intent.getExtras().getString("BarcodeCode");
        new JsoupInformation().execute();


    }



}
