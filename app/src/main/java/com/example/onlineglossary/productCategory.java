package com.example.onlineglossary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class productCategory extends AppCompatActivity {
ImageView noitems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        noitems=findViewById(R.id.noitems);
        if (FirebaseDatabase.getInstance() != null) {
            FirebaseDatabase.getInstance().goOffline();
        }
        FirebaseDatabase.getInstance().goOnline();
        String catid=getIntent().getStringExtra("catid");
        TextView catitem=findViewById(R.id.catitemhed);
        HashMap<String,String >map=new HashMap<>();
        map.put("1","Fruits & Vegetables");
        map.put("2","Meat");
        map.put("3","Packed food & snacks");
        map.put("4","Household");
        map.put("5","Dairy");
        catitem.setText(map.get(catid));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("products");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<productgrid> list = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    productgrid p = ds.getValue(productgrid.class);
                    Log.d("test", "title " + p.getTitle() + " desc " + p.getDescription());
                    if (p.getCatid() == Integer.parseInt(catid)) {
                        list.add(p);
                    }

                }
                noitems.setVisibility(View.GONE);
                if(list.size()==0)
                {
                    noitems.setVisibility(View.VISIBLE);
                }
                Log.d("TAG", String.valueOf(list.size())); //To see is not emplty
                ListView simpleList = findViewById(R.id.catlistviewcart);
                productAdapter myAdapter=new productAdapter(getApplicationContext(), (ArrayList<productgrid>) list);
                simpleList.setAdapter(myAdapter);
                TrackingActivity.setListViewHeightBasedOnChildren(simpleList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
        ref.removeEventListener(valueEventListener);
    }
}