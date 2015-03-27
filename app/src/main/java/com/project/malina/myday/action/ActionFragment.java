package com.project.malina.myday.action;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.project.malina.myday.R;
import com.project.malina.myday.data.SQLDatabaseHelper;
import com.project.malina.myday.graph.GraphActivity;

public class ActionFragment extends Fragment {

    private ActionCursorAdapter customAdapter;
    private SQLDatabaseHelper databaseHelper;
    private ListView listView;

    public ActionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listview, null);
        databaseHelper = new SQLDatabaseHelper(getActivity());

        listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setEmptyView(rootView.findViewById(R.id.empty_view));

        listView.setItemsCanFocus(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String title = ((TextView) view.findViewById(R.id.title_action)).getText().toString();
                itemDialog(title, Long.toString(customAdapter.getItemId(position)));
            }
        });

        listView.setLongClickable(true);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, final View v, final int pos, long id) {

                final String[] stringArray = {getActivity().getResources().getString(R.string.edit),
                        getActivity().getResources().getString(R.string.show_graph),
                        getActivity().getResources().getString(R.string.delete) };
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(stringArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        String title = ((TextView) v.findViewById(R.id.title_action)).getText().toString();
                        switch (item){
                            case 0:
                                title = ((TextView) v.findViewById(R.id.title_action)).getText().toString();
                                itemDialog(title, Long.toString(customAdapter.getItemId(pos)));
                                break;
                            case 1:
                                Intent intent = new Intent(getActivity(), GraphActivity.class);
                                intent.putExtra("title", title);
                                getActivity().startActivity(intent);
                                break;
                            default:
                                databaseHelper.deleteAction(Long.toString(customAdapter.getItemId(pos)));
                                customAdapter.changeCursor(databaseHelper.getActionData());
                        }
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
                customAdapter = new ActionCursorAdapter(getActivity(), databaseHelper.getActionData());
                listView.setAdapter(customAdapter);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void itemDialog(String title, final String id){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        if (id == null) {
            alert.setTitle(R.string.enter_label);
        } else{
            alert.setTitle(R.string.edit_label);
        }
        final EditText input = new EditText(getActivity());
        input.setTextColor(Color.parseColor("#000000"));
        if(title != null){
            input.setText(title);
        }
        alert.setView(input);

        alert.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String title = input.getText().toString();
                if (title.length() != 0) {
                    insertActionDB(title, id);
                }
            }
        });

        alert.setNegativeButton(R.string.revert, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.setOnKeyListener(new AlertDialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (input.getText().toString().trim().length() > 0) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.save_item);
                        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = input.getText().toString();
                                if (title.length() != 0) {
                                    insertActionDB(title, id);
                                    dialog.dismiss();
                                }
                            }
                        });
                        builder.setNegativeButton(R.string.revert, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    } else {
                        dialog.dismiss();
                    }
                    dialog.dismiss();
                }
                return true;
            }
        });
        alert.show();
    };

    private void insertActionDB(String title, String id){
        if (id == null){
            databaseHelper.insertAction(title);
        } else{
            databaseHelper.updateAction(id, title);
        }
        customAdapter.changeCursor(databaseHelper.getActionData());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            itemDialog(null, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

}
