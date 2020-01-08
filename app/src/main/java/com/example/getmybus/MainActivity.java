package com.example.getmybus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main activity";
    LinearLayout l;
    TextView t;
    TextView title;
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
        final List<TimelineData> list= new ArrayList<>();
//        final Timeline timeline= findViewById(R.id.timeline);
//        final EditText editText = findViewById(R.id.change_sm);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                timeline.setHight(Integer.parseInt(String.valueOf(editText.getText())));
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        timeline.setHight(300);
//        title = (TextView) findViewById(R.id.find_bus_title);
//        l = (LinearLayout)findViewById(R.id.history_view);
//        t = (TextView) findViewById(R.id.hide_text);
//        ViewTreeObserver observer = l.getViewTreeObserver();
//        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
//                // TODO Auto-generated method stub
//                Log.i(TAG, "onGlobalLayout: Height"+l.getHeight());
//                if(l.getHeight()<250){
//                    if(t.getVisibility()==View.GONE){
////                        next view hide
//                        if (title.getVisibility()!=View.GONE){
////                                title.setVisibility(View.GONE);
//                        }
////                        l.getViewTreeObserver().removeGlobalOnLayoutListener(
////                                this);
//                    }
//                    else {
//                        t.setVisibility(View.GONE);
//                    }
//                }

//            }
//        });

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        // Create a new user with a first, middle, and last name
        final Map<String, Object> user = new HashMap<>();
        user.put("first", "Alan");
        user.put("middle", "Mathison");
        user.put("last", "Turing");
        user.put("born", 1912);
//
// Add a new document with a generated ID
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });


        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        //TODO recycler view calculation from the database
//      for(int i=0;i<=percent)
//        list.add(new TimelineData("Cherthala", 100,0));
//        list.add(new TimelineData("Kvm hospital", 100,1));
//        list.add(new TimelineData("Mathilakam", 100,1));
//        list.add(new TimelineData("11th Mile", 50,2));
//        list.add(new TimelineData("bhajanamadam", 0,3));
//        list.add(new TimelineData("pallikkavala", 0,3));
        final RecyclerviewAdapter adapter = new RecyclerviewAdapter(list);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        // Access a Cloud Firestore instance from your Activity
        db.collection("bus_test").document("WsCdTluxXcgIITYBxaWe")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException e) {
                            List<String> stops = (List<String>) document.get("routes");
                            List<String> times = (List<String>) document.get("times");
//                            Map<String, String> place_info = (Map<String, String>) document.get("place_info");
//                            for (Map.Entry<String,String> entry : place_info.entrySet()){
//                                stops.add(entry.getKey());
//                                times.add(entry.getValue());
//                            }
                        Log.d(TAG, document.getId() + " => " + document.getData());
                            document.getData().putAll(user);
                            Log.d(TAG, "onComplete: "+document.getLong("id"));
                            long js = document.getLong("percent");
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
