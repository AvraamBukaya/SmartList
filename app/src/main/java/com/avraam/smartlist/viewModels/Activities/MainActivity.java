package com.avraam.smartlist.viewModels.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avraam.smartlist.R;
import com.facebook.login.Login;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ImageButton add_btn;
    private ImageButton view_list_btn;
    private ImageButton attach_file_btn;
    private ImageButton report_btn;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_btn = findViewById(R.id.image_btn_add_product);
        view_list_btn = findViewById(R.id.image_btn_view_list);
        attach_file_btn = findViewById(R.id.image_btn_attach_file);
        report_btn = findViewById(R.id.image_btn_report);
        auth = FirebaseAuth.getInstance();
        onClickAtAddBtnListener();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_nav,menu);

        return true;

    }

    public void onClickAtAddBtnListener(){
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToAddProductScreen = new Intent(MainActivity.this, AddProduct.class);
                startActivity(moveToAddProductScreen);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_message:
                Toast.makeText(this,"There is no Massege",Toast.LENGTH_SHORT);
                return true;
            case R.id.nav_info:
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
                return true;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
