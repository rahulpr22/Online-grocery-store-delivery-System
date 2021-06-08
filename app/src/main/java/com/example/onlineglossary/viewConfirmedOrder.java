package com.example.onlineglossary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class viewConfirmedOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_confirmed_order);
    }
    public void OrderPlaced(View view) {
        //startActivity(new Intent(viewConfirmedOrder.this,TrackingActivity.class));
        String orderStatus="0";
        Intent intent=new Intent(viewConfirmedOrder.this,TrackingActivity.class);
        intent.putExtra("orderStatus",orderStatus);
        startActivity(intent);
    }

    public void OrderConfirmed(View view) {
        String orderStatus="1";
        Intent intent=new Intent(viewConfirmedOrder.this,TrackingActivity.class);
        intent.putExtra("orderStatus",orderStatus);
        startActivity(intent);
    }

    public void OrderProcessed(View view) {
        String orderStatus="2";
        Intent intent=new Intent(viewConfirmedOrder.this,TrackingActivity.class);
        intent.putExtra("orderStatus",orderStatus);
        startActivity(intent);
    }

    public void OrderPickup(View view) {
        String orderStatus="3";
        Intent intent=new Intent(viewConfirmedOrder.this,TrackingActivity.class);
        intent.putExtra("orderStatus",orderStatus);
        startActivity(intent);
    }
}