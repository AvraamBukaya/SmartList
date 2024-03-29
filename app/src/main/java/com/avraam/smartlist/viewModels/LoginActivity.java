package com.avraam.smartlist.viewModels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avraam.smartlist.R;
import com.firebase.ui.auth.AuthUI;



import java.util.Arrays;
import java.util.List;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1213 ;
    private List<AuthUI.IdpConfig>providers;
    private Button btn_sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );
        showsSignInOptions();
        btn_sign_out = findViewById(R.id.sign_out_btn);
        onClickSignOutBtn();

    }

    private void showsSignInOptions()
    {
       startActivityForResult(
               AuthUI.getInstance().createSignInIntentBuilder().
                       setAvailableProviders(providers).
                       setTheme(R.style.LoginTheme).
                       build(),MY_REQUEST_CODE

       );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(requestCode == RESULT_OK)
            {
                //Get User

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent mainScreenSender = new Intent(this, MainActivity.class);
                startActivity(mainScreenSender);
                finish();

                //Show Email on Toast
                popMessage(user.getEmail());
                //Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();

                //Set Button Sign Out
                btn_sign_out.setEnabled(true);
            }
            else if(requestCode == RESULT_CANCELED){
                popMessage("Sign-In cancelled");
            }
            else {
                popMessage("Login process failed for an unknown reason");
            }
        }
    }

    public void onClickSignOutBtn()
    {
        btn_sign_out.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AuthUI.getInstance()
                        .signOut(LoginActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                btn_sign_out.setEnabled(false);
                                showsSignInOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        popMessage(e.getMessage());
                    }
                });
            }
        });
    }

    public void popMessage(String message){
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }
}
