package com.example.onlineglossary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class productScreen extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TextView qty;
    ImageButton plus,min;
    private int csize=0;
    private String uid;
    ImageButton cartbtn,home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_screen);
        Intent i= getIntent();
        String titles = i.getStringExtra("title");
        String Prices = i.getStringExtra("price");
        String descs = i.getStringExtra("desc");
        String logos= i.getStringExtra("logo");
        int pid = i.getIntExtra("pid",0);
        TextView title = findViewById(R.id.screenTitle);
        TextView price = findViewById(R.id.screenPrice);
        TextView desc = findViewById(R.id.screenDesc);
        ImageView logo = findViewById(R.id.screenImage);
        qty = findViewById(R.id.num);
        plus = findViewById(R.id.plus);
        min=findViewById(R.id.minus);
        Button cart =findViewById(R.id.addtocart);
        cartbtn= findViewById(R.id.cartnav);
        home=findViewById(R.id.house);

        title.setText(titles);
        price.setText(Prices);
        desc.setText(descs);
        Picasso.get().load(logos).resize(900,900).into(logo);

        String uid= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        this.uid=uid;
        int number=1;
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty.setText(String.valueOf(Integer.parseInt(qty.getText().toString())+1));
            }
        });

        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(qty.getText().toString())!=1)
                    qty.setText(String.valueOf(Integer.parseInt(qty.getText().toString())-1));
            }
        });


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String usid=uid.replace(".",",");
                /*mDatabase.child("cart/"+usid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get total available quest
                        int size = (int) dataSnapshot.getChildrenCount();
                        csize =size;
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
                String[] temp = Prices.split("₹");
                int p = Integer.parseInt(temp[1]);
                String cost = "₹"+String.valueOf(p*Integer.parseInt(qty.getText().toString()));
                cartHelper cartdetails= new cartHelper(uid,pid,titles,cost,qty.getText().toString(),logos);
                mDatabase.child("cart").child(usid).child(String.valueOf(pid)).setValue(cartdetails);
                Toast.makeText(productScreen.this,"Item added to cart !!",Toast.LENGTH_LONG).show();
            }
        });
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(productScreen.this, com.example.onlineglossary.cart.class);
                i.putExtra("uid",uid);
                i.putExtra("pid",pid);
                startActivity(i);
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(productScreen.this, Dashboard.class);
                startActivity(i);
            }
        });
        
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}