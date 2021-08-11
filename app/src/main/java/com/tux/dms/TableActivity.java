package com.tux.dms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.tux.dms.dto.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    List<Ticket> tickets=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        addHeaders();
        addData();

    }

    public void addHeaders() {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0, "Sl No.", Color.BLACK, Typeface.BOLD, R.color.purple_500));
        tr.addView(getTextView(0, "Subject", Color.BLACK, Typeface.BOLD, R.color.purple_500));
        tr.addView(getTextView(0, "Source", Color.BLACK, Typeface.BOLD, R.color.purple_500));
        tl.addView(tr, getTableLayoutParams());
    }

    public void addData() {
        int z=0;
        TableLayout tl = findViewById(R.id.table);
        for (int i = 0; i < tickets.size(); i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(getLayoutParams());
            //id column
            tr.addView(getTextView(i, Integer.toString(++z), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.white)));
            //adding subject column and making it clickable
            TextView tv = new TextView(this);
            tv.setId(i);
            tv.setText(tickets.get(i).getSubject());
            tv.setTextColor(Color.BLACK);
            tv.setPadding(40, 40, 40, 40);
            tv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tv.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            tv.setLayoutParams(getLayoutParams());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(TableActivity.this,TicketDetailsFragment.class);
                    startActivity(i);

                }
            });
            tr.addView(tv);
            tr.addView(getTextView(i, tickets.get(i).getSource(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.white)));

            tl.addView(tr, getTableLayoutParams());

        }



    }



    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        return tv;
    }

    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }
    private TableLayout.LayoutParams getTableLayoutParams() {
        return new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
    }
}