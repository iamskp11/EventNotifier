package com.example.eventnotifier;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private final static int MY_REQUEST_CODE = 1;
    public static int CHANGED=0;
    Button btn1;
    public static DbHandler handler;
    ListView lst;
    EventAdapter ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler=new DbHandler(this,"Events",null,1);

        btn1=findViewById(R.id.btn1);
        lst=findViewById(R.id.lstview);

        final Handler handler2 = new Handler();
        final int delay = 3600*1000; // 1000 milliseconds == 1 second
        query_db();
        handler2.postDelayed(new Runnable() {
            public void run() {
                query_db();
                handler2.postDelayed(this, delay);
            }
        }, delay);

        final Handler handler3 = new Handler();
        final int delay3 = 1000; // 1000 milliseconds == 1 second
        handler3.postDelayed(new Runnable() {
            public void run() {
                if(CHANGED==1)
                {
                    query_db();
                    CHANGED=0;
                }
                handler3.postDelayed(this, delay3);
            }
        }, delay3);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                    startActivityForResult(intent,MY_REQUEST_CODE);
            }
        });
    }

    public int numDaysPassed(int dd,int mm,int yy)
    {
        int []days={31,28,31,30,31,30,31,31,30,31,30,31};
        if(yy%4==0) days[1]++;
        int tot=0;
        for(int mnth=1;mnth<=mm;mnth++)
        {
            if(mnth!=mm) tot+=days[mnth-1];
            else tot+=dd;
        }
        return tot;
    }
    public String get_rem_date(int dd,int mm,int yy)
    {
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

        Date currentTime = localCalendar.getTime();
        int currentDay = localCalendar.get(Calendar.DATE);
        int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
        int currentYear = localCalendar.get(Calendar.YEAR);

        int this_year_days=numDaysPassed(currentDay,currentMonth,currentYear);
        int event_days=numDaysPassed(dd,mm,currentYear);

        if(event_days==this_year_days) return "0";
        else if(event_days>this_year_days) return Integer.toString(event_days-this_year_days);
        return Integer.toString(event_days-this_year_days+365);

    }
    private boolean check_if_greater(Event a, Event b)
    {
        int dd1=a.date;
        int mm1=a.month;
        int yy1=a.year;

        int dd2=b.date;
        int mm2=b.month;
        int yy2=b.year;

        int rem1=Integer.parseInt(get_rem_date(dd1,mm1,yy1));
        int rem2=Integer.parseInt(get_rem_date(dd2,mm2,yy2));

        return rem1 > rem2;
    }
    public  void query_db()
    {
        ArrayList<Event> e= handler.getEvents();
        // Sort Event here itself
        //Bubble sort of ArrayList
        for (int i = 0; i < e.size() - 1; i++) {
            for (int j = e.size() - 1; j > i; j--) {
                if (check_if_greater(e.get(j - 1),e.get(j))) {
                    //Swap
                    Event tmp = e.get(j - 1);
                    e.set(j -1, e.get(j));
                    e.set(j, tmp);
                }
            }
        }

        ad=new EventAdapter(this,R.layout.events_layout,e);
        lst.setAdapter(ad);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQUEST_CODE) {
                if (data != null)
                {
                    String name=data.getStringExtra("name");
                    String nick_name=data.getStringExtra("nick_name");
                    String event_type=data.getStringExtra("event_type");
                    String date=data.getStringExtra("date");
                    String month=data.getStringExtra("month");
                    String year=data.getStringExtra("year");

                    Toast toast = Toast.makeText(getApplicationContext(),
                            name+","+nick_name+","+event_type+","+date+"/"+month+"/"+year,
                            Toast.LENGTH_SHORT);
                    toast.show();

                    Event e=new Event(name,nick_name,event_type,Integer.parseInt(date),
                            Integer.parseInt(month),Integer.parseInt(year));
                    handler.AddEvents(e);
                    query_db();
                }
            }
        }
    }
}