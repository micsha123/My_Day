package com.project.malina.myday.graph;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.project.malina.myday.R;
import com.project.malina.myday.data.SQLDatabaseHelper;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;


public class GraphFragment extends Fragment {

    private SQLDatabaseHelper databaseHelper;

    private OnFragmentInteractionListener mListener;

    public GraphFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        databaseHelper = new SQLDatabaseHelper(getActivity());
        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        Cursor cursor = databaseHelper.getJournalDataForGraph(title);
        if (cursor.getCount()>2){
            DataPoint[] points = new DataPoint[cursor.getCount()];
            Date[] date = new Date[cursor.getCount()];
            if (cursor.moveToFirst())
            {
                for (int i = 0; i < cursor.getCount(); i++)
                {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yy");
                    date[i] = formatter.parseDateTime(cursor.getString(1)).toDate();
                    if (cursor.isNull(5)){
                        points[i] = points[i-1];
                    } else{
                        points[i] = new DataPoint(date[i], Integer.parseInt(cursor.getString(5)));
                    }
                    cursor.moveToNext();
                }
            }
            GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(points);

            graph.addSeries(series);
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        } else{
            TextView emptyGraph = (TextView) rootView.findViewById(R.id.emptyGraph);
            emptyGraph.setText("You need 2 or more records in journal to watch graph!");
        }
        return rootView;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


}
