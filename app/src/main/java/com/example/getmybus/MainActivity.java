package com.example.getmybus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getmybus.Recyclerview_elements.RecyclerviewAdapter;
import com.example.getmybus.Recyclerview_elements.TimelineData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main activity";
    LinearLayout l;
    TextView t;
    TextView title;
    List<TimelineData> list = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout);
        Button buy = findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TestActivity.class));
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        //TODO recycler view calculation from the database
        adapter = new RecyclerviewAdapter(list);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ride").document(getIntent().getStringExtra("id")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String routeid = documentSnapshot.getString("route_id");
                        long js = documentSnapshot.getLong("percent");
                        List<String> stops = (List<String>) documentSnapshot.get("stopnames");
                        if (stops == null)
                            Toast.makeText(MainActivity.this,"stop is null",Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this,"length"+stops.size(),Toast.LENGTH_SHORT).show();
                        List<String> times = new ArrayList<>();
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                        for (int i=1; i<=stops.size();i++){
                            calendar.add(Calendar.SECOND,270*i);
                            times.add(String.valueOf(simpleDateFormat.format(calendar.getTime()))+" Pm");
                        }
//                                Log.d(TAG, "onComplete: "+documentSnapshot.getLong("id"));
                        int num = Integer.parseInt(String.valueOf(js));
                        num = num+100;
                        list.clear();
                        int place=0;
                        for(place=0;place<num/100;place++){
                            if (place==0){
                                if (1 == num/100){
                                    list.add(new TimelineData(stops.get(place),times.get(place), num%100,0));
                                }
                                else {
                                    list.add(new TimelineData(stops.get(place),times.get(place), 100,0));
                                }
                            }

                            else if (place==(num/100)-1){
                                if (num%100+1>80) {
                                    Toast.makeText(MainActivity.this,String.valueOf(num),Toast.LENGTH_SHORT).show();
                                    list.add(new TimelineData(stops.get(place),times.get(place), 100,1));
                                    place++;
                                    num= num+20;
                                    Toast.makeText(MainActivity.this,String.valueOf(num%100),Toast.LENGTH_SHORT).show();
                                    list.add(new TimelineData(stops.get(place),times.get(place), (num%100)/2,3));
                                } else{
                                    list.add(new TimelineData(stops.get(place),times.get(place), (num%100)+20,2));
                                }
                            }
                            else {
                                list.add(new TimelineData(stops.get(place),times.get(place), 100,1));
                            }
                        }
                        for (place = place;place<stops.size();place++){
                            list.add(new TimelineData(stops.get(place),times.get(place) ,100,4));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        }

}
