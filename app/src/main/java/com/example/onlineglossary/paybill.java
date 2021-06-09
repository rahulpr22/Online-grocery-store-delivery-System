package com.example.onlineglossary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class paybill extends AppCompatActivity {
   TextView imp,pay;
   EditText cardno,expmm,expyy,cvv;
   String uid;
   Button paybtn;
JSONArray array;
    private DatabaseReference mDatabase;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paybill);
        imp=findViewById(R.id.imp);
        pay=findViewById(R.id.paydetail);
        paybtn=findViewById(R.id.paybtn);
        cardno=findViewById(R.id.CardNo);
        cvv=findViewById(R.id.CVV);
        expmm=findViewById(R.id.expirymonth);
        expyy=findViewById(R.id.expiryyear);

        uid= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder("Please fill all the   marked details");
        ssb.setSpan(new ImageSpan(getApplicationContext(), R.drawable.info), 20, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        imp.setText(ssb, TextView.BufferType.SPANNABLE);
        SpannableStringBuilder ssb1 = new SpannableStringBuilder("Payment details ");
        ssb1.setSpan(new ImageSpan(getApplicationContext(), R.drawable.info), 15, 16, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        pay.setText(ssb1, TextView.BufferType.SPANNABLE);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String usid= uid.replace(".",",");

        String oid=usid.split("@")[0]+dtf.format(now).toString().replace("/","").replace(":","").replace(" ","");
        fillListView(getIntent().getStringArrayExtra("test"),usid,oid);

        paybtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(cardno.getText().toString().isEmpty() || cvv.getText().toString().isEmpty()
                   || expmm.getText().toString().isEmpty() || expyy.getText().toString().isEmpty())
                {
                    Toast.makeText(paybill.this,"Please fill all the card details !!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    final int[] x = {0};
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    String name=getIntent().getStringExtra("name");
                    String email=getIntent().getStringExtra("email");
                    String phone=getIntent().getStringExtra("phone");
                    String place=getIntent().getStringExtra("place");
                    String street=getIntent().getStringExtra("street");
                    String zip=getIntent().getStringExtra("zip");
                    String city=getIntent().getStringExtra("city");
                    String state=getIntent().getStringExtra("state");


                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();

                    String[] pid = getIntent().getStringExtra("pid").split("@");


                    DeliveryInfo d=new DeliveryInfo();
                    d.setCity(city);
                    d.setEmail(email);
                    d.setName(name);
                    d.setPhone(phone);
                    d.setPlace(place);
                    d.setState(state);
                    d.setStreet(street);
                    d.setZip(zip);
                    d.setOid(oid);
                    d.setStatus("Pending");
                    AdmindeliveryMonitor a= new AdmindeliveryMonitor();
                    a.setUid(uid.replace(".",","));
                    a.setOid(oid);
                    a.setStatus("Pending");
                    mDatabase.child("delivery/"+usid).child(oid).setValue(d);
                    mDatabase.child("delivery/admindata/"+oid).setValue(a);
                    Log.d("dbcheck",usid+" , "+oid);
                    String tid=dtf.format(now).toString().replace("/","").replace(":","").replace(" ","");
                    transaction t= new transaction();
                    t.setCardNo(cardno.getText().toString());
                    t.setDate(dtf.format(now).toString());
                    t.setUid(uid);
                    t.setPrice(getIntent().getStringExtra("bill"));
                    t.setOid(oid);
                    t.setTid("trIDNO"+tid);



                    mDatabase.child("transaction/"+usid).child("trIDNO"+tid).setValue(t);
                    Log.d("dbcheck",usid+" , "+tid);

                    for(int i=0;i<pid.length;i++)
                    {
                        mDatabase.child("cart/"+usid).child(pid[i]).removeValue();
                        Log.d("pid",pid[i]);
                    }
                    Intent i1 = new Intent(paybill.this,confirmorder.class);
                    i1.putExtra("oid",oid);
                    i1.putExtra("tv2",city+", "+zip+", "+state);
                    i1.putExtra("tv1",place+", "+street);
                    i1.putExtra("uid",uid);

                    startActivity(i1);
                    finish();
                }
            }
        });

    }
    public void fillListView(String[] test,String usid,String oid){
        Log.d("testjhbn","reached");
        ArrayList<orderItem> list = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        for (int i = 0; i < test.length; i++) {
                String[] temp = test[i].split(" ");
                orderItem item = new orderItem();
                item.setLogo(temp[1]);
                item.setPrice(temp[4]);
                item.setTitle(temp[0]);
                item.setQty(temp[3]);
                item.setPid(temp[2]);
                Log.d("0",temp[0]);
                Log.d("1",temp[1]);
                Log.d("2",temp[3]);
                Log.d("3",temp[3]);
                Log.d("4",temp[4]);

                list.add(item);

                mDatabase.child("orders/" + usid).child(oid).child("orderitem" + temp[2]).setValue(item);
            }


        Log.d("testlist", String.valueOf(list.size()));
        orderAdapter myAdapter=new orderAdapter(getApplicationContext(), list);
        ListView lv= findViewById(R.id.orderlist);
        lv.setAdapter((ListAdapter) myAdapter);
        TrackingActivity.setListViewHeightBasedOnChildren(lv);
    }

    public void onBackPressed(){
        paybill.super.onBackPressed();
    }
}
class transaction{
    String uid,tid,oid,price,cardNo,date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public transaction() {
    }

    public transaction(String uid, String tid, String oid, String price, String cardNo) {
        this.uid = uid;
        this.tid = tid;
        this.oid = oid;
        this.price = price;
        this.cardNo = cardNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
class orderItem{
    String pid,qty,price,logo,title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
class orderAdapter extends ArrayAdapter<orderItem>{
    private Context mcontext;


    public orderAdapter(@NonNull Context context, ArrayList<orderItem> dataModalArrayList) {
        super(context,0,dataModalArrayList);
        this.mcontext=context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.ordersummary, parent, false);
        }
        orderItem c= (orderItem) getItem(position);
        TextView title = listitemView.findViewById(R.id.itemname);
        TextView price = listitemView.findViewById(R.id.itemprice);
        TextView qty = listitemView.findViewById(R.id.itemqty);
        ImageView logo= listitemView.findViewById(R.id.oimg);
        title.setText(c.getTitle());
        price.setText(c.getPrice());
        qty.setText("Qty : "+c.getQty());
        Picasso.get().load(c.getLogo()).into(logo);
        Log.d("testsumm","reached");

        return listitemView;
    }
}

