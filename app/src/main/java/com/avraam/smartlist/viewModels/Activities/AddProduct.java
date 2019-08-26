package com.avraam.smartlist.viewModels.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.avraam.smartlist.R;
import com.avraam.smartlist.adapters.ProductAdapter;
import com.avraam.smartlist.models.Dialog;
import com.avraam.smartlist.models.Product;
import com.avraam.smartlist.viewModels.Fragmnets.AddManuallyFragment;
import com.avraam.smartlist.viewModels.Fragmnets.SearchFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class AddProduct extends AppCompatActivity {


    private BottomNavigationView bottom_nav;
    private FrameLayout mainFrame;
    private SearchFragment searchFragment;
    private AddManuallyFragment addManuallyFragment;
    private FirebaseFirestore db;
    private CollectionReference productsRf;
    private ProductAdapter productAdapter;
    public static String comparePrices;
    public TextView barcodeProduct ;
    private ImageView more_icon ;
    private TextView compartionPriceInformation;
    private FirebaseAuth auth ;



    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        View v = findViewById(R.id.recycler_view);
        auth = FirebaseAuth.getInstance();
        bottom_nav = findViewById(R.id.nav_bottom);
        mainFrame = findViewById(R.id.fragments_div);
        searchFragment = new SearchFragment();
        addManuallyFragment = new AddManuallyFragment();
        db = FirebaseFirestore.getInstance();
        productsRf = db.collection("Products");
        setUpRecyclerView();

        barcodeProduct = findViewById(R.id.text_view_product_barcode);
        more_icon = findViewById(R.id.more_icon);
        compartionPriceInformation = findViewById(R.id.prices_info);
        menuItems();
        //onClickPricesCompartionText();
    }

    private void setUpRecyclerView() {
        Query query = productsRf.orderBy("Product_Name",Query.Direction.DESCENDING);
        query = query.whereEqualTo("UserId",auth.getCurrentUser().getUid());
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();
        productAdapter = new ProductAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                productAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }
    protected  void onStart(){
        super.onStart();
        productAdapter.startListening();
    }

    protected void onStop(){
        super.onStop();
        productAdapter.stopListening();
    }

    public void onClickPricesCompartionText(){


        more_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              RetrieveInformation.barcode = barcodeProduct.getText().toString();
              Dialog price = new Dialog();
              price.show(getSupportFragmentManager(),"Information");
              price.setTitle("מחיר המוצר ברשתות המזון השונות");
              price.setMassege(comparePrices);
            }
        });

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
                        Intent mainScreen = new Intent(AddProduct.this, MainActivity.class);
                        startActivity(mainScreen);
                        finish();
                        return true;
                    case R.id.nav_barcode_scanner:
                        Intent barcodeScanner =  new Intent(AddProduct.this, BarcodeScannerActivity.class);
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
