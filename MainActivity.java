package com.example.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    ImageView send;
    ListView listView;
    message msg;
    ArrayList<String> msglist;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        send=(ImageView)findViewById(R.id.send);
        listView = (ListView)findViewById(R.id.listView);
        msg = new message();
        msglist = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,R.layout.list_layout,R.id.textView,msglist);

        //retrieve data from firebase and display in application (in listview)
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("chat");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msglist.clear();
                for(DataSnapshot ds:snapshot.getChildren()) {
                    msg = ds.getValue(message.class);
                    msglist.add(msg.getName()+"\n"+msg.getMessage());
                }listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //inserting data into firebase database (real time database)
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://prithviraj-30d26.firebaseio.com/chat");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message message = new message("prithviraj",editText.getText().toString());
                ref.push().setValue(message);
                editText.setText("");
            }
        });
    }
}
