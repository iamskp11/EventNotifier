package com.example.eventnotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity2 extends AppCompatActivity {
    TextInputEditText name, nickname;
    RadioGroup event_type;
    RadioButton bday, anniversary, selected;
    EditText date, month, year;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        name = findViewById(R.id.name);
        nickname = findViewById(R.id.nickname);
        event_type = findViewById(R.id.event_type);
        bday = findViewById(R.id.bday);
        anniversary = findViewById(R.id.anniv);
        date = findViewById(R.id.date);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        submit = findViewById(R.id.btn);
        date.setFocusable(false);
        date.setClickable(true);
        month.setFocusable(false);
        month.setClickable(true);
        year.setFocusable(false);
        year.setClickable(true);

        View.OnClickListener openDatePicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .build();
                picker.addOnPositiveButtonClickListener(selection -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(selection);
                    int d = cal.get(Calendar.DAY_OF_MONTH);
                    int m = cal.get(Calendar.MONTH) + 1;
                    int y = cal.get(Calendar.YEAR);
                    String formatted = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.getTime());
                    date.setText(formatted);
                    // Store numeric parts for saving
                    date.setTag(Integer.valueOf(d));
                    month.setText(Integer.toString(m));
                    year.setText(Integer.toString(y));
                });
                picker.show(getSupportFragmentManager(), "date_picker");
            }
        };

        date.setOnClickListener(openDatePicker);
        month.setOnClickListener(openDatePicker);
        year.setOnClickListener(openDatePicker);

        // Pre-fill in edit mode
        Intent incoming = getIntent();
        String mode = incoming.getStringExtra("mode");
        if (mode != null && mode.equals("edit")) {
            String origName = incoming.getStringExtra("orig_name");
            String origNick = incoming.getStringExtra("orig_nick_name");
            String origType = incoming.getStringExtra("orig_event_type");
            String origDate = incoming.getStringExtra("orig_date");
            String origMonth = incoming.getStringExtra("orig_month");
            String origYear = incoming.getStringExtra("orig_year");

            if (origName != null)
                name.setText(origName);
            if (origNick != null)
                nickname.setText(origNick);
            if (origType != null) {
                if ("Birthday".equalsIgnoreCase(origType))
                    bday.setChecked(true);
                else
                    anniversary.setChecked(true);
            }
            if (origDate != null) {
                try {
                    int d = Integer.parseInt(origDate);
                    date.setText(origDate);
                    date.setTag(Integer.valueOf(d));
                } catch (NumberFormatException ignore) {
                }
            }
            if (origMonth != null)
                month.setText(origMonth);
            if (origYear != null)
                year.setText(origYear);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean flag = true;

                String InputName = name.getText().toString();
                String InputNickName = nickname.getText().toString();
                /*
                 * Toast toast1= Toast.makeText(getApplicationContext(),
                 * "-----"+InputName.length()+"========"+InputNickName.length(),
                 * Toast.LENGTH_SHORT);
                 * toast1.show();
                 */
                if (InputName.length() == 0)
                    flag = false;
                if (InputNickName.length() == 0)
                    flag = false;
                int selectedId = event_type.getCheckedRadioButtonId();
                if (selectedId == -1)
                    flag = false;
                else
                    selected = (RadioButton) findViewById(selectedId);

                String date_input, month_input, year_input;
                date_input = date.getText().toString();
                month_input = month.getText().toString();
                year_input = year.getText().toString();

                int event_date = 0, event_month = 0, event_year = 0;

                if (date.getTag() instanceof Integer)
                    event_date = (Integer) date.getTag();
                else if (date_input.length() > 0) {
                    // fallback if tag not set and user typed a number
                    try {
                        event_date = Integer.parseInt(date_input);
                    } catch (NumberFormatException ex) {
                        flag = false;
                    }
                } else
                    flag = false;

                if (month_input.length() > 0)
                    event_month = Integer.parseInt(month_input);
                else
                    flag = false;

                if (year_input.length() > 0)
                    event_year = Integer.parseInt(year_input);
                else
                    flag = false;

                if (flag)
                    flag = check_eventDate(event_date, event_month, event_year);

                String[] text = { "Incorrect Details! Please Crosscheck!", "Wow! Data is Correct" };
                if (!flag) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            text[0],
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    // Now, I have all accurate results. Time to pass it to previous activity
                    Intent intent = new Intent();
                    intent.putExtra("name", InputName);
                    intent.putExtra("nick_name", InputNickName);
                    intent.putExtra("event_type", selected.getText());
                    intent.putExtra("date", Integer.toString(event_date));
                    intent.putExtra("month", Integer.toString(event_month));
                    intent.putExtra("year", Integer.toString(event_year));
                    // Pass through original identifiers when editing so caller can match
                    if (mode != null && mode.equals("edit")) {
                        intent.putExtra("is_edit", true);
                        intent.putExtra("orig_name", getIntent().getStringExtra("orig_name"));
                        intent.putExtra("orig_nick_name", getIntent().getStringExtra("orig_nick_name"));
                        intent.putExtra("orig_event_type", getIntent().getStringExtra("orig_event_type"));
                        intent.putExtra("orig_date", getIntent().getStringExtra("orig_date"));
                        intent.putExtra("orig_month", getIntent().getStringExtra("orig_month"));
                        intent.putExtra("orig_year", getIntent().getStringExtra("orig_year"));
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });
    }

    boolean check_eventDate(int dd, int mm, int yy) {
        int[] days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        // Motnh checked
        if (mm <= 0 || mm > 12)
            return false;
        if (mm == 2 && yy % 4 == 0)
            days[1]++;
        int max_date_allowed = days[mm - 1];

        // Number of days checked
        if (dd < 0 || dd > max_date_allowed)
            return false;

        // Year must not be greater than current date

        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

        Date currentTime = localCalendar.getTime();
        int currentDay = localCalendar.get(Calendar.DATE);
        int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
        int currentYear = localCalendar.get(Calendar.YEAR);

        if (yy >= currentYear) {
            if (yy > currentYear)
                return false;
            // Same year
            if (mm >= currentMonth) {
                if (mm > currentMonth)
                    return false;
                if (dd > currentDay)
                    return false;
            }
            return true;
        }

        return true;

    }
}