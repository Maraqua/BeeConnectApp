package com.marty.beeconnect.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.marty.beeconnect.LoginActivity;
import com.marty.beeconnect.R;

public class IdeasFragment extends Fragment {
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    private View.OnClickListener mOnClickListener = new View.OnClickListener () {
        @Override
        public
        void onClick ( View v ) {
            signout();
        }
    };

    private
    void signout () {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent ( getActivity (), LoginActivity.class );
        intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity ( intent );

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ideas, container, false);
        Button btnLogout =(Button) view.findViewById (R.id.logout);
        btnLogout.setOnClickListener ( mOnClickListener );
        return view;

    }

}
