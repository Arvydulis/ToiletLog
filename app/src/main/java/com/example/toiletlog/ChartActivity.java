package com.example.toiletlog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private  AppDatabase db;
    EditText monthEditText;
    DatePickerDialog datePickerDialog;
    private Date month;

    List<DateCountResult> dateCountResultList;
    List<TypeCountResult> typeCountResultList;
    List<SizeCountResult> sizeCountResultList;

    BarChart chart;
    PieChart typePieChart;
    PieChart sizePieChart;
    BarData lineData;
    PieData pieTypeData;
    PieData pieSizeData;

    Navbar navbar = new Navbar();
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);


        //navbar.InstantiateAppBarAndNav(this, R.string.title_list);
        //drawerLayout = navbar.drawerLayout;

        // referencing charts
        chart = (BarChart) findViewById(R.id.chart);
        typePieChart = (PieChart) findViewById(R.id.chartPie1);
        sizePieChart = (PieChart) findViewById(R.id.chartPie2);

        monthEditText = (EditText) findViewById(R.id.setMonth);
        // perform click event on edit text
        monthEditText.setOnClickListener(monthPickerOnClick);
        GetCurrDate();

        // chart options
        chart.setBackgroundColor(Color.WHITE);
        chart.setDragEnabled(false);
        chart.setDescription(null);

        typePieChart.setBackgroundColor(Color.GRAY);
        typePieChart.setHoleRadius(30);
        typePieChart.setTransparentCircleRadius(35);
        typePieChart.getLegend().setWordWrapEnabled(true);
        typePieChart.setDrawEntryLabels(false);
        //typePieChart.getLegend().setEnabled(false);
        typePieChart.setDescription(null);

        sizePieChart.setBackgroundColor(Color.GRAY);
        sizePieChart.setHoleRadius(30);
        sizePieChart.setTransparentCircleRadius(35);
        sizePieChart.getLegend().setEnabled(false);
        sizePieChart.setDescription(null);

        // axis options
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        XAxis xAxis = chart.getXAxis();

        rightAxis.setEnabled(false);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(getDayCountFromMonth());
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        leftAxis.setAxisMinimum(0);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setGranularity(1);

        // referencing database
        db = AppActivity.getDatabase();

        //getting the info needed from database
        Calendar calendar2 = Calendar.getInstance();
        changeBarDataSet(calendar2);
        changeTypePieDataSet(calendar2);
        changeSizePieDataSet(calendar2);
    }

    public static int getDayFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    //Get number of days in current month
    public static int getDayCountFromMonth() {
        // Get the current month
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        // Set the calendar to the first day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // Add one month to move to the next month
        calendar.add(Calendar.MONTH, 1);
        // Subtract one day to get the last day of the current month
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        // Get the day of the month, which represents the number of days in the current month
        return calendar.get(Calendar.DAY_OF_MONTH);

    }

    //Get number of days in a specific month
    public static int getDayCountFromMonth(int month) {
        // Get the current month
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        // Set the calendar to the first day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // Add one month to move to the next month
        calendar.add(Calendar.MONTH, 1);
        // Subtract one day to get the last day of the current month
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        // Get the day of the month, which represents the number of days in the current month
        return calendar.get(Calendar.DAY_OF_MONTH);

    }

    void GetCurrDate(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        c.set(mYear, mMonth, mDay, 0,0, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        monthEditText.setText(sdf.format(c.getTime()));
        month = c.getTime();
    }

    View.OnClickListener monthPickerOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            c.setTime(month);
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(ChartActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set month and year value in the edit text
                            c.set(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                            monthEditText.setText(sdf.format(c.getTime()));
                            month = c.getTime();
                            lineData.removeDataSet(0);
                            changeBarDataSet(c);
                            pieTypeData.removeDataSet(0);
                            changeTypePieDataSet(c);
                            pieSizeData.removeDataSet(0);
                            changeSizePieDataSet(c);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };

    void changeBarDataSet(Calendar calendar)
    {
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int monthNum = calendar.get(Calendar.MONTH) + 1;
        String month;
        if (monthNum < 10) month = "0" + String.valueOf(calendar.get(Calendar.MONTH) + 1);
        else month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        dateCountResultList = db.logEntryDAO().getEntriesDateCounts(year, month);

        //putting the info inside an object that can go into chart
        List<BarEntry> entries = new ArrayList<BarEntry>();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd");
        for (DateCountResult data : dateCountResultList) {
            // turn your data into Entry objects
            entries.add(new BarEntry(getDayFromDate(data.date), data.count));
            //new Entry(data.getValueX(), data.getValueY())
        }


        BarDataSet dataSet = new BarDataSet(entries, "Count"); // add entries to dataset

        lineData = new BarData(dataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(getDayCountFromMonth(calendar.get(Calendar.MONTH)));
        chart.notifyDataSetChanged(); // let the chart know it's data changed
        chart.invalidate(); // refresh
    }

    void changeTypePieDataSet(Calendar calendar)
    {
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int monthNum = calendar.get(Calendar.MONTH) + 1;
        String month;
        if (monthNum < 10) month = "0" + String.valueOf(calendar.get(Calendar.MONTH) + 1);
        else month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        typeCountResultList = db.logEntryDAO().getEntriesTypeCounts(year, month);

        //putting the info inside an object that can go into chart
        List<PieEntry> entries = new ArrayList<PieEntry>();

        for (TypeCountResult data : typeCountResultList) {
            // turn your data into Entry objects
            entries.add(new PieEntry(data.count, data.type));
            //new Entry(data.getValueX(), data.getValueY())
        }


        PieDataSet dataSet = new PieDataSet(entries, ""); // add entries to dataset

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));
        dataSet.setColors(colors);
        dataSet.setValueTextSize(20);

        pieTypeData = new PieData(dataSet);
        typePieChart.setData(pieTypeData);

        typePieChart.notifyDataSetChanged(); // let the chart know it's data changed
        typePieChart.invalidate(); // refresh
    }

    void changeSizePieDataSet(Calendar calendar)
    {
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int monthNum = calendar.get(Calendar.MONTH) + 1;
        String month;
        if (monthNum < 10) month = "0" + String.valueOf(calendar.get(Calendar.MONTH) + 1);
        else month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        sizeCountResultList = db.logEntryDAO().getEntriesSizeCounts(year, month);

        //putting the info inside an object that can go into chart
        List<PieEntry> entries = new ArrayList<PieEntry>();

        for (SizeCountResult data : sizeCountResultList) {
            // turn your data into Entry objects
            entries.add(new PieEntry(data.count, data.size));
            //new Entry(data.getValueX(), data.getValueY())
        }


        PieDataSet dataSet = new PieDataSet(entries, ""); // add entries to dataset

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));
        dataSet.setColors(colors);
        dataSet.setValueTextSize(20);

        pieSizeData = new PieData(dataSet);
        sizePieChart.setData(pieSizeData);

        sizePieChart.notifyDataSetChanged(); // let the chart know it's data changed
        sizePieChart.invalidate(); // refresh
    }

}

