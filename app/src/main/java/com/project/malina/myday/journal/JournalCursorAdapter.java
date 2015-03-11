package com.project.malina.myday.journal;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.project.malina.myday.R;
import com.project.malina.myday.data.SQLDatabaseHelper;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.concurrent.TimeUnit;

public class JournalCursorAdapter extends CursorAdapter {

    private Context mContext;
    private SQLDatabaseHelper databaseHelper;
    public JournalCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.list_item_journal, parent, false);
        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        databaseHelper = new SQLDatabaseHelper(mContext);

        TextView title = (TextView) view.findViewById(R.id.title_journal);
        TextView date = (TextView) view.findViewById(R.id.date_journal);
        TextView time = (TextView) view.findViewById(R.id.time_journal);

        String aTitle = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));
        title.setText(aTitle);
        date.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        String startTime = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)));
        String stopTime = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));
        if (stopTime.equals("0")){
            time.setText(R.string.on_process);
        } else{
            if(cursor.isNull(cursor.getColumnIndex(cursor.getColumnName(5)))){
                DateTimeFormatter formatter = DateTimeFormat.forPattern("yy-MM-dd HH:mm:ss");
                DateTime start = formatter.parseDateTime(startTime);
                DateTime stop = formatter.parseDateTime(stopTime);
                Minutes minutes = Minutes.minutesBetween(start, stop);
                databaseHelper.updateJournalTime(aTitle, minutes.getMinutes());
            }
            int minutes = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(5)));
            time.setText(String.format("%02d:%02d",
                    TimeUnit.MINUTES.toHours(minutes),
                    TimeUnit.MINUTES.toMinutes(minutes) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(minutes))));
//            time.setText(String.format("%02d:%02d",
//                    TimeUnit.MILLISECONDS.toHours(millis),
//                    TimeUnit.MILLISECONDS.toMinutes(millis) -
//                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))));
        }
    }

}
