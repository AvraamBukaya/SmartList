package com.avraam.smartlist.viewModels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.avraam.smartlist.R;

public class MainActivity extends AppCompatActivity {

    private Button add_product_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*    add_product_btn = findViewById(R.id.add_productBtn);
        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addProductScreen = new Intent(MainActivity.this,AddProduct.class);
                startActivity(addProductScreen);
                finish();

            }
        });*/
    }
}
