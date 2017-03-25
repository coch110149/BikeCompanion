package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


@SuppressWarnings ( "UnusedParameters" )
public class GroupManagementActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_group_management);
            final DatabaseHandler db = new DatabaseHandler(this);
            final ArrayList<Group> groups = (ArrayList<Group>) db.getAllGroups();
            final ListView listView = (ListView) findViewById(R.id.list_view_group);
            final GroupAdapter groupAdapter = new GroupAdapter(this, groups);
            listView.setAdapter(groupAdapter);
            (findViewById(R.id.manage_contacts)).setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        startActivity(new Intent(GroupManagementActivity.this,
                                                 ContactManagementActivity.class));
                    }
            });
        }


    public void AddNewGroup(final View _view)
        {
            startActivity(new Intent(GroupManagementActivity.this, GroupConfigActivity.class));
        }
}
