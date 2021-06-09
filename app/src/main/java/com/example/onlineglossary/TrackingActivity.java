package com.example.onlineglossary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackingActivity extends AppCompatActivity {
    View view_order_placed,view_order_confirmed,view_order_processed,view_order_pickup,con_divider,ready_divider,placed_divider;
    ImageView img_orderconfirmed,orderprocessed,orderpickup;
    TextView textorderpickup,text_confirmed,textorderprocessed,tv3,deladdr,tv4,tv8,cancelorder;
    ImageButton b2home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        view_order_placed=findViewById(R.id.view_order_placed);
        view_order_confirmed=findViewById(R.id.view_order_confirmed);
        view_order_processed=findViewById(R.id.view_order_processed);
        view_order_pickup=findViewById(R.id.view_order_pickup);
        placed_divider=findViewById(R.id.placed_divider);
        con_divider=findViewById(R.id.con_divider);
        ready_divider=findViewById(R.id.ready_divider);
        b2home= findViewById(R.id.vcback2home);
        textorderpickup=findViewById(R.id.textorderpickup);
        text_confirmed=findViewById(R.id.text_confirmed);
        textorderprocessed=findViewById(R.id.textorderprocessed);

        img_orderconfirmed=findViewById(R.id.img_orderconfirmed);
        orderprocessed=findViewById(R.id.orderprocessed);
        orderpickup=findViewById(R.id.orderpickup);
        cancelorder=findViewById(R.id.cancelorder);
        Intent intent=getIntent();
        String orderStatus="Processing";//intent.getStringExtra("orderStatus");

        tv3=findViewById(R.id.textView3);
        tv4=findViewById(R.id.oid);
        tv8=findViewById(R.id.textView8);

        String uid= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        String oid=getIntent().getStringExtra("oid");

        deladdr=findViewById(R.id.deladdr);
        Log.d("oid",getIntent().getStringExtra("oid"));
        Log.d("name",getIntent().getStringExtra("name"));

        tv3.setText(getIntent().getStringExtra("tv1"));
        tv4.setText("OrderId : "+oid);
        tv8.setText(getIntent().getStringExtra("name"));
        deladdr.setText(getIntent().getStringExtra("tv2"));
        if (FirebaseDatabase.getInstance() != null)
        {
            FirebaseDatabase.getInstance().goOffline();
        }
        FirebaseDatabase.getInstance().goOnline();
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("orders/"+uid.replace(".",",")).child(oid);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<orderItem> list = new ArrayList<>();
                int i=0;
                String s="";
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    orderItem p = ds.getValue(orderItem.class);
                    list.add(p);
                    String temp="";

                    temp+=p.getQty()+" "+p.getTitle()+" "+p.getPrice();
                    Log.d("testretrieval", temp);
                }


                ListView lv = findViewById(R.id.listvieworderitems);
                orderAdapter myAdapter=new orderAdapter(getApplicationContext(), (ArrayList<orderItem>) list);
                lv.setAdapter((ListAdapter) myAdapter);
                setListViewHeightBasedOnChildren(lv);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        dbref.addListenerForSingleValueEvent(valueEventListener);
        dbref.removeEventListener(valueEventListener);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("delivery/"+uid.replace(".",","));
        ValueEventListener status=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String orderstatus= snapshot.child(oid).child("status").getValue().toString();
                getOrderStatus(orderstatus,oid,uid);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        reference.addListenerForSingleValueEvent(status);
        b2home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackingActivity.super.onBackPressed();
            }
        });
        cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
                ref.child("delivery/"+uid.replace(".",",")).child(oid).removeValue();
                ref.child("orders/"+uid.replace(".",",")).child(oid).removeValue();
                Intent i= new Intent(getApplicationContext(),yourOrders.class);
                startActivity(i);
                finish();
            }
        });
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // Get the Adapter corresponding to ListView
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount() returns the number of data items
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // Calculate the width and height of the child View
            totalHeight += listItem.getMeasuredHeight(); // Count the total height of all children
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight() gets the height occupied by the divider between sub-items
        // params.height finally gets the height required for the complete display of the entire ListView
        listView.setLayoutParams(params);
    }
    private void getOrderStatus(String orderStatus,String oid,String uid) {
        HashMap<String,String> map=new HashMap<>();
        map.put("Placed","0");
        map.put("Pending","0");
        map.put("Confirmed","1");
        map.put("Processing","2");
        map.put("Delivered","3");
        orderStatus=map.get(orderStatus);
                if (orderStatus.equals("0")){
                    float alfa= (float) 0.5;
                    Log.d("reachedalfa","reached");
                    setStatus(alfa);

                }else if (orderStatus.equals("1")){
                    float alfa= (float) 1;
                    setStatus1(alfa);
                    Log.d("reachedalfa","reached");



                }else if (orderStatus.equals("2")){
                    float alfa= (float) 1;
                    setStatus2(alfa);
                    Log.d("reachedalfa","reached");


                }else if (orderStatus.equals("3")){
                    float alfa= (float) 1;
                    setStatus3(alfa);
                    Log.d("reachedalfa","reached");

                }



    }


    private void setStatus(float alfa) {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        orderprocessed.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        placed_divider.setAlpha(alfa);
        img_orderconfirmed.setAlpha(alfa);
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        orderpickup.setAlpha(alfa);

        textorderpickup.setAlpha(myf);




    }

    private void setStatus1(float alfa) {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(myf);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        img_orderconfirmed.setAlpha(alfa);

        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(myf);
        view_order_pickup.setAlpha(myf);
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        orderpickup.setAlpha(myf);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderpickup.setAlpha(myf);
    }

    private void setStatus2(float alfa) {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        img_orderconfirmed.setAlpha(alfa);

        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        textorderpickup.setAlpha(myf);
        orderpickup.setAlpha(myf);

    }

    private void setStatus3(float alfa) {
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderprocessed.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        img_orderconfirmed.setAlpha(alfa);
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        textorderpickup.setAlpha(alfa);
        orderpickup.setAlpha(alfa);
    }
}