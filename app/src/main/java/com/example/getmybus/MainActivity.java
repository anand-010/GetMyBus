package com.example.getmybus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getmybus.Recyclerview_elements.RecyclerviewAdapter;
import com.example.getmybus.Recyclerview_elements.TimelineData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main activity";
    LinearLayout l;
    TextView t;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout);
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
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        List<TimelineData> list= new ArrayList<>();
        list.add(new TimelineData("Cherthala", 100,0));
        list.add(new TimelineData("Kvm hospital", 100,1));
        list.add(new TimelineData("Mathilakam", 100,1));
        list.add(new TimelineData("11th Mile", 20,2));
        RecyclerviewAdapter adapter = new RecyclerviewAdapter(list);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        }

}
