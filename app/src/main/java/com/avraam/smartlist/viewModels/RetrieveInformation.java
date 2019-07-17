package com.avraam.smartlist.viewModels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.avraam.smartlist.R;
import com.avraam.smartlist.models.Dialog;
import com.avraam.smartlist.models.FireStoreDb;
import com.avraam.smartlist.models.JsoupInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RetrieveInformation extends AppCompatActivity {

    public static TextView description;
    public static ImageView prodcutPic;
    public static String barcode;
    private Button add_btn;

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

        add_btn = findViewById(R.id.add_product_btn);
        onClickAddProduct();


    }

    public void onClickAddProduct(){

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df = new SimpleDateFormat("MMM d, yyyy");
                String now = df.format(new Date());
                Map<String, Object> product = new HashMap<>();

                product.put("Barcode Number",barcode);
                product.put("Date added",now);
                product.put("Product Name",description.getText());
                FireStoreDb.FireStoreDb().collection("Products")
                        .add(product)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("Smart List app", "DocumentSnapshot added with ID: " + documentReference.getId());
                                Intent mainScreen = new Intent(RetrieveInformation.this,AddProduct.class);
                                openDialog(mainScreen);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Smart List app", "Error adding product document", e);
                            }
                        });


            }
        });


    }

    private void openDialog(Intent intent) {
        Dialog addProductDialog = new Dialog();
        addProductDialog.show(getSupportFragmentManager(),"Information");
        addProductDialog.setIntent(intent);
        addProductDialog.setTitle("Information");
        addProductDialog.setMassege("Product successfully added");
    }


}
