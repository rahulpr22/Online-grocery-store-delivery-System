package com.example.onlineglossary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class Registration extends AppCompatActivity {

    TextView signup,createact,reghed;
    EditText name,phone,email,pwd;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        signup = findViewById(R.id.signuprbtn);
        name = findViewById(R.id.uname);
        createact = findViewById(R.id.createacnt);
        reghed = findViewById(R.id.reghed);
        phone = findViewById(R.id.phone);
        email=findViewById(R.id.email);
        pwd=findViewById(R.id.pwd);
        if(getIntent().getBooleanExtra("from admin",false))
        {
            reghed.setText("Add Account");
            createact.setVisibility(View.GONE);
            signup.setText("Add");
        }
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth =FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser(){

        //getting email and password from edit texts
        String uname= name.getText().toString();
        String uphone= phone.getText().toString();
        String upwd= pwd.getText().toString();
        String uemail= email.getText().toString();
        user helper = new user();
        helper.setEmail(uemail);
        helper.setName(uname);
        helper.setPhone(uphone);
        helper.setPwd(upwd);

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(uemail)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(upwd)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        boolean[] check = {false};
        //creating a new user
        mAuth.createUserWithEmailAndPassword(uemail, upwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            reference = FirebaseDatabase.getInstance().getReference();
                            reference.child("users/"+uemail.replace(".",",")).setValue(helper);
                            Toast.makeText(Registration.this,"Registration Successful !!",Toast.LENGTH_LONG).show();
                            Log.d("register","reached");
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }else{
                            //display some message here
                            Toast.makeText(Registration.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Registration.this,MainActivity.class);
        startActivity(i);
        finish();
    }

}
class user{
    String name,email,phone,pwd;

    public user() {
    }

    public user(String name, String email, String phone, String pwd) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}