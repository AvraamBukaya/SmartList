package com.avraam.smartlist.viewModels.Activities;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RetrieveInformation extends AppCompatActivity {

    public static TextView title;
    public static ImageView prodcutPic;
    public static String barcode;
    public static String price;
    public static String description;
    private Button add_btn;
    private Button back_btn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_information);
        title = findViewById(R.id.title);
        prodcutPic = findViewById(R.id.product_image);
        setTitle("Product Information");
        Intent intent = getIntent();
        barcode = intent.getExtras().getString("BarcodeCode");
        new JsoupInformation().execute();
        auth = FirebaseAuth.getInstance();
        add_btn = findViewById(R.id.add_product_btn);
        back_btn = findViewById(R.id.back_product_btn);
        onClickAddProduct();
        onClickBackToBarcodeScannerBtn();


    }

    public void onClickAddProduct(){

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String now = dateFormat.format(new Date());
                Map<String, Object> product = new HashMap<>();
                Map<String, Object> productPerUser = new HashMap<>();
                int l = description.length();
                product.put("Barcode",barcode);
                product.put("UserId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                product.put("Date_Added",now);
                product.put("Product_Name", description.substring(10,l));
                product.put("Price",price);
                productPerUser.put("ProductBarcode",barcode);
                productPerUser.put("UserId",auth.getCurrentUser().getUid());
                FireStoreDb.FireStoreDb().collection("Products")
                        .add(product)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("Smart List app", "DocumentSnapshot added with ID: " + documentReference.getId());
                                Intent mainScreen = new Intent(RetrieveInformation.this, AddProduct.class);
                                openDialog(mainScreen);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Smart List app", "Error adding product document", e);
                            }
                        });
                FireStoreDb.FireStoreDb().collection("ProductsPerUser").add(productPerUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Smart List app", "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Smart List app", "Error adding product document", e);
                    }
                });

            }
        });



    }
    public void onClickBackToBarcodeScannerBtn(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RetrieveInformation.this,BarcodeScannerActivity.class));
                finish();
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