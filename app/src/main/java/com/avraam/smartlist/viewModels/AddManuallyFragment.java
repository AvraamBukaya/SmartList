package com.avraam.smartlist.viewModels;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avraam.smartlist.R;
import com.avraam.smartlist.models.DatePickerFragment;
import com.avraam.smartlist.models.Dialog;
import com.avraam.smartlist.models.FireStoreDb;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddManuallyFragment extends Fragment {


    private EditText productName;
    private EditText productBarcode;
    private TextView productAddedDate;
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
                    product.put("Barcode", productBarcode.getText().toString());
                    product.put("Date_Added", productAddedDate.getText().toString());
                    product.put("Product_Name", productName.getText().toString());
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
