package com.marty.beeconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import java.lang.Integer;
import java.util.List;
import java.util.Map;

import com.marty.beeconnect.Helpers.InputValidation;
import com.marty.beeconnect.adapter.IdeasAdapter;
import com.marty.beeconnect.model.IdeasModel;
import com.marty.beeconnect.model.User;
import com.marty.beeconnect.rest.ApiClient;
import com.marty.beeconnect.rest.services.UserInterface;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity{
private final AppCompatActivity activity=LoginActivity.this;

    private static final String TAG = "GOOGLEACTIVITY";
    private static  final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private SignInButton signInButton;
    ProgressDialog progressDialog;
//sign up with email
    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private AppCompatButton appCompatButtonRegister;

    private InputValidation inputValidation;
    private UserInterface databaseHelper;
    private UserReg user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_login);
       //getSupportActionBar().hide();

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);






        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        mFirebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Signing your In... Please Wait !");
        //todo: signout all accounts
    signInButton.setOnClickListener ( v -> signIn () );
    appCompatButtonRegister.setOnClickListener ( v -> postDataToDB () );
    textInputEditTextName.addTextChangedListener ( textWatcher );

    initObjects ();
    }
private final TextWatcher textWatcher = new TextWatcher () {
    @Override
    public
    void beforeTextChanged ( CharSequence s, int start, int count, int after ) {

    }

    @Override
    public
    void onTextChanged ( CharSequence tex, int start, int before, int count ) {
    if(textInputEditTextName.length ()> 20 && textInputEditTextName.length () <=5){
        textInputLayoutName.setError ( getString (R.string.username_checker) );
        textInputLayoutName.setErrorEnabled ( true );
    }else {
        textInputLayoutName.setErrorEnabled ( false );
    }
    }

    @Override
    public
    void afterTextChanged ( Editable s ) {

    }
};



    private void initObjects() {
        inputValidation = new InputValidation( activity);
        databaseHelper = new UserInterface () {
            @Override
            public
            Call<Integer> signin ( UserInfo userInfo ) {
                return null;
            }

            @Override
            public
            Call<User> loadownProfile ( Map<String, String> params ) {
                return null;
            }

            @Override
            public
            Call<Integer> uploadIdea ( MultipartBody requestBody ) {
                return null;
            }

            @Override
            public
            Call<List<User>> searchbees ( Map<String, String> params ) {
                return null;
            }

            @Override
            public
            Call<List<IdeasModel>> getMyProfileTimeline ( Map<String, String> params ) {
                return null;
            }

            @Override
            public
            Call<List<IdeasModel>> getHotPosts ( Map<String, String> params ) {
                return null;
            }


            @Override
            public
            Call<List<IdeasModel>> getAllPosts ( Map<String, String> params ) {
                return null;
            }

            @Override
            public
            Call<Integer> voteDownvote ( IdeasAdapter.AddVote requestBody ) {
                return null;
            }


        };
        user = new UserReg ();




    }

    private
    void postDataToDB () {
        if (!inputValidation.isInputEditTextFilledName(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name_empty))) {
            return;
        }
        if (!inputValidation.isNameGTFive(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name_small))) {
            return;
        }
        if (!inputValidation.isNameAllLetters(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name_letters_only))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilledEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email_empty))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail (textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilledPassword (textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password_empty))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,textInputLayoutConfirmPassword,
                                                    getString(R.string.error_password_match))) {
            return;
        }
        if(textInputEditTextEmail !=null){
            user.setUid (textInputEditTextName.getText().toString().trim());
            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail (textInputEditTextEmail.getText().toString().trim());
            user.setUserPass (textInputEditTextPassword.getText().toString().trim());
            startActivity(new Intent(LoginActivity.this,MainActivity.class));

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser!=null){
            Log.d(TAG,"USER IS ALREADY LOGGED IN");
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            progressDialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mFirebaseAuth.getCurrentUser();


                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    String userToken = instanceIdResult.getToken();
                                    String uid= user.getUid();
                                    String name = user.getDisplayName();
                                    String email = user.getEmail();
                                    final String userPass = "";
                                    String profileUrl =user.getPhotoUrl().toString();
                                    final String coverUrl = "";
                                    UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
                                    //call retrofit asynchronously
                                    Call<Integer> call = userInterface.signin(new UserInfo(uid,name,email,userPass,profileUrl,coverUrl,userToken));
                                    call.enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            progressDialog.dismiss();
                                            if(response.body()==1){
                                                Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                                finish();
                                            }else{
                                                Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                                }

                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            FirebaseAuth.getInstance().signOut();
                                            progressDialog.dismiss();
                                            Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }
    public  class UserInfo{
            String uid,name,email,userPass, profileUrl,coverUrl,userToken;


        public UserInfo( String uid, String name, String email, String userPass, String profileUrl, String coverUrl, String userToken) {
            this.uid = uid;
            this.name = name;
            this.email = email;
            this.userPass = userPass;
            this.profileUrl = profileUrl;
            this.coverUrl = coverUrl;
            this.userToken = userToken;
        }


    }
    //empty fields
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }


}
//todo:change progress bar color for signing in with google
//todo:make a cursor color for every cursor blink