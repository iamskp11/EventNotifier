package com.example.eventnotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity2 extends AppCompatActivity {
    TextInputEditText name,nickname;
    RadioGroup event_type;
    RadioButton bday,anniversary,selected;
    EditText date,month,year;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        name=findViewById(R.id.name);
        nickname=findViewById(R.id.nickname);
        event_type=findViewById(R.id.event_type);
        bday=findViewById(R.id.bday);
        anniversary=findViewById(R.id.anniv);
        date=findViewById(R.id.date);
        month=findViewById(R.id.month);
        year=findViewById(R.id.year);
        submit=findViewById(R.id.btn);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean flag=true;


                String InputName=name.getText().toString();
                String InputNickName=nickname.getText().toString();
                /*Toast toast1= Toast.makeText(getApplicationContext(),
                        "-----"+InputName.length()+"========"+InputNickName.length(),
                        Toast.LENGTH_SHORT);
                toast1.show();*/
                if(InputName.length()==0) flag=false;
                if(InputNickName.length()==0) flag=false;
                int selectedId=event_type.getCheckedRadioButtonId();
                if(selectedId==-1) flag=false;
                else selected=(RadioButton)findViewById(selectedId);

                String date_input,month_input,year_input;
                date_input=date.getText().toString();
                month_input=month.getText().toString();
                year_input=year.getText().toString();

                int event_date=0,event_month=0,event_year=0;

                if(date_input.length()>0)
                event_date=Integer.parseInt(date_input);
                else flag=false;

                if(month_input.length()>0)
                event_month=Integer.parseInt(month_input);
                else flag=false;

                if(year_input.length()>0)
                event_year=Integer.parseInt(year_input);
                else flag=false;

                if(flag)
                flag=check_eventDate(event_date,event_month,event_year);


                String []text={"Incorrect Details! Please Crosscheck!","Wow! Data is Correct"};
                if(!flag)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            text[0],
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    //Now, I have all accurate results. Time to pass it to previous activity
                    Intent intent=new Intent();
                    intent.putExtra("name", InputName);
                    intent.putExtra("nick_name", InputNickName);
                    intent.putExtra("event_type", selected.getText());
                    intent.putExtra("date", Integer.toString(event_date));
                    intent.putExtra("month", Integer.toString(event_month));
                    intent.putExtra("year", Integer.toString(event_year));
                    setResult(RESULT_OK, intent);
                    finish();
                }


                
            }
        });
    }

    boolean check_eventDate(int dd,int mm,int yy)
    {
        int []days={31,28,31,30,31,30,31,31,30,31,30,31};

        // Motnh checked
        if(mm<=0 || mm>12) return false;
        if(mm==2 && yy%4==0) days[1]++;
        int max_date_allowed=days[mm-1];

        // Number of days checked
        if(dd<0 || dd>max_date_allowed) return false;

        // Year must not be greater than current date

        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

        Date currentTime = localCalendar.getTime();
        int currentDay = localCalendar.get(Calendar.DATE);
        int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
        int currentYear = localCalendar.get(Calendar.YEAR);

        if(yy>=currentYear)
        {
            if(yy>currentYear) return false;
            // Same year
            if(mm >= currentMonth)
            {
                if(mm>currentMonth) return false;
                if(dd>currentDay) return false;
            }
            return true;
        }

        return true;


    }
}