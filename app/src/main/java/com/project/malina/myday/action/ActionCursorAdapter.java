package com.project.malina.myday.action;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.project.malina.myday.R;
import com.project.malina.myday.data.SQLDatabaseHelper;

import java.util.Calendar;

public class ActionCursorAdapter extends CursorAdapter {

    private Context mContext;
    private SQLDatabaseHelper databaseHelper;
    public ActionCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.list_item_action, parent, false);
        return retView;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        databaseHelper = new SQLDatabaseHelper(mContext);

        final Calendar c = Calendar.getInstance();

        final TextView title = (TextView) view.findViewById(R.id.title_action);
        final String text = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
        title.setText(text);

        final String toastMessage;
        final String state;
        Button button = (Button) view.findViewById(R.id.button_action);
        button.setTag(cursor.getPosition());
        if (cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))).equals("0")){
            button.setText(R.string.start);
            toastMessage = context.getString(R.string.started);
            state = "1";
        } else{
            button.setText(R.string.on_process);
            toastMessage = context.getString(R.string.stoped);
            state = "0";
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Toast.makeText(mContext, toastMessage, Toast.LENGTH_SHORT).show();
                databaseHelper.updateButtonState(position + 1, state);
                if (state.equals("1")){
                    databaseHelper.insertJournalNote(text);
                } else{
//                    databaseHelper.updateJournalStopTime(text, c.getTimeInMillis());
                    databaseHelper.updateJournalStopTime(text);
                }
                changeCursor(databaseHelper.getActionData());
            }
        });
    }
}
