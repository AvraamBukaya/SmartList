package com.avraam.smartlist.viewModels.Fragmnets;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avraam.smartlist.R;
import com.avraam.smartlist.adapters.ProductAdapter;
import com.avraam.smartlist.adapters.SearchAdpter;
import com.avraam.smartlist.models.Product;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;

import javax.annotation.Nullable;


public class SearchFragment extends Fragment {


    private EditText searchFiled;
    private ImageView searchBtn;
    private RecyclerView resultList;
    private FirebaseFirestore db;
    private CollectionReference productsRf;
    private ArrayList<String> products;
    private ArrayList<String> barcodes;
    private ArrayList<String> prices;
    private SearchAdpter searchAdpter;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_search, container, false);
        searchFiled = view.findViewById(R.id.search_tab);
        searchBtn = view.findViewById(R.id.search_icon);
        resultList = view.findViewById(R.id.recycler_view_search);
        resultList.setHasFixedSize(true);
        resultList.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();
        productsRf = db.collection("Products");
        resultList.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultList.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        products = new ArrayList<>();
        barcodes = new ArrayList<>();
        prices = new ArrayList<>();
        onClickSearch();

        return view;
    }




    public void onClickSearch(){
        searchFiled.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    setAdpter(editable.toString());

                }

            }
        });
    }

    private void setAdpter(final String searchedString) {
        products.clear();
        barcodes.clear();
        resultList.removeAllViews();


        productsRf.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult())
                    {

                        String productName= document.get("Product_Name").toString();
                        String barcode = document.get("Barcode").toString();
                        String price = document.get("Price").toString();

                        int counter = 0;

                        if(productName.contains(searchedString))
                        {
                            products.add(productName);
                            barcodes.add(barcode);
                            prices.add(price);
                            counter++;
                        }
                        else if(barcode.contains(searchedString))
                        {
                            products.add(productName);
                            barcodes.add(barcode);
                            prices.add(price);
                            counter++;
                        }

                        if(counter == 15)
                            break;
                    }

                    searchAdpter = new SearchAdpter(getActivity(), products,barcodes,prices);
                    resultList.setAdapter(searchAdpter);
                }
            }
        });
    }


}
