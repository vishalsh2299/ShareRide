package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class TakeRide extends AppCompatActivity {
    EditText date,from,to;
    ImageButton checkAvail;
    Button vehicle_type;
    TextView set_vehicle_type;
    ListView check;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_ride);

        vehicle_type=findViewById(R.id.vehicle_type);
        set_vehicle_type=findViewById(R.id.set_vehicle_type);
        check=findViewById(R.id.check);
        checkAvail=findViewById(R.id.checkAvailability);


        date=findViewById(R.id.date);


        vehicle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(TakeRide.this,vehicle_type);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup1,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String s=item.getTitle().toString();
                        set_vehicle_type.setText(s);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        checkAvail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference().child("Users");
                firebaseAuth = FirebaseAuth.getInstance();

                Query query = reference.orderByChild("Date").equalTo(date.getText().toString());
                final ArrayList<String> arrayList = new ArrayList<>();

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                arrayList.add(firebaseUser.getEmail());
                            }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.available_list_test, R.id.tv1, arrayList);
                        check.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                check.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        androidx.appcompat.widget.PopupMenu popupMenu=new androidx.appcompat.widget.PopupMenu(TakeRide.this,check);
                        popupMenu.getMenuInflater().inflate(R.menu.menu_popup2,popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if(item.getItemId()==R.id.call){
                                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("tel:9784324528")));
                                    return true;
                                }else if(item.getItemId()==R.id.chat){
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    String toNumber="9784324528";
                                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber ));
                                    startActivity(intent);
                                }
                                return true;
                            }
                        });
                        popupMenu.show();
                    }

                });
            }
        });
        }
}
