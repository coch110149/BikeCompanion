package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;


public class ContactManagementActivity extends Activity
{
    private ArrayList<Contact> mContacts;
    private RecyclerView mRecyclerView;
    private ContactAdapter mContactAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseHandler mDb = new DatabaseHandler(this);
    private String mGroupId;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contact_management);
            mRecyclerView = (RecyclerView) findViewById(R.id.contact_recycle_view);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mContacts = mDb.getAllContacts();
            mContactAdapter =
                    new ContactAdapter(mContacts, this, new ContactAdapter.OnItemClickListener()
                    {
                        @Override public void onItemClick(Contact _contact)
                            {
                                if(mGroupId != null)
                                {
                                    int groupId = Integer.parseInt(mGroupId);
                                    if(_contact.in(groupId, getBaseContext()))
                                    {
                                        mDb.removeContactFromGroup(_contact.getId(), groupId);
                                    }else
                                    {
                                        mDb.addContactToGroup(_contact.getId(), groupId);
                                    }
                                }
                            }
                    });
            (findViewById(R.id.add_contact)).setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                    {
                        startActivity(new Intent(ContactManagementActivity.this,
                                                 ContactConfigActivity.class));
                    }
            });
            if(getIntent().hasExtra("GroupId"))
            {
                mGroupId = getIntent().getStringExtra("GroupId");
                mContactAdapter.addGroupId(mGroupId);
            }
            mRecyclerView.setAdapter(mContactAdapter);
        }
}
