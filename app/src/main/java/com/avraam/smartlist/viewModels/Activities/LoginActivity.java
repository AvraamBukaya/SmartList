package com.avraam.smartlist.viewModels.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avraam.smartlist.R;
import com.avraam.smartlist.adapters.SearchAdpter;
import com.avraam.smartlist.models.FireStoreDb;
import com.firebase.ui.auth.AuthUI;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class LoginActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1213;
    private List<AuthUI.IdpConfig> providers;
    private Button btn_sign_out;
    private Button btn_continue_main_screen;
    private TextView full_name;
    private FirebaseAuth auth;
    private EditText clock;
    private CircleImageView imageUserPhoto;
    private StorageReference storageReference;
    private CollectionReference userRf;

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    private static FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState){
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
        storageReference = FirebaseStorage.getInstance().getReference("UsersImages");
        imageUserPhoto = findViewById(R.id.profile_image);
        userRf = FireStoreDb.FireStoreDb().collection("Users");
        //user = auth.getCurrentUser();
        setTime();
        userLoggedIn();

        signInWith();
        isExist();
        //addNewUser();
        onUserImageClick();





    }

    private void showsSignInOptions()
    {
        auth.signOut();
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().
                        setAvailableProviders(providers).
                        setTheme(R.style.FirebaseUI).
                        setLogo(R.drawable.shoppingcard)
                        .build(), MY_REQUEST_CODE
        );
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {

            if (requestCode == MY_REQUEST_CODE)
            {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                user = FirebaseAuth.getInstance().getCurrentUser();
                full_name.setText("Hi " + getName());
                popMessage("You logged in");
            }

        }
        else if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ){
            imageUri = data.getData();
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getApplicationContext(),"upload in process",Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }

        }


    }

    public void signInWith()
    {
        auth.signOut();
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           showsSignInOptions();
            }
        });
    }

    public void onClick(View v)
    {
        btn_continue_main_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });
    }


    public void userLoggedIn()
    {
        if (auth.getCurrentUser() == null)
        {
            showsSignInOptions();

        }
    }


    public void addNewUser()
    {
        if (auth.getCurrentUser() == null)
            return;
        Map<String, Object> user = new HashMap<>();
        user.put("UserId", auth.getCurrentUser().getUid());
        user.put("FullName", auth.getCurrentUser().getDisplayName());
        user.put("Email", auth.getCurrentUser().getEmail());
        user.put("Phone", auth.getCurrentUser().getPhoneNumber());

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


    public void isExist()
    {
        CollectionReference allUsersRef = FireStoreDb.FireStoreDb().collection("Users");

        allUsersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult())
                    {
                        String useriD = document.getString("UserId");
                        if (useriD.equals(auth.getUid())) {
                           Log.d("Smart List User Login","User is Allready exist");
                            return;
                        }
                    }

                    addNewUser();
                }
                else {
                    popMessage("An error occurred, an entry was not added to the database");
                }
            }
        });

    }

    public void setTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String now = dateFormat.format(new Date());
        clock.setText("כניסה: " + now);
    }

    public void popMessage(String message)
    {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    public String getName()
    {

        if (user != null) {
            if (user.getDisplayName().length() > 0)
                return user.getDisplayName();
            else if (user.getEmail().length() > 0)
                return user.getEmail();
        }
        return "unknown";
    }


    public void onUserImageClick()
    {
        imageUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);

    }
    private String getFileExtention(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getApplicationContext());
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null ){
            final  StorageReference  fileReference = storageReference.child(System.currentTimeMillis()+
                    "."+getFileExtention(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadingUri = task.getResult();
                        String mUri = downloadingUri.toString();


                        HashMap<String, Object> map = new HashMap<>();
                        map.put("Image",mUri);
                        userRf.document(user.getUid()).update(map);
                        pd.dismiss();

                    }
                    else
                        {
                            Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

        }
        else{
            Toast.makeText(getApplicationContext(),"No image selected",Toast.LENGTH_SHORT).show();
        }
    }


}


