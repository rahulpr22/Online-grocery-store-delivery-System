package com.example.onlineglossary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@Deprecated
public class Dashboard extends AppCompatActivity {
    ListView simpleList;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    CircleImageView cart,profi;
    private static String userid;
    private Session session;//global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        cart= findViewById(R.id.carticon);
        profi= findViewById(R.id.profileLogo);

        this.userid= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        Log.d("userid",userid);
        getusername(userid);
        session = new Session(getApplicationContext()); //in oncreate
        session.setusename(userid);





        Log.d("reached","reached");
        if (FirebaseDatabase.getInstance() != null)
        {
            FirebaseDatabase.getInstance().goOffline();
        }
        FirebaseDatabase.getInstance().goOnline();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("products");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<productgrid> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    productgrid p = ds.getValue(productgrid.class);
                    Log.d("test","title "+p.getTitle()+" desc "+p.getDescription());
                    list.add(p);
                }
                Log.d("TAG", String.valueOf(list.size())); //To see is not emplty
                simpleList = findViewById(R.id.simpleGridView);
                productAdapter myAdapter=new productAdapter(getApplicationContext(), (ArrayList<productgrid>) list);
                simpleList.setAdapter(myAdapter);
                TrackingActivity.setListViewHeightBasedOnChildren(simpleList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
       ref.addListenerForSingleValueEvent(valueEventListener);
       ref.removeEventListener(valueEventListener);
       session.setuname(getIntent().getStringExtra("username"));
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        nvDrawer.setItemIconTintList(null);

        // Setup drawer view
        setupDrawerContent(nvDrawer);
        ActionBarDrawerToggle mDrawerToggle;
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close)
            {

                public void onDrawerClosed(View view)
                {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = false;
                }

                public void onDrawerOpened(View drawerView)
                {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = true;
                }
            };
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawer.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Dashboard.this,cart.class);
                i.putExtra("uid",session.getusename());
                i.putExtra("username",session.getuname());
                startActivity(i);
            }
        });
        profi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profiledata=new Intent(Dashboard.this,userProfile.class);
                profiledata.putExtra("uid",session.getusename());
                profiledata.putExtra("pwd",getIntent().getStringExtra("pwd"));
                startActivity(profiledata);
            }
        });



    }
    public void getusername(String email)
    {
        String[] res = {""};
        if (FirebaseDatabase.getInstance() != null)
        {
            FirebaseDatabase.getInstance().goOffline();
        }
        FirebaseDatabase.getInstance().goOnline();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users/"+email.replace(".",","));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                res[0] = dataSnapshot.child("name").getValue().toString();

                Log.d("uname",res[0]);
                NavigationView navigationView = findViewById(R.id.nvView);
                View hView =  navigationView.getHeaderView(0);
                TextView nav_user = (TextView)hView.findViewById(R.id.username);
                nav_user.setText(res[0]);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
        ref.removeEventListener(valueEventListener);


    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                Intent i= new Intent(Dashboard.this,yourOrders.class);
                i.putExtra("uid",session.getusename());
                startActivity(i);
                break;
            case R.id.nav_second_fragment:
                Intent i1= new Intent(Dashboard.this,userProfile.class);
                i1.putExtra("uid",session.getusename());
                startActivity(i1);
                break;
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Dashboard.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                startActivity(intent);
                finish();


                break;


            default:

        }


        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

        // [END basic_query_value_listener]

}