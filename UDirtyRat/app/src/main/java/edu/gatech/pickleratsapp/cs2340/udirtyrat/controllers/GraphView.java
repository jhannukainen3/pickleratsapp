package edu.gatech.pickleratsapp.cs2340.udirtyrat.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import edu.gatech.pickleratsapp.cs2340.udirtyrat.Model.ChartData;
import edu.gatech.pickleratsapp.cs2340.udirtyrat.Model.Model;
import edu.gatech.pickleratsapp.cs2340.udirtyrat.Model.XAxisDateFormatter;
import edu.gatech.pickleratsapp.cs2340.udirtyrat.R;

/**
 * Class to display a graph
 */

public class GraphView extends AppCompatActivity {
    private Model model;
//    private String startYear = "09/04/2015";
//    private String endYear = "09/05/2015";
//    private RatReport latestRatReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);
        //LineChart lineChart = (LineChart) findViewById(R.id.chart);
        model = Model.get_instance();
        final Context context = GraphView.this;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePicker startDate = new DatePicker(
                        edu.gatech.pickleratsapp.cs2340.udirtyrat.controllers.GraphView.this);
                final DatePicker endDate = new DatePicker(
                        edu.gatech.pickleratsapp.cs2340.udirtyrat.controllers.GraphView.this);
                LinearLayout linearLayout = new LinearLayout(
                        edu.gatech.pickleratsapp.cs2340.udirtyrat.controllers.GraphView.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(startDate);
                linearLayout.addView(endDate);
                alertDialog.setTitle("Choose a date range");

                ScrollView scroller = new ScrollView(
                        edu.gatech.pickleratsapp.cs2340.udirtyrat.controllers.GraphView.this);
                scroller.addView(linearLayout);

                alertDialog.setView(scroller);
                alertDialog.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final int startDay = startDate.getDayOfMonth();
                                final int startMonth = startDate.getMonth();
                                final int startYear = startDate.getYear();
                                final int endDay = endDate.getDayOfMonth();
                                final int endMonth = endDate.getMonth();
                                final int endYear = endDate.getYear();
                                Log.d("Start Month ", " " + startMonth);
                                Log.d("Start Day ", " " + startDay);
                                Log.d("Start Year ", " " + startYear);
                                Log.d("End Month ", " " + endMonth);
                                Log.d("End Day ", " " + endDay);
                                Log.d("End Year ", " " + endYear);
                                Log.d("Date Range", startYear + " - " + endYear);
                                GregorianCalendar startDate = new GregorianCalendar(startYear,
                                        startMonth, startDay);
                                GregorianCalendar endDate = new GregorianCalendar(endYear,
                                        endMonth, endDay);
                                List<ChartData> lineData = model.get_data_in_range(startDate,
                                        endDate);
                                String[] labels = new String[lineData.size()];
                                String[] monthsRef = {"Jan", "Feb", "Mar", "Apr", "May",
                                    "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                                ArrayList<Entry> entries = new ArrayList<>();
                                for (int i = 0; i < lineData.size(); i++) {
                                    labels[i] = monthsRef[(startMonth + i) % 12] + ", "
                                        + (startYear + ((startMonth + i) / 12));
                                    entries.add(new Entry(i, lineData.get(i).getY()));
                                }
                                LineDataSet lineSet = new LineDataSet(entries, "# of Reports");
                                LineChart chart = new LineChart(context);
                                setContentView(chart);
                                LineData data = new LineData(lineSet);
                                chart.getXAxis().setValueFormatter(new XAxisDateFormatter(labels));
                                // enable touch gestures
                                chart.setTouchEnabled(true);

                                // enable scaling and dragging
                                chart.setDragEnabled(true);
                                chart.setScaleEnabled(true);
                                // mChart.setScaleXEnabled(true);
                                // mChart.setScaleYEnabled(true);

                                // if disabled, scaling can be done on x- and y-axis separately
                                chart.setPinchZoom(true);
                                chart.setData(data);
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });
    }
}