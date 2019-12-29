package com.example.getmybus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.king.zxing.CaptureActivity;
import com.king.zxing.Intents;
import com.king.zxing.util.CodeUtils;

public class TestActivity extends AppCompatActivity {
    int requestCode = 106;
    ImageView imageView;
    Button bt1,bt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        imageView = findViewById(R.id.show_here);
        bt1 = findViewById(R.id.gen_btn);
        bt2 = findViewById(R.id.scan_btn);
        Context context = TestActivity.this;
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, CaptureActivity.class),requestCode);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.point_yellow);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);
                Bitmap bitmap = CodeUtils.createQRCode("Hai there",600,smallMarker);
                imageView.setImageBitmap(bitmap);
            }
        });

        //跳转的默认扫码界面


        //生成二维码

//        //生成条形码
//        CodeUtils.createBarCode(content, BarcodeFormat.CODE_128,800,200);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == this.requestCode) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                // get String data from Intent
                String returnString = data.getStringExtra(Intents.Scan.RESULT);
                Toast.makeText(this,returnString,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
