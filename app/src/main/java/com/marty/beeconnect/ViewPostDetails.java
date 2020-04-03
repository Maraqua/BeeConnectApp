package com.marty.beeconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.marty.beeconnect.model.IdeasModel;
import com.marty.beeconnect.rest.ApiClient;
import com.marty.beeconnect.util.AgoDateParse;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

public
class ViewPostDetails extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.post_user_image)
    ImageView postUserImage;
    @BindView(R.id.post_user_name)
    TextView postUserName;
    @BindView(R.id.post_date)
    TextView postDate;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.post_image)
    ImageView postImage;
    @BindView(R.id.top_rel)
    RelativeLayout topRel;
    @BindView(R.id.vote_img)
    ImageView voteImg;
    @BindView(R.id.vote_txt)
    TextView voteTxt;
    @BindView(R.id.vote_section)
    LinearLayout voteSection;
    @BindView(R.id.comment_txt)
    TextView commentTxt;
    @BindView(R.id.comment_section)
    LinearLayout commentSection;
    @BindView(R.id.reaction_card)
    CardView reactionCard;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.comment_send)
    ImageView commentSend;
    @BindView(R.id.comment_send_wrapper)
    RelativeLayout commentSendWrapper;
    @BindView(R.id.comment_bottom_part)
    LinearLayout commentBottomPart;
    @BindView(R.id.top_hide_show)
    RelativeLayout topHideShow;

    IdeasModel ideasModel;
    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_view_post_details );
        ButterKnife.bind ( this );
        ideasModel = Parcels.unwrap (getIntent ().getBundleExtra ( "ideasBundle" ).getParcelable ( "ideasModel" ));
        if(ideasModel==null){
            Toast.makeText (this,"Something went wrong!!",Toast.LENGTH_SHORT).show ();
            onBackPressed ();
            finish ();
        }
        setSupportActionBar ( toolbar );
        toolbar.setTitle ( "" );
        toolbar.setNavigationIcon ( R.drawable.ic_arrow_back_black_24dp );
        toolbar.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public
            void onClick ( View v ) {
                onBackPressed ();
            }
        } );
        setData(ideasModel);
    }

    private
    void setData ( IdeasModel ideasModel ) {
        postUserName.setText ( ideasModel.getName () );
        status.setText ( ideasModel.getPost () );
        if(!ideasModel.getProfileUrl ().isEmpty ()){
            Picasso.get ().load ( ideasModel.getProfileUrl () ).networkPolicy ( NetworkPolicy.OFFLINE ).placeholder ( R.drawable.default_image_placeholder ).into ( postUserImage, new com.squareup.picasso.Callback(){

                @Override
                public
                void onSuccess () {

                }

                @Override
                public
                void onError ( Exception e ) {
                    Picasso.get ().load ( ideasModel.getProfileUrl () ).placeholder ( R.drawable.default_image_placeholder ).into ( postUserImage );
                }
            });
        }
        if(!ideasModel.getStatusImage().isEmpty()){
            Picasso.get ().load( ApiClient.BASE_URL_1+ideasModel.getStatusImage()).networkPolicy( NetworkPolicy.OFFLINE).placeholder( R.drawable.default_image_placeholder).into( postImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public
                void onError ( Exception e ) {
                    Picasso.get ().load(ApiClient.BASE_URL_1+ideasModel.getStatusImage()).placeholder(R.drawable.default_image_placeholder).error(R.drawable.default_image_placeholder).into(postImage);

                }


            });
        }else{
            postImage.setVisibility(View.GONE);
        }
        try {
            postDate.setText ( AgoDateParse .getTimeAgo ( AgoDateParse.getTimeInMillsecond ( ideasModel.getStatusTime () ) ));
        } catch (ParseException e) {
            e.printStackTrace ();
        }

    }
}
