package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;


public class GroupManagementActivity extends Activity
{
    private ArrayList<Group> mGroups;
    private RecyclerView mRecyclerView;
    private GroupAdapter mGroupAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseHandler mDb = new DatabaseHandler(this);


    @Override protected void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_group_management);
            mGroups = mDb.getAllGroups();
            mGroupAdapter = new GroupAdapter(mGroups);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView = (RecyclerView) findViewById(R.id.group_recycle_view);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mGroupAdapter);
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
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//
//
//@SuppressWarnings ( "UnusedParameters" )
//public class GroupManagementActivity extends Activity
//{
//    @Override
//    protected void onCreate(final Bundle savedInstanceState)
//        {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_group_management);
//            final DatabaseHandler db = new DatabaseHandler(this);
//            final ArrayList<Group> groups = db.getAllGroups();
//            final ListView listView = (ListView) findViewById(R.id.list_view_group);
//            final GroupAdapter groupAdapter = new GroupAdapter(groups);
//            listView.setAdapter(groupAdapter);
//            (findViewById(R.id.manage_contacts)).setOnClickListener(new View.OnClickListener()
//            {
//                @Override public void onClick(final View v)
//                    {
//                        startActivity(new Intent(GroupManagementActivity.this,
//                                                 ContactManagementActivity.class));
//                    }
//            });
//        }
//
//
//    public void AddNewGroup(final View _view)
//        {
//            startActivity(new Intent(GroupManagementActivity.this, GroupConfigActivity.class));
//        }
//}
