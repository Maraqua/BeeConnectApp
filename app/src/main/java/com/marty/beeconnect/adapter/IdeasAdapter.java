package com.marty.beeconnect.adapter;


import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.protobuf.Api;
import com.marty.beeconnect.R;
import com.marty.beeconnect.ViewPostDetails;
import com.marty.beeconnect.model.IdeasModel;
import com.marty.beeconnect.model.User;
import com.marty.beeconnect.rest.ApiClient;
import com.marty.beeconnect.rest.services.UserInterface;
import com.marty.beeconnect.util.AgoDateParse;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdeasAdapter extends RecyclerView.Adapter<IdeasAdapter.ViewHolder> {
    Context context;
    List<IdeasModel> ideasModels;

    public  IdeasAdapter (Context context,List<IdeasModel> ideasModels ) {
        this.context = context;
        this.ideasModels = ideasModels;


    }

    @NonNull
    @Override
    public
    ViewHolder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.item_ideas_list, parent, false );
        return new ViewHolder ( view );
    }

    @Override
    public
    void onBindViewHolder ( @NonNull ViewHolder holder, int position ) {
    final IdeasModel ideasModel = ideasModels.get ( position );
    if(ideasModel.getPost ()!=null && ideasModel.getPost ().length ()>1){
        holder.post.setText ( ideasModel.getPost () );
    }else {
        holder.post.setVisibility ( View.GONE );

    }
    holder.peopleName.setText ( ideasModel.getName () );
    if(ideasModel.isVoted ()){
        holder.voteImg.setImageResource ( R.drawable.icon_like_selected );
    }else {
        holder.voteImg.setImageResource ( R.drawable.icon_like );

    }
    if(ideasModel.getVoteCount ().equals ( "0" ) || ideasModel.getVoteCount ().equals ( "1" )){
        holder.voteTxt.setText(ideasModel.getVoteCount ()+" Vote");
    }else{
        holder.voteTxt.setText(ideasModel.getVoteCount ()+" Votes");
    }
    holder.voteSection.setOnClickListener ( v -> {
        holder.voteSection.setEnabled ( false );

        if(!ideasModel.isVoted ()){
                //vote
            operationVote ( holder,ideasModel );
            UserInterface userInterface = ApiClient.getApiClient ().create ( UserInterface.class );
            Call<Integer> call = userInterface.voteDownvote ( new AddVote ( FirebaseAuth.getInstance ().getCurrentUser ().getUid (),ideasModel.getPostId (),ideasModel.getPostUserId (),1 ) );
            call.enqueue ( new Callback<Integer> () {
                @Override
                public
                void onResponse ( Call<Integer> call, Response<Integer> response ) {
                    holder.voteSection.setEnabled ( true );
                    if(response.body ().equals ( "0" )){
                       operationDownVote ( holder,ideasModel );
                        Toast.makeText(context,"Something went wrong ! ",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public
                void onFailure ( Call<Integer> call, Throwable t ) {
                holder.voteSection.setEnabled ( true );
                operationDownVote ( holder,ideasModel );
                Toast.makeText(context,"Something went wrong ! ",Toast.LENGTH_SHORT).show();

                }
            } );
        }else {
            //downvote
            operationDownVote ( holder,ideasModel );
            UserInterface userInterface = ApiClient.getApiClient ().create ( UserInterface.class );
            Call<Integer> call = userInterface.voteDownvote ( new AddVote ( FirebaseAuth.getInstance ().getCurrentUser ().getUid (),ideasModel.getPostId (),ideasModel.getPostUserId (),0 ) );
            call.enqueue ( new Callback<Integer> () {
                @Override
                public
                void onResponse ( Call<Integer> call, Response<Integer> response ) {
                    holder.voteSection.setEnabled ( true );
                    if(response.body ()==null){
                        operationVote ( holder,ideasModel );
                        Toast.makeText(context,"Something went wrong ! ",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public
                void onFailure ( Call<Integer> call, Throwable t ) {
                    holder.voteSection.setEnabled ( true );
                    operationVote ( holder,ideasModel );
                    Toast.makeText(context,"Something went wrong ! ",Toast.LENGTH_SHORT).show();

                }
            } );
        }
    } );
        if(!ideasModel.getStatusImage ().isEmpty ()){
            Picasso.get ().load ( ApiClient.BASE_URL_1+ideasModel.getStatusImage () ).networkPolicy ( NetworkPolicy.OFFLINE ).placeholder ( R.drawable.default_image_placeholder ).into ( holder.statusImage, new com.squareup.picasso.Callback(){

                @Override
                public
                void onSuccess () {

                }

                @Override
                public
                void onError ( Exception e ) {
                Picasso.get ().load ( ApiClient.BASE_URL_1+ideasModel.getStatusImage () ).placeholder ( R.drawable.default_image_placeholder ).into ( holder.statusImage );
                }
            });


        }else {
            holder.statusImage.setImageDrawable (null);
        }
        try {
            holder.date.setText ( AgoDateParse.getTimeAgo ( AgoDateParse.getTimeInMillsecond ( ideasModel.getStatusTime () ) ) );
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        if(!ideasModel.getProfileUrl ().isEmpty ()){
            Picasso.get ().load ( ideasModel.getProfileUrl () ).networkPolicy ( NetworkPolicy.OFFLINE ).placeholder ( R.drawable.default_image_placeholder ).into ( holder.peopleImage, new com.squareup.picasso.Callback(){

                @Override
                public
                void onSuccess () {

                }

                @Override
                public
                void onError ( Exception e ) {
                    Picasso.get ().load ( ideasModel.getProfileUrl () ).placeholder ( R.drawable.default_image_placeholder ).into ( holder.peopleImage );
                }
            });
        }
holder.itemView.setOnClickListener ( new View.OnClickListener () {
    @Override
    public
    void onClick ( View v ) {
        Intent intent = new Intent ( context, ViewPostDetails.class);
        Bundle bundle = new Bundle ( );
        bundle.putParcelable ( "ideasModel", Parcels.wrap ( ideasModel )  );
        intent.putExtra (  "ideasBundle",bundle);
        context.startActivity ( intent );

    }
} );
    }
    private void operationVote(@NonNull ViewHolder holder, IdeasModel ideasModel) {
        holder.voteImg.setImageResource(R.drawable.icon_like_selected);
        int count = Integer.parseInt(ideasModel.getVoteCount ());
        count++;
        if (count == 0 || count == 1) {
            holder.voteTxt.setText(count + " Vote");
        } else {
            holder.voteTxt.setText(count + " Votes");
        }

        ideasModels.get(holder.getAdapterPosition()).setVoteCount (count + "");
        ideasModels.get(holder.getAdapterPosition()).setVoted (true);
    }

    private void operationDownVote(@NonNull ViewHolder holder,IdeasModel ideasModel) {


        holder.voteImg.setImageResource(R.drawable.icon_like);
        int count = Integer.parseInt(ideasModel.getVoteCount ());
        count--;

        if (count == 0 || count == 1) {
            holder.voteTxt.setText(count + " Vote");
        } else {
            holder.voteTxt.setText(count + " Votes");
        }
        ideasModels.get(holder.getAdapterPosition()).setVoteCount(count + "");
        ideasModels.get(holder.getAdapterPosition()).setVoted (false);


    }


    @Override
    public
    int getItemCount () {

        return ideasModels.size ();
    }



    public
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.people_image)
        ImageView peopleImage;
        @BindView(R.id.people_name)
        TextView peopleName;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.memory_meta_rel)
        RelativeLayout memoryMetaRel;
        @BindView(R.id.post)
        TextView post;
        @BindView(R.id.status_image)
        ImageView statusImage;
        @BindView(R.id.vote_img)
        ImageView voteImg;
        @BindView(R.id.vote_txt)
        TextView voteTxt;
        @BindView(R.id.voteSection)
        LinearLayout voteSection;
        @BindView(R.id.room_img)
        ImageView roomImg;
        @BindView(R.id.room_txt)
        TextView roomTxt;
        @BindView(R.id.roomSection)
        LinearLayout roomSection;
        public ViewHolder ( @NonNull View itemView ) {
            super ( itemView );
            ButterKnife.bind ( this, itemView );
        }
    }
//    userId
//    postId
//    contentOwnerId
//    operationType

    public static class AddVote {
        String userId, postId, contentOwnerId;
        int operationType;

        public AddVote(String userId, String postId, String contentOwnerId, int operationType) {
            this.userId = userId;
            this.postId = postId;
            this.contentOwnerId = contentOwnerId;
            this.operationType = operationType;
        }
    }
}
