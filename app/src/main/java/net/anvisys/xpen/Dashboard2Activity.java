package net.anvisys.xpen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
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
import net.anvisys.xpen.Common.DataAccess;
import net.anvisys.xpen.Common.Session;
import net.anvisys.xpen.Object.ActivityData;
import net.anvisys.xpen.Object.Expense;
import net.anvisys.xpen.Object.Profile;
import net.anvisys.xpen.Object.ProjectData;
import net.anvisys.xpen.fragments.ActivityFragment;
import net.anvisys.xpen.fragments.ExpenseTypeListener;
import net.anvisys.xpen.fragments.ListenerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Dashboard2Activity extends AppCompatActivity implements ListenerInterface{

    View viewApproval;
    View managerBar,adminBar;
    Profile myProfile;
    List<Expense> expenseData;
    ActivityData selActivity;
    View viewActivityActions,viewProjectActions,ExpManager,personal,managerExp,viewPersonalExp,viewCreateActivity,actionAddActivity1;
    DashboardActivity.ExpenseTypeTabAdapter expenseTypeadapter;
    DashboardActivity.ExpTypeAdapter expTypeAdapter;
    int selPrjID,selExpenseID;
    String selPrjName;
    ProjectData selProject;
    TextView chartTitle,txtActivity1,txtActivity2,ExpenseStatus,txtInProgCount,txtInProgress,txtSubCount,txtSubmitted,txtApprovedCount,txtApproved;
    private Integer ClickCount=0;
    private long prevTime = 0;
    PieChart pieChart;
    BarChart barChart;
    Button showLocalData;
    NumberFormat currFormat;
    RadioGroup rg;
    RadioButton rbActivity,rbProject;
    View viewDashboard;
    Expense exp;
    int EnpenseValue =0,ACount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Dashboard");
        actionBar.show();

       RadioGroup  rgSelection = findViewById(R.id.rgSelection);

        viewActivityActions = findViewById(R.id.viewActivityActions);     // this is inside activity_project_Bar
        viewProjectActions = findViewById(R.id.viewProjectActions);       // this is inside activity_project_Bar
        actionAddActivity1 = findViewById(R.id.actionAddActivity1);

        //viewPersonalExp = findViewById(R.id.viewPersonalExp);
        pieChart = findViewById(R.id.pie_chart);
        showLocalData = findViewById(R.id.showLocalData);

        managerBar = findViewById(R.id.action_bar_manager);

        adminBar = findViewById(R.id.action_bar_admin);
        barChart = findViewById(R.id.barchart);
        txtInProgress = findViewById(R.id.txtInProgress);
        txtApproved = findViewById(R.id.txtApproved);
        txtSubmitted = findViewById(R.id.txtSubmitted);
        txtActivity1 = findViewById(R.id.txtActivity1);
        txtActivity2 = findViewById(R.id.txtActivity2);
        personal = findViewById(R.id.personal);
        ExpenseStatus= findViewById(R.id.ExpenseStatus);
        txtInProgCount= findViewById(R.id.txtInProgCount);
        txtSubCount= findViewById(R.id.txtSubCount);
        txtApprovedCount= findViewById(R.id.txtApprovedCount);
        rbActivity = findViewById(R.id.rbActivity);
        rbProject = findViewById(R.id.rbProject);
        rg = findViewById(R.id.rg);

       viewDashboard = findViewById(R.id.viewDashboard);



        txtActivity1.setSelected(true);
        txtActivity2.setSelected(true);

        myProfile = Session.GetUser(getApplicationContext());

        if(myProfile.Role.matches("Employee"))
        {
            rgSelection.setVisibility(View.GONE);
        }

        currFormat = NumberFormat.getCurrencyInstance();
        currFormat.setCurrency(Currency.getInstance("INR"));

        ActivityFragment.RegisterListener(this);

        viewActivityActions.setVisibility(View.GONE);
       //InitiateActionBar();
        CreateExpenseChart();
        CreateDailyExpChart();
    }

    public void onToggleClicked(View view) {
        GetDashboardData();

            if (((ToggleButton) view).isChecked()) {
                // handle toggle on
                ExpenseStatus.setText("My Expense");
                txtInProgCount.setText("No of activity: " );
                txtInProgress.setText(currFormat.format(EnpenseValue));
                txtApprovedCount.setText("No of activity: " + ACount);
                txtApproved.setText(currFormat.format(EnpenseValue));
                txtSubCount.setText("No of activity: " + ACount);
                txtSubmitted.setText(currFormat.format(EnpenseValue));

            }
        else {
            // handle toggle off
            ExpenseStatus.setText("Project Expense");
            txtInProgCount.setText("No of activity: "+ACount);
            txtInProgress.setText(currFormat.format(EnpenseValue));
            txtApprovedCount.setText("No of activity: "+ACount);
            txtApproved.setText(currFormat.format(EnpenseValue));
            txtSubCount.setText("No of activity: "+ACount);
            txtSubmitted.setText(currFormat.format(EnpenseValue));
        }
    }

    private void InitiateActionBar()
    {
        viewActivityActions.setVisibility(View.GONE);
        viewProjectActions.setVisibility(View.GONE);
        managerBar.setVisibility(View.GONE);

        if(myProfile.Role.matches("Admin"))
        {
            adminBar.setVisibility(View.VISIBLE);
        }
        else
        {
            adminBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void OnActivitySelect(ActivityData actData) {
        selActivity = actData;
        viewDashboard.setVisibility(View.INVISIBLE);

        viewActivityActions.setVisibility(View.VISIBLE);
        viewProjectActions.setVisibility(View.GONE);


        if(myProfile.Role.matches("Manager") || myProfile.Role.matches("Admin"))
        {
            managerBar.setVisibility(View.GONE);
            ExpManager.setVisibility(View.VISIBLE);
        }
        else
        {
            managerBar.setVisibility(View.GONE);
            ExpManager.setVisibility(View.GONE);
        }
    }
    @Override
    public void OnProjectSelect(ProjectData project) {
        selProject = project;
        selPrjID = project.ProjectID;
        selPrjName = project.ProjectName;

        viewActivityActions.setVisibility(View.GONE);
        viewProjectActions.setVisibility(View.VISIBLE);

        if(myProfile.Role.matches("Manager") || myProfile.Role.matches("Admin"))
        {
            managerBar.setVisibility(View.VISIBLE);

        }
        else
        {
            managerBar.setVisibility(View.GONE);
            managerExp.setVisibility(View.GONE);
        }
    }


    @Override
    public void OnActivityDeSelect() {
        viewActivityActions.setVisibility(View.GONE);
        viewDashboard.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnDeSelect() {
        viewProjectActions.setVisibility(View.GONE);
    }

    public void OnAction(View v)
    {
        int id = v.getId();
        if(id==R.id.actionAddExpense)
        {
            Intent newExpenseIntent = new Intent(Dashboard2Activity.this, AddExpenseActivity.class);
            newExpenseIntent.putExtra("Type", "Activity");
            newExpenseIntent.putExtra("ActivityID", selActivity.ActivityID);
            newExpenseIntent.putExtra("ActivityName", selActivity.ActivityName);
            newExpenseIntent.putExtra("Activity", selActivity);
            startActivity(newExpenseIntent);
        }

        else if(id==R.id.actionShowActivityDetails)
        {
            String url = APP_CONST.APP_SERVER_URL + "api/ExpenseItem/Activity/" + selActivity.ActivityID;
            Intent actIntent = new Intent(Dashboard2Activity.this, ReportActivity.class);
            actIntent.putExtra("Type", "Activity");
            actIntent.putExtra("Activity", selActivity);
            actIntent.putExtra("url", url);
            startActivity(actIntent);
        }

        else if(id==R.id.actionAddProjectExpense)
        {
            Intent newExpenseIntent = new Intent(Dashboard2Activity.this, AddExpenseActivity.class);
            newExpenseIntent.putExtra("Type", "Project");
            newExpenseIntent.putExtra("ProjectID", selProject.ProjectID);
            newExpenseIntent.putExtra("ProjectName", selProject.ProjectName);
            newExpenseIntent.putExtra("Project", selProject);
            startActivity(newExpenseIntent);
        }
        else if(id==R.id.actionProjectRemove)
        {

            AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard2Activity.this);
            dialog.setMessage("Are you sure to remove project ?");
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    DataAccess da = new DataAccess(getApplicationContext());
                    da.open();
                    da.DeleteProject(selProject.ProjectID);
                    da.close();
                }
            });
            dialog.create();
            dialog.show();
            expenseTypeadapter.notifyDataSetChanged();
            managerBar.setVisibility(View.GONE);
            viewProjectActions.setVisibility(View.GONE);

        }
        else if(id==R.id.actionAddActivity)
        {
            Intent actIntent = new Intent(Dashboard2Activity.this, AddActivityActivity.class);
            actIntent.putExtra("Type", "Project");
            actIntent.putExtra("ProjectID",selProject.ProjectID);
            actIntent.putExtra("ProjectName",selProject.ProjectName);
            actIntent.putExtra("Project", selProject);
            startActivityForResult(actIntent,APP_CONST.REQUEST_ACTIVITY_CODE);
        }
        else if(id==R.id.actionAddActivity1)
        {
            Intent statement = new Intent(Dashboard2Activity.this, ProjectActivity.class);
            startActivity(statement);
        }

        else if(id==R.id.showMyExpenses)
        {
            String url =   url = APP_CONST.APP_SERVER_URL + "api/ExpenseItem/Project/" + selProject.ProjectID+"/Employee/" +myProfile.UserID ;;
            Intent actIntent = new Intent(Dashboard2Activity.this, ReportActivity.class);
            actIntent.putExtra("Type", "Project");
            actIntent.putExtra("Project", selProject);
            actIntent.putExtra("url", url);
            startActivity(actIntent);
        }
        else if(id==R.id.actionRemoveActivity)
        {

            AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard2Activity.this);
            // dialog.setTitle("Remove Activity");
            dialog.setMessage("Are you sure to remove Activity ?");
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    return;
                }
            });
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    DataAccess da = new DataAccess(getApplicationContext());
                    da.open();
                    da.DeleteActivity(selActivity.ActivityID);
                    da.close();
                }
            });
            dialog.create();
            dialog.show();


            expenseTypeadapter.notifyDataSetChanged();
            viewActivityActions.setVisibility(View.GONE);
        }
        else if(id==R.id.actionAddPersonalExpense)
        {
            Intent expense = new Intent(Dashboard2Activity.this, AddExpenseActivity.class);
            expense.putExtra("Type", "Personal");
            expense.putExtra("ExpenseID",selExpenseID);
            expense.putExtra("ExpenseName",selExpenseID);
            startActivity(expense);
        }
        else if(id==R.id.actionShowPersonalExpense)
        {
            Intent statement = new Intent(Dashboard2Activity.this, ReportActivity.class);
            statement.putExtra("Type", "Personal");

            startActivity(statement);
        }
        else if(id==R.id.showTransaction)
        {
            Intent transaction = new Intent(Dashboard2Activity.this, TransactionReportActivity.class);
            transaction.putExtra("Type", "Project");
            transaction.putExtra("Project", selProject);
            startActivity(transaction);
        }
        else if(id==R.id.actionInvoice)
        {
            Intent invoice = new Intent(Dashboard2Activity.this, InvoiceActivity.class);
            invoice.putExtra("Type", "Project");
            invoice.putExtra("Project", selProject);
            startActivity(invoice);
        }
        else if(id==R.id.actionPurchase)
        {
            Intent purchase = new Intent(Dashboard2Activity.this, PurchaseActivity.class);
            purchase.putExtra("Type", "Project");
            purchase.putExtra("Project", selProject);
            startActivity(purchase);
        }
        else if(id==R.id.actionAllProjectExpenses)
        {
            String url = APP_CONST.APP_SERVER_URL + "api/ExpenseItem/Project/" + selProject.ProjectID ;
            Intent actIntent = new Intent(Dashboard2Activity.this, ReportActivity.class);
            actIntent.putExtra("Type", "AllExpense");
            actIntent.putExtra("Project", selProject);
            actIntent.putExtra("url", url);
            startActivity(actIntent);
        }
        else if(id == R.id.showApproval)
        {
            //String url = APP_CONST.APP_SERVER_URL + "api/ExpenseItem/Project/" + selProject.ProjectID ;
            Intent appIntent = new Intent(Dashboard2Activity.this, ApprovalActivity.class);
            startActivity(appIntent);
        }
        else if(id == R.id.showAccounts)
        {
            //String url = APP_CONST.APP_SERVER_URL + "api/ExpenseItem/Project/" + selProject.ProjectID ;
            Intent appIntent = new Intent(Dashboard2Activity.this, AccountsActivity.class);
            startActivity(appIntent);
        }

        else if(id == R.id.ShowTax)
        {
            Intent review = new Intent(Dashboard2Activity.this, TaxActivity.class);
            startActivity(review);
        }

        else if(id == R.id.ShowAllExpenses)
        {
            String url =   url = APP_CONST.APP_SERVER_URL + "api/ExpenseItem/Project/" + selProject.ProjectID+"/Employee/" +myProfile.UserID ;;
            Intent reportIntent = new Intent(Dashboard2Activity.this, ReportActivity.class);
            reportIntent.putExtra("Type", "AllWork");
            reportIntent.putExtra("Project", selProject);
            reportIntent.putExtra("url", url);
            startActivity(reportIntent);
        }

        else if(id==R.id.showLocalData)
        {
            Intent statement = new Intent(Dashboard2Activity.this, LocalExpenseActivity.class);

            startActivity(statement);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logOff) {

            LogOff();

            return true;
        } else if (id == R.id.Profile) {

            Intent statement = new Intent(Dashboard2Activity.this, ProfileActivity.class);
            startActivity(statement);
            return true;
        }
        else
        {
            return false;
        }

    }

    public void LogOff()
    {

        try {

            DataAccess da = new DataAccess(getApplicationContext());
            da.open();
            expenseData = da.GetAllExpenses();
            da.close();
            AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard2Activity.this);
            dialog.setTitle("Log out");
            if (expenseData.size() > 0) {

                dialog.setMessage("You have Local Data, pending to check in!");
            } else {

                dialog.setMessage("Are you sure ?");
            }

            dialog.setCancelable(false);
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    return;
                }
            });

            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (Session.LogOff(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), "Successfully LogOut", Toast.LENGTH_LONG).show();
                            Dashboard2Activity.this.finish();
                            DataAccess da = new DataAccess(getApplicationContext());
                            da.open();
                            da.ClearAll();
                            da.close();
                            return;
                        }
                    } catch (Exception ex) {

                    }
                }
            });
            dialog.create();
            dialog.show();
        }
        catch (Exception ex)
        {

            int a=1;
        }
    }

    @Override
    public void onBackPressed() {
        try {
            long time = SystemClock.currentThreadTimeMillis();

            if (prevTime == 0) {
                prevTime = SystemClock.currentThreadTimeMillis();
            }

            if (time - prevTime > 1000) {
                prevTime = time;
                ClickCount=0;
            }
            if (time - prevTime < 1000 && time > prevTime) {
                ClickCount++;
                if (ClickCount == 2) {

                    Dashboard2Activity.this.finish();
                } else {
                    prevTime = time;
                    String msg = "double click to close";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
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
        barChart.animateY(5000);
        BarData data = new BarData(Day, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.getLegend().setEnabled(false);
        barChart.setFitsSystemWindows(true);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new DefaultXAxisValueFormatter());
        barChart.setData(data);

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
            pieChart.setTouchEnabled(true);
            pieChart.setHighlightPerTapEnabled(true);
            //  dataset.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            // set the color
            PieData data = new PieData(labels, dataset); // initialize Piedata
            pieChart.getLegend().setPosition(Legend.LegendPosition.LEFT_OF_CHART);
            pieChart.getLegend().setEnabled(false);
            pieChart.setHoleRadius(0f);
            data.setValueTextSize(13f);
            pieChart.setData(data);
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

    public void GetDashboardData()
    {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            String  url = APP_CONST.APP_SERVER_URL + "api/Dashboard/Organization/" + myProfile.OrgID +"/Employee/" + myProfile.UserID;

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray json = response.getJSONArray("$values");
                        int x = json.length();
                        if (x==0)
                        {
                            //noData.setVisibility(View.VISIBLE);
                           // noData.setText("No Invoice for Selected Project");
                        }
                        for(int i = 0; i < x; i++){
                            try {
                                Expense expense = new Expense();
                                JSONObject jObj = json.getJSONObject(i);
                                expense.Date = jObj.getString("Date");
                                expense.ExpenseAmount = jObj.getInt("ExpenseAmount");
                                expense.ReceiveAmount = jObj.getInt("ReceiveAmount");
                                expense.ActivityCount = jObj.getInt("ActivityCount");
                                expense.Status = jObj.getString("Status");
                                if (expense.Status.matches("Added")){
                                    EnpenseValue =expense.ExpenseAmount;
                                    ACount = expense.ActivityCount;
                                }
                                else if(expense.Status.matches("Approved"))
                                {
                                    EnpenseValue =expense.ExpenseAmount;
                                    ACount = expense.ActivityCount;
                                }
                                else if(expense.Status.matches("Submitted"))
                                {
                                    EnpenseValue =expense.ExpenseAmount;
                                    ACount = expense.ActivityCount;
                                }
                                else if(expense.Status.matches("Inprogress"))
                                {
                                    EnpenseValue =expense.ExpenseAmount;
                                    ACount = expense.ActivityCount;
                                }
                            }

                            catch (Exception ex)
                            {
                                int a=1;
                                a++;
                            }

                        }

                    } catch (JSONException jEx) {
                        int a=1;
                        a++;
                    }



                }



            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }


            });


            RetryPolicy rPolicy = new DefaultRetryPolicy(0,-1,0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
            //*******************************************************************************************************
        } catch (Exception js) {

        } finally {

        }

    }

}
