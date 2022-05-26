package com.example.eventnotifier;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class EventAdapter extends ArrayAdapter<Event> {

    private final ArrayList<Event> events;
    private Context context;
    public EventAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Event> objects) {
        super(context,resource,objects);
        this.context=context;
        this.events=objects;
    }

    @Nullable
    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.events_layout,parent,false);

        TextView tv_name=convertView.findViewById(R.id.adap_name);
        TextView tv_nickname=convertView.findViewById(R.id.adap_nickname);
        TextView tv_eventtype=convertView.findViewById(R.id.adap_eventtype);
        TextView tv_comingup=convertView.findViewById(R.id.adap_comingup);
        Button del=convertView.findViewById(R.id.del);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.handler.deleteEvent(getItem(position));

            }
        });

        Event curr=getItem(position);

        tv_name.setText(curr.name);
        tv_nickname.setText(curr.nick_name);
        tv_eventtype.setText(curr.event_type);

        int dd=curr.date;
        int mm=curr.month;
        int yy=curr.year;


        String days_rem=get_rem_date(dd,mm,yy);

        tv_comingup.setText("Coming in "+days_rem+" days");

        return convertView;


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
}
