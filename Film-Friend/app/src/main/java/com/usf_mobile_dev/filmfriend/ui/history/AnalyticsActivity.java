package com.usf_mobile_dev.filmfriend.ui.history;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.usf_mobile_dev.filmfriend.R;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        /*PieChart pie = (PieChart) findViewById(R.id.genre_pie_chart);
        PieChart test = (PieChart) findViewById(R.id.test_pie_chart);
        pie.setTouchEnabled(false);
        test.setTouchEnabled(false);

        List<PieEntry> entries = new ArrayList<>();
        PieEntry x = new PieEntry(30.8f, "Blue");

        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));

        PieDataSet set = new PieDataSet(entries, "Election Results");

        set.setColors(new int[] {R.color.green, R.color.yellow, R.color.red, R.color.blue}, this);

        PieData data = new PieData(set);
        data.setValueTextSize((float) 16.0);
        pie.setData(data);
        test.setData(data);*/


    }
}
