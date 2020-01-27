package com.example.getmybus.helper;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperRoute {
List<GeoPoint> points = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public DatabaseHelperRoute() {
        CollectionReference documentrefferance = db.collection("users");
//        documentrefferance.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                points = (List<GeoPoint>) queryDocumentSnapshots.getDocuments().get(0).get("route");
//            }
//        });

    }
    public List<GeoPoint> getRoutes(){
        return points;
    }
}
