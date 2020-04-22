package net.anvisys.xpen.HomePage;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.anvisys.xpen.AddActivityActivity;
import net.anvisys.xpen.AddExpenseActivity;
import net.anvisys.xpen.Common.APP_CONST;
import net.anvisys.xpen.Common.DataAccess;
import net.anvisys.xpen.Common.Session;
import net.anvisys.xpen.Dashboard2Activity;
import net.anvisys.xpen.Object.ActivityData;
import net.anvisys.xpen.Object.Profile;
import net.anvisys.xpen.Object.ProjectData;
import net.anvisys.xpen.ProjectActivity;
import net.anvisys.xpen.R;
import net.anvisys.xpen.ReportActivity;
import net.anvisys.xpen.fragments.ActivityFragment;
import net.anvisys.xpen.fragments.ListenerInterface;
import net.anvisys.xpen.fragments.ProjectFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProjectFragment extends Fragment implements ListenerInterface, View.OnClickListener {

    View viewActivityActions;
    View viewProjectActions;
    ProjectData selProject;
    Profile myProfile;

    View actionAddProjectExpense, actionAddActivity,showMyExpenses,actionProjectRemove;

    public MyProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_project, container, false);
        viewActivityActions = view.findViewById(R.id.viewActivityActions);
        viewProjectActions = view.findViewById(R.id.viewProjectActions);

        actionAddProjectExpense = view.findViewById(R.id.actionAddProjectExpense);
        actionAddActivity = view.findViewById(R.id.actionAddActivity);
        showMyExpenses = view.findViewById(R.id.showMyExpenses);
        actionProjectRemove = view.findViewById(R.id.actionProjectRemove);

        actionAddProjectExpense.setOnClickListener(this);
        actionAddActivity.setOnClickListener(this);
        showMyExpenses.setOnClickListener(this);
        actionProjectRemove.setOnClickListener(this);


        myProfile = Session.GetUser(getContext());

        viewActivityActions.setVisibility(View.GONE);
        viewProjectActions.setVisibility(View.VISIBLE);



        return view;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.actionAddProjectExpense)
        {
            Intent newExpenseIntent = new Intent(getContext(), AddExpenseActivity.class);
            newExpenseIntent.putExtra("Type", "Project");
            newExpenseIntent.putExtra("ProjectID", selProject.ProjectID);
            newExpenseIntent.putExtra("ProjectName", selProject.ProjectName);
            newExpenseIntent.putExtra("Project", selProject);
            startActivity(newExpenseIntent);
        }
        else if(id==R.id.actionProjectRemove)
        {

            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
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

                    DataAccess da = new DataAccess(getContext());
                    da.open();
                    da.DeleteProject(selProject.ProjectID);
                    da.close();
                }
            });
            dialog.create();
            dialog.show();

            viewProjectActions.setVisibility(View.GONE);

        }
        else if(id==R.id.actionAddActivity)
        {
            Intent actIntent = new Intent(getContext(), AddActivityActivity.class);
            actIntent.putExtra("Type", "Project");
            actIntent.putExtra("ProjectID",selProject.ProjectID);
            actIntent.putExtra("ProjectName",selProject.ProjectName);
            actIntent.putExtra("Project", selProject);
            startActivityForResult(actIntent, APP_CONST.REQUEST_ACTIVITY_CODE);
        }

        else if(id==R.id.showMyExpenses)
        {
            String url =   url = APP_CONST.APP_SERVER_URL + "api/ExpenseItem/Project/" + selProject.ProjectID+"/Employee/" +myProfile.UserID ;;
            Intent actIntent = new Intent(getContext(), ReportActivity.class);
            actIntent.putExtra("Type", "Project");
            actIntent.putExtra("Project", selProject);
            actIntent.putExtra("url", url);
            startActivity(actIntent);
        }
    }

    @Override
    public void OnActivitySelect(ActivityData actData) {

    }

    @Override
    public void OnActivityDeSelect() {

    }

    @Override
    public void OnProjectSelect(ProjectData project) {
        selProject = project;
        //selPrjID = project.ProjectID;
        //selPrjName = project.ProjectName;

        viewActivityActions.setVisibility(View.GONE);
        viewProjectActions.setVisibility(View.VISIBLE);

        if(myProfile.Role.matches("Manager") || myProfile.Role.matches("Admin"))
        {
            //managerBar.setVisibility(View.VISIBLE);

        }
        else
        {
            //managerBar.setVisibility(View.GONE);
            //managerExp.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnDeSelect() {

    }
}
