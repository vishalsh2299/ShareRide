package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hackathon.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity {

    ImageButton giveride,takeride,rent,goods,options;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    TextView username;
    CircleImageView profile_image;

    StorageReference storageReference;

    private static final int IMAGE_REQUEST=1;
    private StorageTask uploadTask;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        giveride=findViewById(R.id.giveride);
        takeride=findViewById(R.id.takeride);
        rent=findViewById(R.id.rent);
        goods=findViewById(R.id.goods);

        options=findViewById(R.id.hamburger);

        storageReference= FirebaseStorage.getInstance().getReference("uploads");

        username=findViewById(R.id.username);
        profile_image=findViewById(R.id.circleImageView);

        giveride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,GiveRide.class));
            }
        });


        takeride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,TakeRide.class));
            }
        });


        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getApplicationContext(),GiveRent.class));
            }
        });
        goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,LuggageActivity.class));
            }
        });


        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.drawable.profilepic);
                }else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(HomePage.this,options);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.logout){
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(),StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            return true;
                        }else if(item.getItemId()==R.id.contact){
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:8423230932")));
                        }else if(item.getItemId()==R.id.aboutus){
                            startActivity(new Intent(getApplicationContext(),AboutUs.class));
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });


    }
    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_REQUEST && resultCode== RESULT_OK && data!=null && data.getData() != null){
            imageUri = data.getData();

            if(uploadTask!=null && uploadTask.isInProgress()){
                Toast.makeText(getApplicationContext(), "Upload in progress",Toast.LENGTH_SHORT).show();//to show uploading toast
            }else {
                uploadImage();
            }
        }
    }

    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(HomePage.this);//Android Progress Dialog is almost same as ProgressBar with the exception that this is displayed as a dialog box.
        pd.setMessage("Uploading");//it shows the uploading message
        pd.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            //TODO--check and learn firebase functions
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();//stop the progress box
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtension(Uri uri){
        //content resolver provides access to different applications, it provides the uri to diff applications and for querying content providers are used
        //MIME-> Multi-purpose Internet Mail Extensions->it is the original internet email protocol that lets people use the protocol to exchange diff kinds of data files on internet

        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();//maps mime types to file extension and vice versa getSinleton->singleton instance of the MIME-map type

        //singleton pattern is a software pattern that ensures a class has only one instance at a time. Anytime multiple classes or clients request for that class they get
        //the same instance that is global.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));//getExtension..->Return the registered extension for the given MIME type.
    }

}

