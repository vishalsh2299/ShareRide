package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GiveRent extends AppCompatActivity {

    ImageButton car_available,bike_available;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    ListView check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_rent);

        car_available=findViewById(R.id.car_available);
        bike_available=findViewById(R.id.bike_available);
        check=findViewById(R.id.check);

        car_available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference().child("Users");
                firebaseAuth=FirebaseAuth.getInstance();

                Query query=reference.orderByChild("Vehicle").equalTo("Four Wheeler");

                final ArrayList<String> arrayList=new ArrayList<>();

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
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
                        androidx.appcompat.widget.PopupMenu popupMenu=new androidx.appcompat.widget.PopupMenu(GiveRent.this,check);
                        popupMenu.getMenuInflater().inflate(R.menu.menu_popup2,popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if(item.getItemId()==R.id.call){
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:9784324528")));
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

        bike_available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference().child("Users");
                firebaseAuth=FirebaseAuth.getInstance();

                Query query=reference.orderByChild("Vehicle").equalTo("Two Wheeler");

                final ArrayList<String> arrayList=new ArrayList<>();

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
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
                        androidx.appcompat.widget.PopupMenu popupMenu=new androidx.appcompat.widget.PopupMenu(GiveRent.this,check);
                        popupMenu.getMenuInflater().inflate(R.menu.menu_popup2,popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if(item.getItemId()==R.id.call){
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:9784324528")));
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
