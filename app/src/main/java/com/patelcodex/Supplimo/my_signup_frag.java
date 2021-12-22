package com.patelcodex.Supplimo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.patelcodex.Supplimo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class my_signup_frag extends DialogFragment {
    EditText f_name,l_name,phone,otp;
    Button get_otp,verifiy_otp;
    CountryCodePicker ccp;
    public String first_name,last_name;
    private String temp_no,phone_no,str_otp,otp_id,verification_id,token,token2;
    private FirebaseAuth mAuth;
    FirebaseDatabase fdb;
    DatabaseReference dbref;
//    profileFragment profileFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.signup_fragment,container,false);

        f_name=view.findViewById(R.id.first_name);
        l_name=view.findViewById(R.id.last_name);
        phone=view.findViewById(R.id.mobile_no);
        verifiy_otp=view.findViewById(R.id.verify_otp);
        get_otp=view.findViewById(R.id.get_otp);
        otp=view.findViewById(R.id.otp);
        ccp = view.findViewById(R.id.countryCodePicker);


        verifiy_otp.setVisibility(View.INVISIBLE);
        otp.setVisibility(View.INVISIBLE);
//      profileFragment=new profileFragment();

        mAuth=FirebaseAuth.getInstance();
        fdb=FirebaseDatabase.getInstance();
        dbref=fdb.getReference();

        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp_no = phone.getText().toString();
                first_name=f_name.getText().toString();
                last_name=l_name.getText().toString();
                if (temp_no.equals("")) {
                    Toast.makeText(getContext(), "fill mobile_no", Toast.LENGTH_SHORT).show();
                }
                else if(first_name.equals("")){
                    Toast.makeText(getContext(), "enter first name", Toast.LENGTH_SHORT).show();
                }
                else if(last_name.equals("")){
                    Toast.makeText(getContext(), "enter last name", Toast.LENGTH_SHORT).show();
                }
                else if (temp_no.length() == 10) {
                    ccp.registerCarrierNumberEditText(phone);
                    phone_no = ccp.getFullNumberWithPlus().replace(" ", "").toString();
                    initiateOtp();
                    otp.setVisibility(View.VISIBLE);
                    verifiy_otp.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Please check no", Toast.LENGTH_SHORT).show();
                }
            }
        });

        verifiy_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please Enter otp", Toast.LENGTH_SHORT).show();
                } else if (otp.getText().toString().length() != 6) {
                    Toast.makeText(getContext(), "Enter valid otp", Toast.LENGTH_SHORT).show();
                } else {
                     verifyCode();
                }
            }
        });
        return view;
    }

    private void initiateOtp(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone_no,
                30,
                TimeUnit.SECONDS,
                getActivity(),
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                       otp_id=s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }
                }

        );
    }


    private void verifyCode() {
        // below line is used for getting getting
        // credentials from our verification id and code.
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp_id,otp.getText().toString());
            signInWithPhoneAuthCredential(credential);
        }
        catch (Exception e){
            Toast.makeText(getContext(),"check sim is in same device",Toast.LENGTH_SHORT).show();
        }
        // after getting credential we are
        // calling sign in method.

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            String uid=mAuth.getCurrentUser().getUid().toString();
                            Map<String, Object> user_info = new HashMap<String,Object>();
                            user_info.put("fname",first_name);
                            user_info.put("lname",last_name);
                            user_info.put("phone",phone_no);
                            try {
                                DatabaseReference newref=dbref.child("users").child(uid);
                                newref.updateChildren(user_info);
                            }
                            catch (Exception e){
                                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            }


                            Intent i = new Intent(getContext(), MainActivity.class);

                            startActivity(i);
                            dismiss();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
