package com.example.onlineglossary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userProfile extends AppCompatActivity {
TextView view,udetails,upwd,bhome,fname,userid,phone;
TextInputLayout textname,textphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        view =findViewById(R.id.viewOrders);
        udetails =findViewById(R.id.updateDetails);
        fname=findViewById(R.id.fullname);
        userid=findViewById(R.id.email1);
        userid.setText(uid);
        phone=findViewById(R.id.phone1);
        upwd =findViewById(R.id.upwd);
        bhome =findViewById(R.id.backtohome);
        textname =findViewById(R.id.updatename);
        textphone =findViewById(R.id.updatephone);
        String pwd=getIntent().getStringExtra("pwd");
        getDetails(uid);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(userProfile.this,yourOrders.class);
                i.putExtra("uid",uid);
                startActivity(i);
                finish();
            }
        });
        udetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(textname.getEditText().getText().toString().isEmpty() && textphone.getEditText().getText().toString().isEmpty()))
                {
                    updateDetails(uid,textname.getEditText().getText().toString(),textphone.getEditText().getText().toString());
                    Toast.makeText(userProfile.this,"Data updated Successfully !!",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(userProfile.this,"Invalid data !!",Toast.LENGTH_LONG).show();

                }
            }
        });
        upwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(userProfile.this,updatepwd.class);
                i.putExtra("uid",uid);
                i.putExtra("pwd",pwd);
                startActivity(i);
            }
        });
        bhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfile.super.onBackPressed();
            }
        });
    }
    public void getDetails(String email){
        if (FirebaseDatabase.getInstance() != null)
        {
            FirebaseDatabase.getInstance().goOffline();
        }
        FirebaseDatabase.getInstance().goOnline();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users/"+email.replace(".",","));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                textname.getEditText().setText( dataSnapshot.child("name").getValue().toString());
                phone.setText(dataSnapshot.child("phone").getValue().toString());
                fname.setText(dataSnapshot.child("name").getValue().toString());
                textphone.getEditText().setText( dataSnapshot.child("phone").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
        ref.removeEventListener(valueEventListener);
    }
    public void updateDetails(String email,String name,String phone)
    {
        if (FirebaseDatabase.getInstance() != null)
        {
            FirebaseDatabase.getInstance().goOffline();
        }
        FirebaseDatabase.getInstance().goOnline();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users/"+email.replace(".",","));
        ref.child("name").setValue(name);
        ref.child("phone").setValue(phone);


    }
}