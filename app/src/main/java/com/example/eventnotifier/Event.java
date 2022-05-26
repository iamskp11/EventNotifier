package com.example.eventnotifier;

public class Event
{
   String name;
   String nick_name;
   String event_type;
   int date;
   int month;
   int year;

   public Event(String name, String nick_name, String event_type, int date, int month, int year) {
      this.name = name;
      this.nick_name = nick_name;
      this.event_type = event_type;
      this.date = date;
      this.month = month;
      this.year = year;
   }
}
