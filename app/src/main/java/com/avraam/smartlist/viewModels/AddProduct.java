package com.avraam.smartlist.viewModels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.avraam.smartlist.R;
import com.avraam.smartlist.adapters.ProductAdapter;
import com.avraam.smartlist.models.FireStoreDb;
import com.avraam.smartlist.models.Product;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;


public class AddProduct extends AppCompatActivity {


    private BottomNavigationView bottom_nav;
    private FrameLayout mainFrame;
    private SearchFragment searchFragment;
    private AddManuallyFragment addManuallyFragment;
    private FirebaseFirestore db;
    private CollectionReference productsRf;
    private ProductAdapter productAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        bottom_nav = findViewById(R.id.nav_bottom);
        mainFrame = findViewById(R.id.fragments_div);
        searchFragment = new SearchFragment();
        addManuallyFragment = new AddManuallyFragment();
        db = FirebaseFirestore.getInstance();
        productsRf = db.collection("Products");
        setUpRecyclerView();

        menuItems();


    }

    private void setUpRecyclerView() {
        Query query = productsRf.orderBy("Product_Name",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();
        productAdapter = new ProductAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);
    }
    protected  void onStart(){
        super.onStart();
        productAdapter.startListening();
    }

    protected void onStop(){
        super.onStop();
        productAdapter.stopListening();
    }


    public void menuItems(){
        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_home:
                        Intent mainScreen = new Intent(AddProduct.this,MainActivity.class);
                        startActivity(mainScreen);
                        finish();
                        return true;

                    case R.id.nav_barcode_scanner:
                        Intent barcodeScanner =  new Intent(AddProduct.this,BarcodeScannerActivity.class);
                        startActivity(barcodeScanner);
                        return true;
                    case R.id.nav_add_manually_product:
                        setFragment(addManuallyFragment);
                        return true;
                    case R.id.nav_search:
                        setFragment(searchFragment);
                        return true;
                     default:
                         setFragment(searchFragment);
                         return true;
                }
            }
        });
    }

    private void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragments_div, fragment );
        fragmentTransaction.commit();

    }

}
