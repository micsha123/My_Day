package com.project.malina.myday.journal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.project.malina.myday.R;
import com.project.malina.myday.data.SQLDatabaseHelper;

public class JournalFragment extends Fragment {

    private JournalCursorAdapter customAdapter;
    private SQLDatabaseHelper databaseHelper;
    private ListView listView;

    public JournalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listview, null);
        databaseHelper = new SQLDatabaseHelper(getActivity());

        listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setEmptyView(rootView.findViewById(R.id.empty_view));

        listView.setItemsCanFocus(true);

        listView.setLongClickable(true);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, final View v, final int pos, long id) {

                final String[] stringArray = {getActivity().getResources().getString(R.string.delete) };
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(stringArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                            databaseHelper.deleteJournalNote(Long.toString(customAdapter.getItemId(pos)));
                            customAdapter.changeCursor(databaseHelper.getJournalData());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                customAdapter = new JournalCursorAdapter(getActivity(), databaseHelper.getJournalData());
                listView.setAdapter(customAdapter);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        customAdapter.changeCursor(databaseHelper.getJournalData());
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_actions, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_add) {
//            itemDialog(null, null);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

}
