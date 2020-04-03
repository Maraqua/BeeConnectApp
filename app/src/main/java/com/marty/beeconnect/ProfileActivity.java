package com.marty.beeconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.marty.beeconnect.adapter.ProfileViewPageAdapter;
import com.marty.beeconnect.model.User;
import com.marty.beeconnect.rest.ApiClient;
import com.marty.beeconnect.rest.services.UserInterface;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileActivity extends AppCompatActivity {
@BindView(R.id.profile_cover)
    ImageView profileCover;
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
    int current_state =0;
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
        progressDialog.setMessage ("Loading...");
        progressDialog.show ();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this, MainActivity.class));

            }
        });

        if(FirebaseAuth.getInstance ().getCurrentUser ().getUid ().equalsIgnoreCase ( uid )){
            current_state = 1;

            loadProfile();

        }else{
            current_state=2;

        }
    }
    private void showUserData(User user) {
        profileViewPageAdapter = new ProfileViewPageAdapter ( getSupportFragmentManager (),1,user.getUid (),user.getState ());
        ViewPagerProfile.setAdapter ( profileViewPageAdapter );

    }
    private void loadProfile() {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        Call<User> call = userInterface.loadownProfile( params);
        call.enqueue ( new Callback<User> () {
            @Override
            public
            void onResponse ( Call<User> call, Response<User> response ) {
                if (response.body () != null) {
                    progressDialog.dismiss ();
                    showUserData ( response.body () );
                    profileUrl = response.body ().getProfileUrl ();
                    //coverUrl = response.body ().getCoverUrl ();
                    collapsingToolbar.setTitle ( response.body ().getName () );
                    if (!profileUrl.isEmpty ()) {
                        Picasso.get ().load ( profileUrl ).networkPolicy ( NetworkPolicy.OFFLINE ).into ( profileImage, new com.squareup.picasso.Callback () {
                            @Override
                            public
                            void onSuccess () {

                            }

                            @Override
                            public
                            void onError ( Exception e ) {
                                Picasso.get ().load ( profileUrl ).into ( profileImage );
                            }
                        } );
                        if (!coverUrl.isEmpty ()) {
                            Picasso.get ().load ( coverUrl ).networkPolicy ( NetworkPolicy.OFFLINE ).into ( profileCover, new com.squareup.picasso.Callback () {
                                @Override
                                public
                                void onSuccess () {

                                }

                                @Override
                                public
                                void onError ( Exception e ) {
                                    Picasso.get ().load ( coverUrl ).into ( profileCover );
                                }
                            } );

                        }

                    } else {
                        Toast.makeText ( ProfileActivity.this, "Something Went wrong...please try again later", Toast.LENGTH_SHORT ).show ();
                    }
                }
            }
            @Override
            public
            void onFailure ( Call<User> call, Throwable t ) {
                progressDialog.dismiss ();
                Toast.makeText ( ProfileActivity.this, "Something Went wrong...please try again later", Toast.LENGTH_SHORT ).show ();


            }
        } );



    }


}
