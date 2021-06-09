package com.example.onlineglossary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminViewTracking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_tracking);
    }
    public void OrderPlaced(View view) {
        //startActivity(new Intent(viewConfirmedOrder.this,TrackingActivity.class));
        String orderStatus="0";
        String oid=getIntent().getStringExtra("oid");
        String uid=getIntent().getStringExtra("uid");
        String ostatus=getIntent().getStringExtra("status");
        if(!ostatus.equalsIgnoreCase("Pending"))
        {
            Toast.makeText(getApplicationContext(),"Order Already Placed !!",Toast.LENGTH_LONG).show();
        }
        else {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("delivery/admindata");
            ref.child(oid).child("status").setValue("Placed");
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("delivery/"+uid.replace(".",","));
            reference.child(oid).child("status").setValue("Placed");

        }
    }

    public void OrderConfirmed(View view) {
        String orderStatus="1";
        String oid=getIntent().getStringExtra("oid");
        String uid=getIntent().getStringExtra("uid");
        String ostatus=getIntent().getStringExtra("status");
        if(ostatus.equalsIgnoreCase("Processing") || ostatus.equalsIgnoreCase("Delivered") )
        {
            Toast.makeText(getApplicationContext(),"Order Already Placed !!",Toast.LENGTH_LONG).show();
        }
        else {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("delivery/admindata");
            ref.child(oid).child("status").setValue("Confirmed");
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("delivery/"+uid.replace(".",","));
            reference.child(oid).child("status").setValue("Confirmed");
        }
    }

    public void OrderProcessed(View view) {
        String orderStatus="2";
        String oid=getIntent().getStringExtra("oid");
        String uid=getIntent().getStringExtra("uid");
        String ostatus=getIntent().getStringExtra("status");
        if( ostatus.equalsIgnoreCase("Delivered") )
        {
            Toast.makeText(getApplicationContext(),"Order Already delivered !!",Toast.LENGTH_LONG).show();
        }
        else {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("delivery/admindata");
            ref.child(oid).child("status").setValue("Processing");
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("delivery/"+uid.replace(".",","));
            reference.child(oid).child("status").setValue("Processing");
        }
    }

    public void OrderPickup(View view) {
        String orderStatus="3";
        String oid=getIntent().getStringExtra("oid");
        String uid=getIntent().getStringExtra("uid");
        String ostatus=getIntent().getStringExtra("status");
        if( ostatus.equalsIgnoreCase("Processing") )
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("delivery/admindata");
            ref.child(oid).child("status").setValue("Delivered");
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("delivery/"+uid.replace(".",","));
            reference.child(oid).child("status").setValue("Delivered");
        }
        else {
            Toast.makeText(getApplicationContext(),"Invalid Operation !!",Toast.LENGTH_LONG).show();
        }
    }
}