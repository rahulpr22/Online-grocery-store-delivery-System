package com.example.onlineglossary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class yourOrders extends AppCompatActivity {

    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        if (FirebaseDatabase.getInstance() != null)
        {
            FirebaseDatabase.getInstance().goOffline();
        }
        FirebaseDatabase.getInstance().goOnline();
        Log.d("yorderpage","reached");
        dbRef= FirebaseDatabase.getInstance().getReference().child("delivery/"+uid.replace(".",","));
        Log.d("yorderpage","reached"+"delivery"+uid.replace(".",","));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<DeliveryInfo> list = new ArrayList<>();
                Log.d("yorderpage", "reached");
                Log.d("snapshot", String.valueOf(snapshot.getChildrenCount()));
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DeliveryInfo d = ds.getValue(DeliveryInfo.class);
                    Log.d("viewOrders", d.getOid() + " , " + d.getName());
                    list.add(d);
                }
                ListView lv = findViewById(R.id.listvieworders);
                deliversOrdersAdapter myAdapter = new deliversOrdersAdapter(getApplicationContext(), list, uid);
                lv.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        dbRef.addListenerForSingleValueEvent(valueEventListener);


    }
    public  void vieworders(String uid,String oid,String t1,String t2){
        Intent i = new Intent(yourOrders.this,TrackingActivity.class);
        i.putExtra("uid", uid);
        i.putExtra("oid", oid);
        i.putExtra("tv1",t1);
        i.putExtra("tv2",t2);
        startActivity(i);
        finish();
    }
    /*@Override
    protected void onStart() {
        super.onStart();
        String uid = getIntent().getStringExtra("uid");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<DeliveryInfo> list = new ArrayList<>();
                Log.d("yorderpage", "reached");
                Log.d("snapshot", String.valueOf(snapshot.getChildrenCount()));
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DeliveryInfo d = ds.getValue(DeliveryInfo.class);
                    Log.d("viewOrders", d.getOid() + " , " + d.getName());
                    list.add(d);
                }
                ListView lv = findViewById(R.id.listvieworders);
                deliversOrdersAdapter myAdapter = new deliversOrdersAdapter(getApplicationContext(), list, uid);
                lv.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        dbRef.addListenerForSingleValueEvent(valueEventListener);
    }*/
}
class deliversOrdersAdapter extends ArrayAdapter<DeliveryInfo> {
    private Context context;
    private String uid;

    public deliversOrdersAdapter(@NonNull Context context, @NonNull List<DeliveryInfo> objects, @NonNull String uid) {
        super(context, 0, objects);
        this.context = context;
        this.uid = uid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.deliveryorderitem, parent, false);
        }
        DeliveryInfo t = (DeliveryInfo) getItem(position);
        TextView tv = listitemView.findViewById(R.id.doid);
        TextView tv1 = listitemView.findViewById(R.id.dostatus);
        TextView track = listitemView.findViewById(R.id.deltrackorder);
        tv.setText(t.getOid());
        tv1.setText(t.getStatus());
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //yourOrders x= new yourOrders();
                //x.vieworders(uid,t.getOid(),t.getPlace()+", "+t.getStreet(),t.getCity()+", "+t.getZip()+", "+t.getState());
                Intent i = new Intent(getContext(), TrackingActivity.class);
                i.putExtra("uid", uid);
                i.putExtra("oid", t.getOid());
                i.putExtra("tv1",t.getPlace()+", "+t.getStreet());
                i.putExtra("tv2",t.getCity()+", "+t.getZip()+", "+t.getState());
                i.putExtra("name",t.getName()+" "+t.getPhone());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        return listitemView;
    }
}