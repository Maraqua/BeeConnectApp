package com.marty.beeconnect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.marty.beeconnect.rest.ApiClient;
import com.marty.beeconnect.rest.services.UserInterface;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public
class UploadActivity extends AppCompatActivity {

    @BindView(R.id.postBtnTxt)
    TextView postBtnTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dialogAvatar)
    CircleImageView dialogAvatar;
    @BindView(R.id.status_edit)
    EditText statusEdit;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.add_image)
    Button addImage;
    String imageUploadUrl = "";
    boolean isImageSelected = false;
    ProgressDialog progressDialog;
    File compressedImageFile = null;
    protected

    @Override
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_upload );
        ButterKnife.bind ( this );
        setSupportActionBar ( toolbar );
        toolbar.setNavigationIcon ( R.drawable.ic_arrow_back_black_24dp );
        getSupportActionBar ().setTitle ( "" );
        toolbar.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public
            void onClick ( View v ) {
                onBackPressed ();
            }
        } );
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading...");
         
        addImage.setOnClickListener ( new View.OnClickListener () {
            @Override
            public
            void onClick ( View v ) {
                //open gallery
                ImagePicker.create ( UploadActivity.this )
                        .folderMode ( true )
                        .single ().start ();
            }
        } );
        postBtnTxt.setOnClickListener ( new View.OnClickListener () {
            @Override
            public
            void onClick ( View v ) {
                uploadPost();
            }
        } );
    }

    private void uploadPost() {
        String status = statusEdit.getText().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (status.trim().length() > 0 || isImageSelected) {
            progressDialog.show();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            builder.addFormDataPart("post", status);
            builder.addFormDataPart("postUserId", userId);

            if (isImageSelected) {
                builder.addFormDataPart("isImageSelected", "1");
                builder.addFormDataPart ( "file", compressedImageFile.getName (), RequestBody.create ( MediaType.parse ( "multipart/form-data"),compressedImageFile ) );
            }else{
                builder.addFormDataPart ( "isImageSelected","0" );

            }MultipartBody multipartBody = builder.build ();
            UserInterface userInterface = ApiClient.getApiClient ().create ( UserInterface.class );
            Call<Integer>call = userInterface.uploadIdea ( multipartBody );
            call.enqueue ( new Callback<Integer> () {
                @Override
                public
                void onResponse ( Call<Integer> call, Response<Integer> response ) {
                    progressDialog.dismiss ();
                    if(response.body ()!=null && response.body () == 1){
                        Toast.makeText ( UploadActivity.this,"Post is Successful",Toast.LENGTH_SHORT ).show ();
                        Intent intent = new Intent ( UploadActivity.this,MainActivity.class );
                        startActivity ( intent );
                        finish ();
                    }else{
                        Toast.makeText ( UploadActivity.this, "Something went wrong!! ", Toast.LENGTH_SHORT ).show ();
                    }

                }

                @Override
                public
                void onFailure ( Call<Integer> call, Throwable t ) {
                    Toast.makeText ( UploadActivity.this, "Something went wrong!! ", Toast.LENGTH_SHORT ).show ();
                    progressDialog.dismiss ();
                }
            } );


        }else{
            Toast.makeText(UploadActivity.this, "Please write your post first", Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    protected
    void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data ) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image selectedImage = ImagePicker.getFirstImageOrNull( data);

            try {
                compressedImageFile = new Compressor ( this)
                        .setQuality(75)
                        .compressToFile(new File(selectedImage.getPath()));

                isImageSelected = true;

                Picasso.get ().load( new File( selectedImage.getPath())).placeholder( R.drawable.default_image_placeholder).into( image);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult ( requestCode, resultCode, data );
    }
}
