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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdminViewListofOrders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_listof_orders);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("delivery/admindata");
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<AdmindeliveryMonitor> list= new ArrayList<>();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    AdmindeliveryMonitor p = ds.getValue(AdmindeliveryMonitor.class);

                    if(!p.getStatus().equalsIgnoreCase("delivered")){
                        AdmindeliveryMonitor a=new AdmindeliveryMonitor();
                        a.setStatus(p.getStatus());
                        a.setOid(p.getOid());
                        a.setUid(p.getUid());
                        list.add(a);
                    }


                }
                ListView lv=findViewById(R.id.adminlistvieworders);
                AdminorderAdapter myAdapter= new AdminorderAdapter(getApplicationContext(),list);
                lv.setAdapter(myAdapter);
                TrackingActivity.setListViewHeightBasedOnChildren(lv);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        reference.addListenerForSingleValueEvent(valueEventListener);

    }
}
class AdminorderAdapter extends ArrayAdapter<AdmindeliveryMonitor>{
    private Context mcontext;
    public AdminorderAdapter(@NonNull Context context, ArrayList<AdmindeliveryMonitor> dataModalArrayList) {
        super(context,0,dataModalArrayList);
        this.mcontext=context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.adminlistorderitem, parent, false);
        }
        AdmindeliveryMonitor c= (AdmindeliveryMonitor) getItem(position);
        TextView uid = listitemView.findViewById(R.id.adminuid);
        TextView oid = listitemView.findViewById(R.id.admindoid);
        uid.setText(c.getUid().replace(",","."));
        oid.setText(c.getOid());
        TextView monitor= listitemView.findViewById(R.id.adminmonitordeliery);
        monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),AdminViewTracking.class);
                i.putExtra("oid",c.getOid());
                i.putExtra("uid",c.getUid());
                i.putExtra("status",c.getStatus());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(i);
            }
        });
        return listitemView;
    }
}
class AdmindeliveryMonitor{
    String uid,oid,status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AdmindeliveryMonitor() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public AdmindeliveryMonitor(String uid, String oid,String status) {
        this.uid = uid;
        this.oid = oid;
        this.status=status;
    }
}