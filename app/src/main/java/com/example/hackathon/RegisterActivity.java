package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText username,email,password,adhaar,rcnumber,vehicleNumber,mobileNumber;
    ImageButton tbn_regiter;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        Toolbar toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);//to set the tool bar in the activity
//        getSupportActionBar().setTitle("Register");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        adhaar=findViewById(R.id.adhar);
        vehicleNumber=findViewById(R.id.vehicle_number);
        rcnumber=findViewById(R.id.rc_number);
        mobileNumber=findViewById(R.id.mobile);

        tbn_regiter=findViewById(R.id.btn_register);

        auth=FirebaseAuth.getInstance();

        tbn_regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username=username.getText().toString();
                String txt_email=email.getText().toString();
                String txt_passsword=password.getText().toString();
                String mobile=mobileNumber.getText().toString();
                String rc=rcnumber.getText().toString();
                String vehicle=vehicleNumber.getText().toString();
                String adhar=adhaar.getText().toString();

                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_passsword)
                || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(rc) || TextUtils.isEmpty(vehicle) || TextUtils.isEmpty(adhar) ) {
                    Toast.makeText(getApplicationContext(), "Fill the Details", Toast.LENGTH_LONG).show();
                }else if(password.length()<6){
                    Toast.makeText(getApplicationContext(),"Password length should be greater than 6", Toast.LENGTH_LONG).show();
                }
                else {
                    register(txt_username,txt_email,txt_passsword,rc);
                }
            }
        });
    }

    private void register(final String username, final String email, String password, final String rcnum)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;//added to remove the possibility of getting null value in getUid so alt+enter on UID and assert
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            String mobile=mobileNumber.getText().toString();
                            String rc=rcnumber.getText().toString();

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("RCNumber", rc);
                            hashMap.put("MobileNumber", mobile);
                            hashMap.put("Email", email);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        //FLAG_ACTIVITY_NEW_TASK --if set then this activity becomes the start of a new task on this history stack
                                        //FLAG_ACTIVITY_CLEAT_TASK--if it is passed to startActivity(intent) then it will clear any exiting acticity or any task and start new activity
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(RegisterActivity.this,"You can't register with this email or password",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
