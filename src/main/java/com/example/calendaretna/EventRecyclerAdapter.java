package com.example.calendaretna;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Event> arrayList;
    DBOpenHelper dbOpenHelper;

    public EventRecyclerAdapter(Context context, ArrayList<Event> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event event = arrayList.get(position);
        holder.Event.setText(event.getEventName());
        holder.DateTxt.setText(event.getDate());
        holder.Time.setText(event.getTime());
        holder.add_guest.setOnClickListener(view -> {
            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            View addGuestView = LayoutInflater.from(context).inflate(R.layout.add_guest_layout, null);
            Button add_guest_btn = addGuestView.findViewById(R.id.add_guest_btn);
            add_guest_btn.setOnClickListener(view1 -> {
                EditText mailGuestEditText = addGuestView.findViewById(R.id.mailGuest);
                String emailGuest = mailGuestEditText.getText().toString();
               saveguest(event.getDate(), event.getIDEvent(), emailGuest);
            });
            builder.setView(addGuestView);
            alertDialog = builder.create();
            alertDialog.show();
                });
        holder.delete.setOnClickListener(view -> {
            deleteCalendarEvent(event.getEventName(), event.getDate(), event.getTime());
            arrayList.remove(position);
            notifyDataSetChanged();
        });


        if (isAlarmed(event.getDate(), event.getEventName(), event.getTime())) {
            holder.setAlarm.setImageResource(R.drawable.ic_action_notification_on);
        } else {
            holder.setAlarm.setImageResource(R.drawable.ic_action_notification_off);
        }
        Calendar datecalendar = Calendar.getInstance();
        datecalendar.setTime(ConvertStringToDate(event.getDate()));
        int alarmYear = datecalendar.get(Calendar.YEAR);
        int alarmMonth = datecalendar.get(Calendar.MONTH);
        int alarmDay = datecalendar.get(Calendar.DAY_OF_MONTH);
        Calendar timecalendar = Calendar.getInstance();
        timecalendar.setTime(ConvertStringToTime(event.getTime()));
        int alarmHour = timecalendar.get(Calendar.HOUR_OF_DAY);
        int alarmMinuit = timecalendar.get(Calendar.MINUTE);

        holder.setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAlarmed(event.getDate(), event.getEventName(), event.getTime())) {
                    holder.setAlarm.setImageResource(R.drawable.ic_action_notification_off);
                    cancelAlarm(getRequestCode(event.getDate(), event.getEventName(), event.getTime()));
                    updateEvent(event.getDate(), event.getEventName(), event.getTime(), "off");
                    notifyDataSetChanged();
                } else {
                    holder.setAlarm.setImageResource(R.drawable.ic_action_notification_on);
                    Calendar alarmCalendar = Calendar.getInstance();
                    alarmCalendar.set(alarmYear, alarmMonth, alarmDay, alarmHour, alarmMinuit);
                    setAlarm(alarmCalendar, event.getEventName(), event.getTime(), getRequestCode(event.getDate(),
                            event.getEventName(), event.getTime()));
                    updateEvent(event.getDate(), event.getEventName(), event.getTime(), "on");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView DateTxt, Event, Time;
        Button delete, add_guest;
        ImageButton setAlarm;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DateTxt = itemView.findViewById(R.id.eventdate);
            Event = itemView.findViewById(R.id.eventname);
            Time = itemView.findViewById(R.id.eventime);
            delete = itemView.findViewById(R.id.delete);
            add_guest = itemView.findViewById(R.id.add_guest);
            setAlarm = itemView.findViewById(R.id.alarmmeBtn);

        }
    }

    private Date ConvertStringToDate(String eventDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Date ConvertStringToTime(String eventDate) {
        SimpleDateFormat format = new SimpleDateFormat("kk:mm", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void saveguest(String date, String IDEvent, String mailGuest){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveGuest(date, IDEvent, mailGuest, database);
        dbOpenHelper.close();
    }

    private void deleteCalendarEvent(String event, String date, String time) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.deleteEvent(event, date, time, database);
        dbOpenHelper.close();
    }

    private boolean isAlarmed(String date, String event, String time) {
        boolean alarmed = false;
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadIDEvents(date, event, time, database);
        while (cursor.moveToNext()) {
            String notify = cursor.getString(cursor.getColumnIndex(DBStructure.Notify));
            if (notify.equals("on")) {
                alarmed = true;
            } else {
                alarmed = false;
            }
        }
        cursor.close();
        dbOpenHelper.close();
        return alarmed;
    }

    private void setAlarm(Calendar calendar, String eventName, String time, int RequestCOde) {
        Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("event", eventName);
        intent.putExtra("time", time);
        intent.putExtra("id", RequestCOde);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RequestCOde, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(int RequestCOde) {
        Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RequestCOde, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private int getRequestCode(String date, String eventName, String time) {
        int code = 0;
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadIDEvents(date, eventName, time, database);
        while (cursor.moveToNext()) {
            code = cursor.getInt(cursor.getColumnIndex(DBStructure.IDEVENT));
        }
        cursor.close();
        dbOpenHelper.close();
        return code;
    }

    private void updateEvent(String date, String eventName, String time, String notify) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.updateEvent(date, eventName, time, notify, database);
        dbOpenHelper.close();
    }

}
