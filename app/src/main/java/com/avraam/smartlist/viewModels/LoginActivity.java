package com.avraam.smartlist.viewModels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.avraam.smartlist.R;
import com.avraam.smartlist.models.FireStoreDb;
import com.firebase.ui.auth.AuthUI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1213 ;
    private List<AuthUI.IdpConfig>providers;
    private Button btn_sign_out;
    private Button btn_continue_main_screen;
    private TextView full_name;
    private FirebaseAuth auth;
    private TextView clock;




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

        btn_sign_out = findViewById(R.id.sign_out_btn);
        clock = findViewById(R.id.clock);
        btn_continue_main_screen = findViewById(R.id.btn_main_screen);
        full_name = findViewById(R.id.user_full_name);
        auth = FirebaseAuth.getInstance();
        //db = FirebaseFirestore.getInstance();
        setTime();
        userLoggedIn();
        full_name.setText("Hi "+auth.getInstance().getCurrentUser().getDisplayName());
        signInWith();
        isExist();
        //addNewUser();
    }

    private void showsSignInOptions()
    {
       startActivityForResult(
               AuthUI.getInstance().createSignInIntentBuilder().
                       setAvailableProviders(providers).
                       setTheme(R.style.FirebaseUI).
                       setLogo(R.drawable.shoppingcard)
                       .build(),MY_REQUEST_CODE
       );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK)
            {
                //Get User

               FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                popMessage("You logged in");

                //Set Button Sign Out
                //btn_sign_out.setEnabled(true);
            }

        }
    }

    public void signInWith()
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
                                //btn_sign_out.setEnabled(false);
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

   public void onClick(View v) {

            btn_continue_main_screen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mainActivity = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                }
            });
    }


    public void userLoggedIn(){
        if(auth.getCurrentUser() == null){
            showsSignInOptions();

        }
    }


    public void addNewUser(){

        if (auth.getCurrentUser() == null)
            return;
        Map<String, Object> user = new HashMap<>();
        user.put("UserId",auth.getCurrentUser().getUid());
        user.put("FullName",auth.getCurrentUser().getDisplayName());
        user.put("Email",auth.getCurrentUser().getEmail());
        user.put("Phone",auth.getCurrentUser().getPhoneNumber());

        // Add a new document with a generated ID
        FireStoreDb.FireStoreDb().collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Smart List app", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Smart List app", "Error adding document", e);
                    }
                });

    }



    public void isExist() {
        CollectionReference allUsersRef = FireStoreDb.FireStoreDb().collection("Users");

        allUsersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (DocumentSnapshot document : task.getResult())
                    {
                        String useriD = document.getString("UserId");
                        if (useriD.equals(auth.getUid())) {
                            Toast.makeText(LoginActivity.this, "That username already exists.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                  addNewUser();

                } else {
                    popMessage("An error occurred, an entry was not added to the database");


                }
            }
        });


    }

    public void setTime(){
        DateFormat df = new SimpleDateFormat("MMM d, yyyy");
        String now = df.format(new Date());
        clock.setText(now);
    }

    public void popMessage(String message){
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }


}
