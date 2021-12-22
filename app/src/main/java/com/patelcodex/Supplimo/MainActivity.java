package com.patelcodex.Supplimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.patelcodex.Supplimo.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bnv;
    public Fragment temp=null,temp2=null;
    public Toolbar toolbar;
    public SearchView searchView;
    public DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private ImageButton gotocart;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private NavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new homeFragment()).commit();
//      setSupportActionBar(toolbar);
        toolbar=findViewById(R.id.toolbar);
        drawerLayout=findViewById(R.id.drawer);
        gotocart=findViewById(R.id.gotocart);
        searchView=findViewById(R.id.searchview);
        bnv=findViewById(R.id.bottom_navigation);
        nav=findViewById(R.id.navi_menu);
        nav.setItemTextAppearance(R.style.menu_text_style);


//      adding drawer toogle button to toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//      go to cart option
        gotocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=mAuth.getCurrentUser();
                if(user!=null){
                    Intent intent=new Intent(MainActivity.this,cart_activity.class);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    startActivity(intent);
                }
                else {
                    my_signup_frag login=new my_signup_frag();
                    login.show(getSupportFragmentManager(),"login_frag");
                }

            }
        });

// implement search view in firebase recycler view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                prossessearch(query.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                prossessearch(newText.toLowerCase());
                return false;
            }
        });

//        adding bottom navigation item
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_home:
                        temp=new homeFragment();
                        searchView.setVisibility(View.VISIBLE);
                        break;
                    case R.id.main_category:temp=new categoryFragment();
                        searchView.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.main_profile:temp=new profileFragment();
                        searchView.setVisibility(View.INVISIBLE);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,temp).commit();
                return true;
            }
        });

//        on click item of nav_menu
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new homeFragment()).commit();
                        bnv.setSelectedItemId(R.id.main_home);
                        break;

                    case R.id.menu_profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new profileFragment()).commit();
                        bnv.setSelectedItemId(R.id.main_profile);
                        break;

                    case R.id.menu_help:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent=new Intent(MainActivity.this,help_activity.class);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        startActivity(intent);
                        break;

                    case R.id.menu_aboutus:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent ab_intnt=new Intent(MainActivity.this,about_us.class);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        startActivity(ab_intnt);
                        break;
                    case R.id.menu_share:

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                        sendIntent.setType("text/plain");
                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                        drawerLayout.closeDrawer(GravityCompat.START);break;
                }

                return true;
            }
        });
    }

    private void prossessearch(String query) {
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("products").orderByChild("name").startAt(query).endAt(query+"\uf8ff"), model.class)
                        .build();
//        startAt(query).endAt(query+"\uf8ff")
        adapter adapter=new adapter(options);
        adapter.startListening();
        temp=new homeFragment();
        recyclerView=findViewById(R.id.recyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    public void opencategory(View v){
        AppCompatActivity activity=(AppCompatActivity)v.getContext();
        int resid=v.getId();
        String name=getResources().getResourceEntryName(resid);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new categoryFreagment(name)).addToBackStack(null).commit();
//        activity.getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    public void onBackPressed() {
        int count=getSupportFragmentManager().getBackStackEntryCount();
        if(bnv.getSelectedItemId()==R.id.main_home) {
            super.onBackPressed();
        }
        else{
            getSupportFragmentManager().popBackStack();
            bnv.setSelectedItemId(R.id.main_home);
        }
    }
}