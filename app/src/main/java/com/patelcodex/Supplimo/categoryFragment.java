package com.patelcodex.Supplimo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.patelcodex.Supplimo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class categoryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private company_model company_model;

    public categoryFragment() {
    }

    public static categoryFragment newInstance(String param1, String param2) {
        categoryFragment fragment = new categoryFragment();
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
        View view=inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView=view.findViewById(R.id.company_recyler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        return view;
    }

    @Override
    public void onStart() {
        FirebaseRecyclerOptions<company_model> options=new FirebaseRecyclerOptions.Builder<company_model>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("companies"), company_model.class)
                .build();
        FirebaseRecyclerAdapter<company_model,company_viewholder> adapter=new FirebaseRecyclerAdapter<company_model, company_viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull company_viewholder holder, int position, @NonNull company_model model) {
                Glide.with(holder.imageView.getContext()).load(model.getCo_url()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity=(AppCompatActivity)v.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new company_fragment(model.getCompany())).addToBackStack(null).commit();

                    }
                });
            }

            @NonNull
            @Override
            public company_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.company_box,parent,false);
                company_viewholder holder=new company_viewholder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        super.onStart();
    }
}