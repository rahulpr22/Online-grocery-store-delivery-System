package com.example.onlineglossary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class AdminAddProduct extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private EditText name,desc,price,catid;
    private Button uploadbutton;
    private Uri imageuri;
    private StorageReference mStorageref;
    private DatabaseReference mDatabaseref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);
        imageView = findViewById(R.id.image);
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        price = findViewById(R.id.price);
        catid = findViewById(R.id.catid);
        uploadbutton = findViewById(R.id.btn_upload);
        mStorageref = FirebaseStorage.getInstance().getReference("products");
        mDatabaseref = FirebaseDatabase.getInstance().getReference("products");



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfilechooser();


            }
        });

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadfile();
            }
        });
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }

    private void uploadfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                long count=snapshot.getChildrenCount();
                if (imageuri !=null){
                    StorageReference  filereference  = mStorageref.child(System.currentTimeMillis()+
                            "."+getFileExtension(imageuri));

                    filereference.putFile(imageuri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getApplicationContext(), "Upload Successfully", Toast.LENGTH_SHORT).show();
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;
                                            productgrid upload=new productgrid("₹"+price.getText().toString(),name.getText().toString(),
                                                    desc.getText().toString(),downloadUrl.toString(),(int)(count+1),
                                                    Integer.parseInt(catid.getText().toString()));
                                            progressDialog.show();
                                            String  uploadId = mDatabaseref.push().getKey();
                                            mDatabaseref.child(String.valueOf(count+1)).setValue(upload);
                                            progressDialog.setCanceledOnTouchOutside(false);
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.setMessage("Uploaded  " +(int)progress+"%");



                                }
                            });
                }else
                    Toast.makeText(getApplicationContext(), "Please Select a Image", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        mDatabaseref.addListenerForSingleValueEvent(valueEventListener);





    }

    private void openfilechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null &&
                data.getData()!=null){
            imageuri = data.getData();
            Picasso.get().load(imageuri).into(imageView);

        }
    }
}