package com.patelcodex.Supplimo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.patelcodex.Supplimo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class cart_set_quantity extends DialogFragment {
    RadioGroup radioGroup;
    RadioButton radioButton,one;
    Button done_btn;
    DatabaseReference dbrf;
    FirebaseAuth mAuth;
    private cart_activity c_a;
    int id;
    String qua_radio_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        String itemid=getArguments().getString("item");
        View view=inflater.inflate(R.layout.quantity_dialog_box,container,false);
        radioGroup=(RadioGroup)view.findViewById(R.id.radio_group);
        one=view.findViewById(R.id.one);
        one.setChecked(true);
        done_btn=(Button)view.findViewById(R.id.done_btn);
        mAuth=FirebaseAuth.getInstance();
        String uid=mAuth.getCurrentUser().getUid().toString();
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=radioGroup.getCheckedRadioButtonId();
                radioButton=radioGroup.findViewById(id);
                qua_radio_btn=radioButton.getText().toString();
                dbrf= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("cart").child(itemid);
                Map<String,Object> qua=new HashMap<>();
                qua.put("quant",qua_radio_btn);
                dbrf.updateChildren(qua)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();
//                                Intent intent=new Intent(getContext(),cart_activity.class);
//                                startActivity(intent);
                                c_a.recreate();
                                dismiss();
                            }
                        });

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
