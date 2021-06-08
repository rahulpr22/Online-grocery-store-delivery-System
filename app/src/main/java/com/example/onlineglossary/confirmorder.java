package com.example.onlineglossary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class confirmorder extends AppCompatActivity {
ImageView confirm;
Button bo,bh;
TextView tv1;
private final String url="https://firebasestorage.googleapis.com/v0/b/online-glossary.appspot.com/o/orderconfirm.jpg?alt=media&token=91c4a277-6b54-4e44-9be3-8176066c62f8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder);
        //confirm=findViewById(R.id.oplaced);
        //Picasso.get().load(R.drawable.orderconfirm).resize(1000,1000).into(confirm);
        String oid= getIntent().getStringExtra("oid");
        String uid= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        tv1= findViewById(R.id.oid);
        tv1.setText("OrderId : "+oid);
        bo=findViewById(R.id.vorder);
        bh=findViewById(R.id.o2home);
        String t1=getIntent().getStringExtra("tv1");
        String t2=getIntent().getStringExtra("tv2");
        bh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirmorder.super.onBackPressed();
                Intent i= new Intent(confirmorder.this,Dashboard.class);
                i.putExtra("userid",uid);
                startActivity(i);
                finish();

            }
        });
        bo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(confirmorder.this,yourOrders.class);
                i.putExtra("uid",uid);
                startActivity(i);
                finish();
            }
        });
    }
}