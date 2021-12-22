package com.patelcodex.Supplimo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.patelcodex.Supplimo.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class descfragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    String id,name,company,p1,url,uid,w1,p2,p3,w2,w3,ct_pr,ct_w,temp_id,category;
    Button addtocart,buynow;
    public TextView pro_name,pro_company,pro_price;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private DatabaseReference dbr;
    private ImageSlider img_slider;
    private PDFView pdfView;
    private RadioGroup radioGroup;
    private RadioButton item1,item2,item3;

    public descfragment() {
    }
    public descfragment(String id,String name,String company,String p1,String url,String w1,String category) {
        this.id=id;
        this.name=name;
        this.company=company;
        this.p1=p1;
        this.url=url;
        this.w1=w1;
        this.category=category;
    }

    public static descfragment newInstance(String param1, String param2) {
        descfragment fragment = new descfragment();
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
        View view=inflater.inflate(R.layout.fragment_descfragment, container, false);
        img_slider=view.findViewById(R.id.image_slider);
        pro_name=view.findViewById(R.id.pro_view_name);
        pro_company=view.findViewById(R.id.pro_view_company);
        addtocart=view.findViewById(R.id.addtocart);
        buynow=view.findViewById(R.id.buy_now);
        pdfView=view.findViewById(R.id.desc_pdf);
        pdfView.fitToWidth();
        pdfView.setMinZoom(1);
        pdfView.setMaxZoom(1);
        radioGroup=view.findViewById(R.id.pro_view_price);
        item1=view.findViewById(R.id.item_1);
        item2=view.findViewById(R.id.item_2);
        item3=view.findViewById(R.id.item_3);

        item1.setText("₹"+p1+" ("+w1+")");
        item1.setChecked(true);

        pro_name.setText(name);
        pro_company.setText(company);
//        pro_price.setText("₹"+p1);

        final List<SlideModel> imageslider=new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();
        fdb=FirebaseDatabase.getInstance();
        dbr=fdb.getReference();
//        View nv=LayoutInflater.from(getActivity()).inflate(R.layout.activity_main,null);
        View view1=getActivity().findViewById(R.id.searchview);
//        SearchView sc=(SearchView) nv.findViewById(R.id.searchview);
        view1.setVisibility(View.INVISIBLE);

        dbr.child("images_url").child(id)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot ds=task.getResult();
                imageslider.add(new SlideModel(ds.child("0").getValue().toString(), ScaleTypes.CENTER_INSIDE));
                imageslider.add(new SlideModel(ds.child("1").getValue().toString(), ScaleTypes.CENTER_INSIDE));
                imageslider.add(new SlideModel(ds.child("2").getValue().toString(), ScaleTypes.CENTER_INSIDE));
                imageslider.add(new SlideModel(ds.child("3").getValue().toString(), ScaleTypes.CENTER_INSIDE));
                img_slider.setImageList(imageslider,ScaleTypes.CENTER_INSIDE);
                img_slider.stopSliding();
            }
        });

        dbr.child("products").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String desc_url=snapshot.child("desc").getValue().toString();
                try{
                    p2=snapshot.child("p2").getValue().toString();
                    p3=snapshot.child("p3").getValue().toString();
                    w2=snapshot.child("w2").getValue().toString();
                    w3=snapshot.child("w3").getValue().toString();
                    item2.setText("₹"+p2+" ("+w2+")");
                    if(w3.equals("")){
                        item3.setVisibility(View.INVISIBLE);
                    }
                    else {
                        item3.setText("₹" + p3 + " (" + w3 + ")");
                    }
                }
                catch (Exception e){}

//                Toast.makeText(getContext(),desc_url,Toast.LENGTH_SHORT).show();
                new retrivingPdf().execute(desc_url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"error in retriving data",Toast.LENGTH_SHORT).show();
            }
        });



        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();

            }
        });
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
                FirebaseUser user=mAuth.getCurrentUser();
                if(user!=null){
                    Intent intent=new Intent(getContext(),cart_activity.class);
                    startActivity(intent);
                }

            }
        });
        return view;
    }
    class retrivingPdf extends AsyncTask<String,Void, InputStream>{

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream=null;
            try {
                URL url1=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection)url1.openConnection();
                if(urlConnection.getResponseCode()==200){
                    inputStream=new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            catch (IOException e){
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
//            Toast.makeText(getContext(),inputStream.toString(),Toast.LENGTH_SHORT).show();
            pdfView.fromStream(inputStream).load();
        }
    }

    public void order(){
        FirebaseUser user=mAuth.getCurrentUser();
//        Toast.makeText(getContext(),user.toString(),Toast.LENGTH_SHORT).show();
        if(user!=null){
            uid=mAuth.getCurrentUser().getUid();
            switch (radioGroup.getCheckedRadioButtonId()){
                case R.id.item_1:
                    ct_pr=p1;
                    ct_w=w1;
                    temp_id=id+"1";
                    break;
                case R.id.item_2:
                    ct_pr=p2;
                    ct_w=w2;
                    temp_id=id+"2";
                    break;
                case R.id.item_3:
                    ct_pr=p3;
                    ct_w=w3;
                    temp_id=id+"3";
                    break;
            }
            DatabaseReference newrf=dbr.child("users").child(uid).child("cart");
            Map<String, Object> cart = new HashMap<>();
            Map<String, Object> item = new HashMap<>();
            item.put("item",temp_id);
            item.put("url",url);
            item.put("name",name);
            item.put("price",ct_pr);
            item.put("weight",ct_w);
            item.put("company",company);
            item.put("quant","1");
            item.put("category",category);
            cart.put(temp_id,item);
            newrf.updateChildren(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(),"added to cart",Toast.LENGTH_SHORT).show();
                        addtocart.setText("added to cart");
                    }
                }
            });
        }
        else{
            my_signup_frag loginf=new my_signup_frag();
            loginf.show(getChildFragmentManager(),"my_frag");
        }
    }
}