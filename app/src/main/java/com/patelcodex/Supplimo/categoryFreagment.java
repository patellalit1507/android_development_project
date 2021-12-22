package com.patelcodex.Supplimo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.patelcodex.Supplimo.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class categoryFreagment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String name;
    private TextView products_name,categoryname;
    private RecyclerView recyclerView;
    private ImageButton cut;
    adapter adapter;

    public categoryFreagment() {
    }

    public categoryFreagment(String name) {
        this.name=name.replace("_","");
    }

    public static categoryFreagment newInstance(String param1, String param2) {
        categoryFreagment fragment = new categoryFreagment();
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
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_category_freagment, container, false);
        categoryname =view.findViewById(R.id.category_top);
        cut=view.findViewById(R.id.cut_category);
        String newn=name.replace("cat","").replace("fra","");
//        Toast.makeText(getContext(),newn,Toast.LENGTH_SHORT).show();

        categoryname.setText(newn);
        recyclerView=view.findViewById(R.id.category_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("products").orderByChild("category").startAt(newn).endAt(newn+"\uf8ff"), model.class)
                        .build();
        adapter=new adapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment temp=new homeFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.ctaegory_id_layout,temp).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;

    }

}