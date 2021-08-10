package com.tux.dms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PriorityDetails extends AppCompatActivity {

    CardView high,medium,low;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_details);

        high=findViewById(R.id.high);
        medium=findViewById(R.id.medium);
        low=findViewById(R.id.low);

        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(PriorityDetails.this,TableActivity.class);
                startActivity(i);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(PriorityDetails.this,TableActivity.class);
                startActivity(i);
            }
        });

        low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(PriorityDetails.this,TableActivity.class);
                startActivity(i);
            }
        });
    }
}