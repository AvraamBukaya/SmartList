package com.avraam.smartlist.viewModels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.avraam.smartlist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;



public class AddProduct extends AppCompatActivity {


    private BottomNavigationView bottom_nav;
    private FrameLayout mainFrame;
    private SearchFragment searchFragment;
    private AddManuallyFragment addManuallyFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        bottom_nav = findViewById(R.id.nav_bottom);
        mainFrame = findViewById(R.id.fragments_div);
        searchFragment = new SearchFragment();
        addManuallyFragment = new AddManuallyFragment();
        menuItems();


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
