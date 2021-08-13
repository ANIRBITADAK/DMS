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

import com.tux.dms.cache.SessionCache;
import com.tux.dms.dto.Ticket;
import com.tux.dms.dto.TicketList;
import com.tux.dms.rest.ApiClient;
import com.tux.dms.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class TableActivity extends AppCompatActivity {
    ApiInterface apiInterface = ApiClient.getApiService();
    SessionCache sessionCache = SessionCache.getSessionCache();
    //List<Ticket> tickets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        String token = sessionCache.getToken();
        Call<TicketList> tickList = apiInterface.getTickets(token, null,"NEW", null,
                1, 5);
        tickList.enqueue(new Callback<TicketList>() {
            @Override
            public void onResponse(Call<TicketList> call, Response<TicketList> response) {
                System.out.println("got ticket list" + response.body());
                TicketList ticketList = response.body();
                addHeaders();
                addData(ticketList.getTickets());
            }

            @Override
            public void onFailure(Call<TicketList> call, Throwable t) {

            }
        });
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

    public void addData(List<Ticket> tickets) {
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