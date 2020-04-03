package com.marty.beeconnect.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.marty.beeconnect.R;
import com.marty.beeconnect.adapter.IdeasAdapter;
import com.marty.beeconnect.model.IdeasModel;
import com.marty.beeconnect.rest.ApiClient;
import com.marty.beeconnect.rest.services.UserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public
class IsHotFragment extends Fragment {
    Context context;
    @BindView(R.id.ideas)
    RecyclerView ideas;
    @BindView(R.id.ideasProgressBar)
    ProgressBar ideasProgressBar;
    Unbinder unbinder;

    int limit = 5;
    int offset = 0;

    boolean isFromStart = true;
    IdeasAdapter ideasAdapter;
    List<IdeasModel> ideasModels = new ArrayList<> ();




    @Override
    public
    void onAttach ( Context context ) {
        super.onAttach ( context );
        this.context = context;
    }

    @Nullable
    @Override
    public
    View onCreateView ( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View view = inflater.inflate ( R.layout.fragment_ideas, container, false );

        unbinder = ButterKnife.bind ( this, view );
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager ( context );
        ideas.setLayoutManager ( linearLayoutManager );
        ideasAdapter = new IdeasAdapter ( context, ideasModels );

        ideas.setAdapter ( ideasAdapter );
        ideas.addOnScrollListener ( new RecyclerView.OnScrollListener () {
            @Override
            public
            void onScrolled ( @NonNull RecyclerView recyclerView, int dx, int dy ) {
                int visibleItemCount = linearLayoutManager.getChildCount ();
                int totalItemCount = linearLayoutManager.getItemCount ();
                int passVisibleItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition ();
                if(passVisibleItems+visibleItemCount>=(totalItemCount)){
                    isFromStart=false;
                    ideasProgressBar.setVisibility ( View.VISIBLE );
                    offset=offset+limit;
                    loadAllPosts ();
                }
            }
        } );
        return view;
    }

    @Override
    public
    void onStart () {
        super.onStart ();
        isFromStart = true;
        offset = 0;
        loadAllPosts();


    }

    private
    void loadAllPosts () {

        UserInterface userInterface = ApiClient.getApiClient ().create ( UserInterface.class );
        Map<String,String>params = new HashMap<String,String> ();
        params.put( "uid", FirebaseAuth.getInstance ().getCurrentUser ().getUid () );
        params.put("limit", limit + "");
        params.put("offset", offset + "");
        Call<List<IdeasModel>> ideasModelCall = userInterface.getHotPosts( params );
        ideasModelCall.enqueue ( new Callback<List<IdeasModel>> () {
            @Override
            public
            void onResponse ( Call<List<IdeasModel>> call, Response<List<IdeasModel>> response ) {
                ideasProgressBar.setVisibility ( View.GONE );
                if(response.body () != null){
                    ideasModels.addAll (  response.body ());
                    if(isFromStart){
                        ideas.setAdapter ( ideasAdapter );

                    }else {
                        ideasAdapter.notifyItemRangeChanged (ideasModels.size (),response.body ().size ());
                    }
                }
            }

            @Override
            public
            void onFailure ( Call<List<IdeasModel>> call, Throwable t ) {
                ideasProgressBar.setVisibility ( View.GONE );
                Toast.makeText(context,"Something went wrong !",Toast.LENGTH_SHORT).show();



            }
        } );




    }

    @Override
    public
    void onDestroy () {
        super.onDestroy ();
        ideasModels.clear ();
        ideasAdapter.notifyDataSetChanged ();
    }

    @Override
    public
    void onDestroyView () {
        super.onDestroyView ();
        unbinder.unbind ();
    }
}
