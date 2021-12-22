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


public class company_fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    adapter adapter;
    String company;
    ImageButton cut;
    TextView name_com;
    private RecyclerView recyclerView;
    public company_fragment() {
        // Required empty public constructor
    }

    public company_fragment(String company){
        this.company=company;
    }

    public static company_fragment newInstance(String param1, String param2) {
        company_fragment fragment = new company_fragment();
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
        View view=inflater.inflate(R.layout.fragment_company_fragment, container, false);
        recyclerView=view.findViewById(R.id.comapny_cate_recycler);
        cut=view.findViewById(R.id.cut_company);
        name_com=view.findViewById(R.id.company_top);
        name_com.setText(company);
        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Fragment temp=new categoryFragment();
                 getChildFragmentManager().beginTransaction().replace(R.id.company_freg,temp).commit();
                 getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("products").orderByChild("company").startAt(company).endAt(company+"\uf8ff"), model.class)
                        .build();
        adapter=new adapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
//        Toast.makeText(getContext(),company,Toast.LENGTH_SHORT).show();
        return view;
    }

}