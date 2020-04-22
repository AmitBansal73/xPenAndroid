package net.anvisys.xpen.HomePage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.DefaultXAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import net.anvisys.xpen.Common.APP_CONST;
import net.anvisys.xpen.Object.Expense;
import net.anvisys.xpen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {


    TextView txtInProgCount,txtInProgress, txtSubCount, txtSubmitted,txtApprovedCount, txtApproved;

    com.github.mikephil.charting.charts.PieChart   pie_chart;
    com.github.mikephil.charting.charts.BarChart barchart;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        pie_chart = view.findViewById(R.id.pie_chart);
        barchart = view.findViewById(R.id.barchart);
        CreateExpenseChart();
        CreateDailyExpChart();
        return  view;
    }


    public void CreateDailyExpChart() {

        ArrayList Expense = new ArrayList();

        Expense.add(new BarEntry(945f, 0));
        Expense.add(new BarEntry(1040f, 1));
        Expense.add(new BarEntry(1533f, 2));
        Expense.add(new BarEntry(1240f, 3));
        Expense.add(new BarEntry(2069f, 4));
        Expense.add(new BarEntry(1487f, 5));
        Expense.add(new BarEntry(1501f, 6));

        ArrayList Day = new ArrayList();
        Day.add("Monday");
        Day.add("Tuesday");
        Day.add("Wednesday");
        Day.add("Thursday");
        Day.add("Friday");
        Day.add("Saturday");
        Day.add("Sunday");


        BarDataSet bardataset = new BarDataSet(Expense, "Daily Expense");
        barchart.animateY(5000);
        BarData data = new BarData(Day, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barchart.getLegend().setEnabled(false);
        barchart.setFitsSystemWindows(true);
        XAxis xAxis = barchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new DefaultXAxisValueFormatter());
        barchart.setData(data);

    }

    public void CreateExpenseChart() {
        try {


            ArrayList<String> labels = new ArrayList<String>();
            labels.add("Local Data");
            labels.add("In Progress");
            labels.add("submitted");
            labels.add("Approved");



            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry((float) 12000, 0));
            entries.add(new Entry((float) 450000, 1));
            entries.add(new Entry((float) 230000, 2));
            entries.add(new Entry((float) 100000, 3));

            PieDataSet dataset = new PieDataSet(entries, "# of votes");
            dataset.setColors(ColorTemplate.COLORFUL_COLORS);
            dataset.setSliceSpace(2f);
            dataset.setSelectionShift(5f);
            pie_chart.setTouchEnabled(true);
            pie_chart.setHighlightPerTapEnabled(true);
            //  dataset.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            // set the color
            PieData data = new PieData(labels, dataset); // initialize Piedata
            pie_chart.getLegend().setPosition(Legend.LegendPosition.LEFT_OF_CHART);
            pie_chart.getLegend().setEnabled(false);
            pie_chart.setHoleRadius(0f);
            data.setValueTextSize(13f);
            pie_chart.setData(data);
            /*
                    ChartData values = new ChartData();
                    values.setSectorValue((float)5000);
                    values.setSectorLabel("Local");
                    semiChart.addSector(values);
                    values = new ChartData();
                    values.setSectorValue((float)3200);
                    values.setSectorLabel("In Progress");
                    semiChart.addSector(values);
                    values = new ChartData();
                    values.setSectorValue((float)5000);
                    values.setSectorLabel("Submitted");
                    semiChart.addSector(values);
                    values = new ChartData();
                    values.setSectorValue((float)12000);
                    values.setSectorLabel("Approved");
                    semiChart.addSector(values);*/

        } catch (Exception e) {
            int a=1;
        }
    }


}
