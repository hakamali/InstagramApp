package com.example.instagramapp.profileuser;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.instagramapp.R;
import com.example.instagramapp.instagramposthome.InstagramHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserProfileActivity extends AppCompatActivity {
     // DECLARE THE FIELDS
    EditText userNameEditText,userStatsEditText;
    ImageView userImageProfileView;
    LinearLayout saveProfileBtn;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mUserDatabase;
    Uri imageHoldUri=null;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_user_profile);
        userNameEditText = findViewById (R.id.userProfileName);
        userStatsEditText = findViewById (R.id.userProfileStatus);
        userImageProfileView = findViewById (R.id.userProfileImageView);
        saveProfileBtn = findViewById (R.id.saveProfile);
        mAuth = FirebaseAuth.getInstance ();
        mAuthListener = new FirebaseAuth.AuthStateListener () {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser ();
                if (user != null) {
                    finish ();
                    Intent moveToHome = new Intent (UserProfileActivity.this, InstagramHome.class);
                    moveToHome.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity (moveToHome);
                }

            }
        };
        mUserDatabase = FirebaseDatabase.getInstance ().getReference ().child ("Users").child (mAuth.getCurrentUser ().getUid ());
        mStorageRef= FirebaseStorage.getInstance ().getReference ();
        saveProfileBtn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
               saveUserProfile();
            }
        });
        userImageProfileView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                profilePicSelection();

            }
        });

    }

    private void saveUserProfile() {
        final String username,userStatus;
        username=userNameEditText.getText ().toString ().trim ();
        userStatus=userStatsEditText.getText ().toString ().trim ();
        if (!TextUtils.isEmpty (username) && !TextUtils.isEmpty (userStatus)){
            if (imageHoldUri !=null){
              StorageReference mChildStorage = mStorageRef.child ("USer_Profile").child ( imageHoldUri.getLastPathSegment ());
              String profilePicUrl= imageHoldUri.getLastPathSegment ();
              mChildStorage.putFile (imageHoldUri).addOnSuccessListener (new OnSuccessListener <UploadTask.TaskSnapshot> () {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      final Uri imageUrl = taskSnapshot.getDownloadUrl();

                  }
              });



            }
        }
    }

    private void profilePicSelection() {

    }
}
