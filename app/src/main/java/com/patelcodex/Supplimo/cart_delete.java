package com.patelcodex.Supplimo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.patelcodex.Supplimo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class cart_delete extends DialogFragment {
    Button cancel,delete;
    FirebaseAuth mAuth;
    DatabaseReference dbref;
    String uid,pro_id;
    private cart_activity c_a;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.remove_cart_obj,container,false);
        cancel=view.findViewById(R.id.cancel_del);
        delete=view.findViewById(R.id.delete_confirm);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        pro_id=getArguments().getString("pro_id");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbref= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("cart").child(pro_id);
                dbref.removeValue();
                c_a.recreate();
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try{
            c_a=(cart_activity) activity;
        }
        catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement FeedbackListener");
            }

    }
}
