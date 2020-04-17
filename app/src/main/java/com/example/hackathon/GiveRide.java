package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class GiveRide extends AppCompatActivity {

    Button vehicle_type;
    ImageButton submit;
    TextView set_vehicle_type;
    CheckBox conditions;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String s;

    EditText date,fare,from,to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_ride);

        date=findViewById(R.id.date);
        fare=findViewById(R.id.fare);
        from=findViewById(R.id.from);
        to=findViewById(R.id.to);
        conditions=findViewById(R.id.conditions);
        submit=findViewById(R.id.submit);

        vehicle_type=findViewById(R.id.vehicle_type);
        set_vehicle_type=findViewById(R.id.set_vehicle_type);

       vehicle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(GiveRide.this,vehicle_type);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup1,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                         s=item.getTitle().toString();
                        set_vehicle_type.setText(s);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(conditions.isChecked()){
                   final String d=date.getText().toString();
                   final String f=fare.getText().toString();
                   final String vehicle=set_vehicle_type.getText().toString();

                   firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                   reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                   reference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           HashMap<String ,Object> map=new HashMap<>();
                           map.put("Date",d);
                           map.put("Fare",f);
                           map.put("Vehicle",vehicle);
                           reference.updateChildren(map);

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

               }else {
                   Toast.makeText(getApplicationContext(),"Please accept the terms and Conditions",Toast.LENGTH_SHORT).show();
               }
               Toast.makeText(getApplicationContext(),"Submitted",Toast.LENGTH_SHORT).show();
           }
       });

    }
}
