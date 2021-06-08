package com.example.onlineglossary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class updatepwd extends AppCompatActivity {
TextInputLayout cpwd1,npwd;
TextView upwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepwd);
        String uid=FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        String cpwd=getIntent().getStringExtra("pwd");
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("users").child(uid.replace(".",","));
        ValueEventListener valueEventListener= new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String cpwd=snapshot.child("pwd").getValue().toString();
                Log.d("upwd", String.valueOf(cpwd.equals("prb@123")));
                helpermethod(uid,cpwd);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };

        ref.addListenerForSingleValueEvent(valueEventListener);
        ref.removeEventListener(valueEventListener);


    }
    public void helpermethod(String uid,String currpwd)
    {

        cpwd1=findViewById(R.id.cpwd1);
        npwd=findViewById(R.id.npwd);
        upwd=findViewById(R.id.updatePwd);

        upwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check",currpwd+" = "+cpwd1.getEditText().getText().toString());
                if(!cpwd1.getEditText().getText().toString().isEmpty() && !npwd.getEditText().getText().toString().isEmpty() && currpwd.equals(cpwd1.getEditText().getText().toString())) {
                    Log.d("reachedupwd", "reached");
                    updatepwd(uid, currpwd);
                }
                else
                {
                    Toast.makeText(updatepwd.this,"Incorrect Current Password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void updatepwd(String uid,String cpwd){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("user", String.valueOf(user));

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(uid, cpwd);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(npwd.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Success", "Password updated");
                                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users/"+uid.replace(".",","));
                                        reference.child("pwd").setValue(npwd.getEditText().getText().toString());
                                        Toast.makeText(updatepwd.this,"Password updated Succesfully",Toast.LENGTH_LONG).show();
                                        onBackPressed();
                                    } else {
                                        Log.d("error", "Error password not updated");
                                        Toast.makeText(updatepwd.this,"Error password not updated",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Log.d("error", "Error auth failed");
                            Toast.makeText(updatepwd.this,"Authentication failed may be due to incorrect current password",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}