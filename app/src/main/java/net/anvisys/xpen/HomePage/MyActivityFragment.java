package net.anvisys.xpen.HomePage;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.anvisys.xpen.AccountsActivity;
import net.anvisys.xpen.AddActivityActivity;
import net.anvisys.xpen.AddExpenseActivity;
import net.anvisys.xpen.ApprovalActivity;
import net.anvisys.xpen.Common.APP_CONST;
import net.anvisys.xpen.Common.DataAccess;
import net.anvisys.xpen.Common.Session;
import net.anvisys.xpen.DashboardActivity;
import net.anvisys.xpen.InvoiceActivity;
import net.anvisys.xpen.LocalExpenseActivity;
import net.anvisys.xpen.Object.ActivityData;
import net.anvisys.xpen.Object.Profile;
import net.anvisys.xpen.Object.ProjectData;
import net.anvisys.xpen.PurchaseActivity;
import net.anvisys.xpen.R;
import net.anvisys.xpen.ReportActivity;
import net.anvisys.xpen.TaxActivity;
import net.anvisys.xpen.TransactionReportActivity;
import net.anvisys.xpen.fragments.ActivityFragment;
import net.anvisys.xpen.fragments.ListenerInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyActivityFragment extends Fragment implements ListenerInterface, View.OnClickListener {

    ActivityData selActivity;
    View viewActivityActions;
    Profile myProfile;

    View actionAddActivityExpense, actionShowActivityDetails,actionAddActivity1;

    public MyActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_activity, container, false);

        viewActivityActions = view.findViewById(R.id.viewActivityActions);
        actionAddActivityExpense = view.findViewById(R.id.actionAddActivityExpense);
        actionShowActivityDetails = view.findViewById(R.id.actionShowActivityDetails);
        actionAddActivity1 = view.findViewById(R.id.actionAddActivity1);

        actionAddActivityExpense.setOnClickListener(this);
        actionShowActivityDetails.setOnClickListener(this);
        actionAddActivity1.setOnClickListener(this);

        ActivityFragment.RegisterListener(this);

        myProfile = Session.GetUser(getContext());
        return  view;
    }

    @Override
    public void OnActivitySelect(ActivityData actData) {
        selActivity = actData;


        viewActivityActions.setVisibility(View.VISIBLE);

        if(myProfile.Role.matches("Manager") || myProfile.Role.matches("Admin"))
        {
            //managerBar.setVisibility(View.GONE);
            //ExpManager.setVisibility(View.VISIBLE);
        }
        else
        {
            //managerBar.setVisibility(View.GONE);
            //ExpManager.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnActivityDeSelect() {

    }

    @Override
    public void OnProjectSelect(ProjectData project) {

    }

    @Override
    public void OnDeSelect() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.actionAddActivityExpense)
        {
            Intent newExpenseIntent = new Intent(getContext(), AddExpenseActivity.class);
            newExpenseIntent.putExtra("Type", "Activity");
            newExpenseIntent.putExtra("ActivityID", selActivity.ActivityID);
            newExpenseIntent.putExtra("ActivityName", selActivity.ActivityName);
            newExpenseIntent.putExtra("Activity", selActivity);
            startActivity(newExpenseIntent);
        }

        else if(id==R.id.actionShowActivityDetails)
        {
            String url = APP_CONST.APP_SERVER_URL + "api/ExpenseItem/Activity/" + selActivity.ActivityID;
            Intent actIntent = new Intent(getContext(), ReportActivity.class);
            actIntent.putExtra("Type", "Activity");
            actIntent.putExtra("Activity", selActivity);
            actIntent.putExtra("url", url);
            startActivity(actIntent);
        }
        else if(id==R.id.actionAddActivity1)
        {
            String url = APP_CONST.APP_SERVER_URL + "api/ExpenseItem/Activity/" + selActivity.ActivityID;
            Intent actIntent = new Intent(getContext(), AddActivityActivity.class);
            actIntent.putExtra("Type", "Activity");
            actIntent.putExtra("Activity", selActivity);
            actIntent.putExtra("url", url);
            startActivity(actIntent);
        }
    }


}
