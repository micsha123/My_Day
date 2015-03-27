package com.project.malina.myday.graph;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.project.malina.myday.R;

public class GraphActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Bundle extras = getIntent().getExtras();
        String title = null;
        if(extras != null)
        {
            title = extras.getString("title");
        }
        if (savedInstanceState == null) {
            GraphFragment graph = new GraphFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            graph.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(
                    R.id.container, graph).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
