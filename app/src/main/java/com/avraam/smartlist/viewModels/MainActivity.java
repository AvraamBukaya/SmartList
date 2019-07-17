package com.avraam.smartlist.viewModels;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.avraam.smartlist.R;

public class MainActivity extends AppCompatActivity {

    private ImageButton add_btn;
    private ImageButton view_list_btn;
    private ImageButton attach_file_btn;
    private ImageButton report_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_btn = findViewById(R.id.image_btn_add_product);
        view_list_btn = findViewById(R.id.image_btn_view_list);
        attach_file_btn = findViewById(R.id.image_btn_attach_file);
        report_btn = findViewById(R.id.image_btn_report);
        onClickAtAddBtnListener();
    }


    public void onClickAtAddBtnListener(){
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToAddProductScreen = new Intent(MainActivity.this,AddProduct.class);
                startActivity(moveToAddProductScreen);
                finish();
            }
        });
    }
}
