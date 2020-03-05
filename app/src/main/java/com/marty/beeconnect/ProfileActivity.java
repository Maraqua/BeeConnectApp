package com.marty.beeconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.marty.beeconnect.adapter.ProfileViewPageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {
//@BindView(R.id.profile_cover)
//    ImageView profileCover;
@BindView(R.id.profile_image)
    CircleImageView profileImage;
@BindView(R.id.profile_option_btn)
    Button profileOptionButton;
@BindView(R.id.toolbar)
    Toolbar toolbar;
@BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
@BindView(R.id.ViewPager_profile)
    ViewPager ViewPagerProfile;
ProfileViewPageAdapter profileViewPageAdapter;
String uid ="0";
int current_state =0; //0 load current user profile, 1 show their profile
    String profileUrl="",coverUrl="";
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        uid = getIntent ().getStringExtra ( "uid" );
        progressDialog = new ProgressDialog ( this );
        progressDialog.setCancelable ( false );
        progressDialog.setMessage ("Uploading...");
       // progressDialog.show ();

        ButterKnife.bind(this);
        profileViewPageAdapter = new ProfileViewPageAdapter ( getSupportFragmentManager (),1 );
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this, MainActivity.class));

            }
        });
        ViewPagerProfile.setAdapter ( profileViewPageAdapter );
        if(FirebaseAuth.getInstance ().getCurrentUser ().getUid ().equalsIgnoreCase ( uid )){
            current_state = 1;
            profileOptionButton.setText ( "Edit Profile" );

        }else{
            current_state=0;

        }
    }
}
