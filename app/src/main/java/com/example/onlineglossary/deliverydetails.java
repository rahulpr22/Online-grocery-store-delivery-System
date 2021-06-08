package com.example.onlineglossary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class deliverydetails extends AppCompatActivity {
    Button proceed;
    TextView imp2;
    EditText name,email,phone,place,street,zip,state,city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverydetails);
        proceed = findViewById(R.id.proceed);
        name=findViewById(R.id.payeeName);
        email=findViewById(R.id.payeeemail);
        phone=findViewById(R.id.payeePhone);
        place=findViewById(R.id.place);
        street=findViewById(R.id.Locality);
        zip=findViewById(R.id.ZipCode);
        state=findViewById(R.id.State);
        city=findViewById(R.id.City);
        imp2=findViewById(R.id.imp2);
        imp2.setVisibility(View.GONE);
        String pid= getIntent().getStringExtra("pid");

        String t1= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        String t2= getIntent().getStringExtra("bill");
        String[] t3=getIntent().getStringArrayExtra("test");
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()
                || email.getText().toString().isEmpty()
                || phone.getText().toString().isEmpty()
                || place.getText().toString().isEmpty()
                || zip.getText().toString().isEmpty()
                || city.getText().toString().isEmpty()
                || state.getText().toString().isEmpty())
                {
                    Toast.makeText(deliverydetails.this,"Please fill all the necessary details !!",Toast.LENGTH_LONG).show();
                    SpannableStringBuilder ssb = new SpannableStringBuilder("Please fill all the   marked details");
                    ssb.setSpan(new ImageSpan(getApplicationContext(), R.drawable.info), 20, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    imp2.setText(ssb, TextView.BufferType.SPANNABLE);
                    imp2.setVisibility(View.VISIBLE);
                }
                else {

                    String temp= street.getText().toString();
                    if(street.getText().toString().isEmpty())
                        temp="NA";

                    Intent i = new Intent(deliverydetails.this, paybill.class);
                    i.putExtra("uid", t1);
                    i.putExtra("bill", t2);
                    i.putExtra("test", t3);
                    i.putExtra("name",name.getText().toString());
                    i.putExtra("phone",phone.getText().toString());
                    i.putExtra("email",email.getText().toString());
                    i.putExtra("place",place.getText().toString());
                    i.putExtra("zip",zip.getText().toString());
                    i.putExtra("city",city.getText().toString());
                    i.putExtra("state",state.getText().toString());
                    i.putExtra("street",temp);
                    i.putExtra("pid",pid);

                    startActivity(i);
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