package com.patelcodex.Supplimo;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.patelcodex.Supplimo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

public class profileFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button sign_out;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private DatabaseReference dbref;
    private StorageReference stref;
    private FirebaseUser nuser,user;
    private ImageView profileimg;
    private Uri img_uri;
    TextView username,user_phone_no;
    String f__name,l__name,uid,prof_url,number;
    CardView orders,address,help;

    public profileFragment() {

    }


    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        sign_out=view.findViewById(R.id.sign_out);
        username=view.findViewById(R.id.username);
        orders=view.findViewById(R.id.order_card);
//        address=view.findViewById(R.id.address_card);
        help=view.findViewById(R.id.help_card);
        profileimg=view.findViewById(R.id.profile_image);
        user_phone_no=view.findViewById(R.id.user_phone);


        fdb=FirebaseDatabase.getInstance();
        dbref=fdb.getReference("users");
        stref= FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        my_signup_frag loginf=new my_signup_frag();

//        checking existence of user
        if(user!=null){
            uid=mAuth.getCurrentUser().getUid();
            dbref.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                       DataSnapshot dataSnapshot=task.getResult();
                       prof_url=String.valueOf(dataSnapshot.child("profile").getValue());
                       f__name=String.valueOf(dataSnapshot.child("fname").getValue());
                       l__name=String.valueOf(dataSnapshot.child("lname").getValue());
                       number=String.valueOf(dataSnapshot.child("phone").getValue());
                       if(prof_url.isEmpty()){
                           profileimg.setImageResource(R.drawable.addaccount);
                       }
                       else{
                           Glide.with(profileimg.getContext()).load(prof_url).into(profileimg);
                       }
                       String  temp=f__name.toUpperCase()+" "+l__name.toUpperCase();
                       username.setText(temp);
                       user_phone_no.setText(number);
                    }
                    else {
                        Toast.makeText(getContext(), "error in retriving data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            profileimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    crop_img();
                }
            });
        }
        else{
            username.setText("USER");
            sign_out.setText("SIGN IN");
        }

//        when signout button is clicked
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuser=mAuth.getCurrentUser();
                if(nuser==null){
                    my_signup_frag login_frag2=new my_signup_frag();
                    login_frag2.show(getChildFragmentManager(),"login");
//                    onResume();
                }

                else{
                mAuth.signOut();
                username.setText("USER");
                sign_out.setText("SIGN IN");
                user_phone_no.setText("");
                profileimg.setImageResource(R.drawable.addaccount);
                }
            }
        });

//        add click listners on order card view
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),orders_activity.class);
                user=mAuth.getCurrentUser();
                if(user!=null){
                    uid=mAuth.getCurrentUser().getUid();
                    intent.putExtra("uid",uid);
                    startActivity(intent);
                }
                else{
                    loginf.show(getChildFragmentManager(),"login");
                }
            }
        });



//        add click listner on help card view
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),help_activity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(user!=null){
            uid=mAuth.getCurrentUser().getUid();
            dbref.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        DataSnapshot dataSnapshot=task.getResult();
                        f__name=String.valueOf(dataSnapshot.child("fname").getValue());
                        l__name=String.valueOf(dataSnapshot.child("lname").getValue());
                        username.setText(f__name.toUpperCase()+" "+l__name.toUpperCase());
                    }
                    else {
                        Toast.makeText(getContext(), "error in retriving data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK &&data!=null){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            img_uri=result.getUri();
            upload_profile();
        }
        else {
            Toast.makeText(getContext(),"error in croping img",Toast.LENGTH_SHORT).show();
        }
    }

    private void crop_img() {
        CropImage.activity().setAspectRatio(1,1).start(getContext(),this);
    }
    private void upload_profile(){
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("updating profile");
        progressDialog.setMessage("uploading");
        progressDialog.show();
        if(img_uri!=null){
            stref.child("users").child(mAuth.getCurrentUser().getUid()).child(mAuth.getCurrentUser().getUid()+".jpg")
                    .putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                String down_url=task.getResult().toString();
                                Map<String,Object> img=new HashMap<String,Object>();
                                img.put("profile",down_url);
                                dbref.child(mAuth.getCurrentUser().getUid()).updateChildren(img).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

//                                        Toast.makeText(getContext(),"added profile successfully",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        profileimg.setImageURI(img_uri);
                                    }
                                });
                            }
                        }
                    }) ;
                }
            });
        }
    }
}