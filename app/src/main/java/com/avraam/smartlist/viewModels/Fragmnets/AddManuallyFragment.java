package com.avraam.smartlist.viewModels.Fragmnets;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avraam.smartlist.R;
import com.avraam.smartlist.models.Dialog;
import com.avraam.smartlist.models.FireStoreDb;
import com.avraam.smartlist.viewModels.Activities.AddProduct;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddManuallyFragment extends Fragment {


    private EditText productName;
    private EditText productBarcode;
    private TextView productAddedDate;
    private TextView productPrice;
    private Button save_btn;

    public AddManuallyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_manually, container, false);
        productName = v.findViewById(R.id.add_manually_product_name);
        productBarcode = v.findViewById(R.id.add_manually_barcode);
        productAddedDate = v.findViewById(R.id.add__manually_date);
        productPrice = v.findViewById(R.id.add__manually_price);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String now = dateFormat.format(new Date());
        productAddedDate.setText(now);
        save_btn = v.findViewById(R.id.save_button);
        onClickSaveBtn();

        return v;
    }


    public void onClickSaveBtn() {
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidateField()) {
                    Map<String, String> product = new HashMap<>();
                    Map<String, String> productPerUser = new HashMap<>();
                    product.put("UserId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    product.put("Barcode", productBarcode.getText().toString());
                    product.put("Date_Added", productAddedDate.getText().toString());
                    product.put("Product_Name", productName.getText().toString());
                    product.put("Price",productPrice.getText().toString());
                    productPerUser.put("UserId",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    productPerUser.put("ProductBarcode",productBarcode.getText().toString());
                    FireStoreDb.FireStoreDb().collection("Products")
                            .add(product)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("Smart List app", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Intent addProduct = new Intent(getActivity(), AddProduct.class);
                                    openDialog(addProduct);


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Smart List app", "Error adding product document", e);
                                }
                            });


                }
            }
        });



    }

    public boolean isValidateField(){

        String product_Name=  productName.getText().toString();
        String product_Barcode = productBarcode.getText().toString();

        if(product_Name.length() == 0){
            productName.setError("Required field");
            return false;
        }
        else if(product_Barcode.length() == 0){
            productBarcode.setError("Required field");
            return false;
        }
        return true;


    }

    private void openDialog(Intent intent) {
        Dialog addProductDialog = new Dialog();
        addProductDialog.show(getFragmentManager(),"Information");
        addProductDialog.setTitle("Information");
        addProductDialog.setIntent(intent);
        addProductDialog.setMassege("Product successfully added");
    }



}
