package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class SelectionActivity extends Activity
{
    private ArrayList<?> mObjects;
    private RecyclerView mRecyclerView;
    private SelectionAdapter mSelectionAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseHandler mDb = new DatabaseHandler(this);
    private String mTypeOfRequest;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_selection);
            mRecyclerView = (RecyclerView) findViewById(R.id.selection_recycle_view);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mTypeOfRequest = getIntent().getStringExtra("TypeOfRequest");
            mObjects = mDb.getObjects(mTypeOfRequest);
            final Button okButton = (Button) findViewById(R.id.okay_button);
            okButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                    {
                        finish();
                    }
            });
            if(getIntent().hasExtra("ContactId"))
            {
                final Contact contact = mDb.getContact(Integer.parseInt(
                        getIntent().getStringExtra("ContactId")));
                mSelectionAdapter =
                        new SelectionAdapter(mObjects, this,
                                             new SelectionAdapter.OnItemClickListener()
                                             {
                                                 @Override public void onItemClick(Group item)
                                                     {
                                                         int id = item.getId();
                                                         Context context = getBaseContext();
                                                         if(contact.in(id, context))
                                                         {
                                                             contact.removeFromGroup(id, context);
                                                         }else
                                                         {
                                                             contact.addToGroup(id, context);
                                                         }
                                                     }
                                             });
                mSelectionAdapter.addContact(contact);
            }else
            {
                if("Bike".equals(mTypeOfRequest))
                {
                    okButton.setVisibility(View.GONE);
                }
                mSelectionAdapter = new SelectionAdapter(mObjects, this,
                                                         new SelectionAdapter.OnItemClickListener()
                                                         {
                                                             @Override public void onItemClick
                                                                     (Group item)
                                                                 {
                                                                     item.swapSelected();
                                                                     mDb.updateGroup(item);
                                                                 }
                                                         });

            }
            mRecyclerView.setAdapter(mSelectionAdapter);
        }


    private int getLastVisibleItemPosition()
        {
            return mLinearLayoutManager.findLastVisibleItemPosition();
        }
    ///// TODO: 28/03/2017 look into ItemTouchHelper.SimpleCallback
}