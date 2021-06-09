package com.example.onlineglossary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class cart extends AppCompatActivity {
    private DatabaseReference dbref;
    private int cartvalue=0;
    TextView cartcost,sorrymsg;
    ImageView emptycart;
    ImageButton btn;
    ListView lv;
    Button checkout;
    JSONArray order =new JSONArray();
    String temp="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btn=findViewById(R.id.backhome);
        cartcost = findViewById(R.id.totalprice);
        checkout =findViewById(R.id.checkout);
        emptycart=findViewById(R.id.emptycart);
        sorrymsg=findViewById(R.id.cartsorrymsg);
        checkout.setVisibility(View.GONE);
        cartcost.setVisibility(View.GONE);
        Intent prime=new Intent(cart.this,deliverydetails.class);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        if (FirebaseDatabase.getInstance() != null)
        {
            FirebaseDatabase.getInstance().goOffline();
        }
        FirebaseDatabase.getInstance().goOnline();
        dbref= FirebaseDatabase.getInstance().getReference("cart/"+uid.replace(".",","));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<cartHelper> list = new ArrayList<>();
                List<String> str=new ArrayList<>();
                int i=0;
                String s="";
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    cartHelper p = ds.getValue(cartHelper.class);
                    i++;
                    cartvalue += Integer.parseInt(p.getPrice().split("₹")[1]);
                    Log.d("cartValue", String.valueOf(cartvalue));
                    list.add(p);
                    s+=p.getPid()+"@";
                    String temp="";
                    temp+=p.getItem()+" "+p.getLogo()+" "+i+" "+p.getQuantity()+" "+p.getPrice();
                    Log.d("Spaces", String.valueOf(temp.split(" ").length));
                    str.add(temp);
                }
                emptycart.setVisibility(View.GONE);
                sorrymsg.setVisibility(View.GONE);
                if(list.size()==0)
                {
                    emptycart.setVisibility(View.VISIBLE);
                    sorrymsg.setVisibility(View.VISIBLE);
                }
                Log.d("cartarray", String.valueOf(list.size())); //To see is not emplty

                String[] test= str.toArray(new String[0]);
                Log.d("cartlength", String.valueOf(test.length)); //To see is not emplty




                lv = findViewById(R.id.listviewcart);
                cartAdapter myAdapter=new cartAdapter(getApplicationContext(), (ArrayList<cartHelper>) list);
                lv.setAdapter((ListAdapter) myAdapter);
                TrackingActivity.setListViewHeightBasedOnChildren(lv);
                checkout.setVisibility(View.VISIBLE);
                cartcost.setVisibility(View.VISIBLE);
                cartcost.setText("Total Price : ₹"+String.valueOf(cartvalue)+".00");
                prime.putExtra("test",test);
                prime.putExtra("size",String.valueOf(list.size()));
                if(!s.isEmpty())
                    prime.putExtra("pid",s.substring(0,s.length()-1));
                else
                    prime.putExtra("pid",s);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        dbref.addListenerForSingleValueEvent(valueEventListener);
        dbref.removeEventListener(valueEventListener);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(cart.this, Dashboard.class);
                i.putExtra("userid",uid);
                startActivity(i);
                finish();
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prime.getStringExtra("size").equals("0"))
                {
                    Toast.makeText(getApplicationContext(), "Your Cart is empty!!", Toast.LENGTH_LONG).show();
                }
                else{
                    prime.putExtra("bill",cartcost.getText().toString().split(":")[1]);
                    prime.putExtra("uid",uid);
                    startActivity(prime);
                    finish();
                }

            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

}