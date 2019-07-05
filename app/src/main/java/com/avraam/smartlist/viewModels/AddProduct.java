package com.avraam.smartlist.viewModels;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.avraam.smartlist.R;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;

public class AddProduct extends AppCompatActivity {

    FirebaseVisionBarcodeDetectorOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_QR_CODE,
                        FirebaseVisionBarcode.FORMAT_AZTEC)
                .build();
    }



}
