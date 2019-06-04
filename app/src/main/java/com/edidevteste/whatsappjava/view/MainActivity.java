package com.edidevteste.whatsappjava.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.edidevteste.whatsapp.R;

public class MainActivity extends AppCompatActivity {

    //private DatabaseReference mReferenceFirebase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mReferenceFirebase.child("pontos").setValue(100);
    }
}
