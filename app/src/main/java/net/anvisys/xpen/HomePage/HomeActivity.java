package net.anvisys.xpen.HomePage;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.anvisys.xpen.R;

public class HomeActivity extends AppCompatActivity {

    RadioGroup rgSelection;
    RadioButton rbDashboard, rbActivity, rbProject;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("xPen");
        actionBar.show();
        rgSelection = findViewById(R.id.rgSelection);
        rbDashboard = findViewById(R.id.rbDashboard);
        rbActivity = findViewById(R.id.rbActivity);
        rbProject = findViewById(R.id.rbProject);
        //viewPager = findViewById(R.id.viewPager);
        setDashboardView();

        rgSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setDashboardView();
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        DashboardFragment fragment = new DashboardFragment();
        fragmentTransaction.add(R.id.viewFragment, fragment);
        fragmentTransaction.commit();

    }



    private void setDashboardView()
    {
        try {
            if (rbDashboard.isChecked()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                DashboardFragment fragment = new DashboardFragment();
                fragmentTransaction.replace(R.id.viewFragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else if (rbActivity.isChecked()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MyActivityFragment fragment = new MyActivityFragment();
                fragmentTransaction.replace(R.id.viewFragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else if (rbProject.isChecked()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MyProjectFragment fragment = new MyProjectFragment();
                fragmentTransaction.replace(R.id.viewFragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
        }
    }
}
