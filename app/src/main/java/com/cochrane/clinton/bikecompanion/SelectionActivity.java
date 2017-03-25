package com.cochrane.clinton.bikecompanion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class SelectionActivity extends AppCompatActivity
{
    private final DatabaseHandler mDb = new DatabaseHandler(this);
    private ListView mListView;
    private ArrayList<?> mObjects;
    private Button mOkayButton;
    private Context mContext;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_selection);
            mContext = getBaseContext();
            final String contactId = getIntent().getStringExtra("ContactId");
            final String typeOfRequest = getIntent().getStringExtra("TypeOfRequest");
            mListView = (ListView) findViewById(R.id.object_list_view);
            mOkayButton = (Button) findViewById(R.id.okay_button);
            mObjects = mDb.getObjects(typeOfRequest);
            final SelectionAdapter selectionAdapter =
                    (contactId == null) ? new SelectionAdapter(this, mObjects) :
                    new SelectionAdapter(this, mObjects, contactId);
            mListView.setAdapter(selectionAdapter);
            if("Bike".equals(typeOfRequest))
            {
                bikeSelection();
            }else if("Group".equals(typeOfRequest))
            {
                groupSelection(contactId);
            }
        }


    private void bikeSelection()
        {
            mOkayButton.setVisibility(View.GONE);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view,
                                        final int position, final long id)
                    {
                        final Intent intent = new Intent();
                        final Bike bike = (Bike) mObjects.get(position);
                        intent.setData(Uri.parse(Integer.toString(bike.getId())));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
            });
        }


    private void groupSelection(final String _contactId)
        {
            mOkayButton.setText(R.string.finished_selection);
            mOkayButton.setVisibility(View.VISIBLE);
            if(_contactId != null)
            {
                group_contactSelection(_contactId);
            }else
            {
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(final AdapterView<?> parent, final View view,
                                            final int position, final long id)
                        {
                            final Group group = (Group) mObjects.get(position);
                            group.swapSelected();
                            if(group.isSelected())
                            {
                                view.setBackgroundColor(ContextCompat.getColor(
                                        mContext, R.color.bright_teal));
                            }else
                            {
                                view.setBackgroundColor(0);
                            }
                            mDb.updateGroup(group);
                        }
                });
                mOkayButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(final View v){finish();}
                });
            }
        }


    private void group_contactSelection(final String _extraId)
        {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @SuppressWarnings ( "LawOfDemeter" ) @Override
                public void onItemClick(final AdapterView<?> parent, final View view,
                                        final int position, final long id)
                    {
                        final Group group = (Group) mObjects.get(position);
                        final Contact contact = group.addRemoveContact(_extraId, getBaseContext());
                        if(contact.in(group.getId(), getBaseContext()))
                        {
                            view.setBackgroundColor(ContextCompat.getColor(
                                    mContext, R.color.bright_teal));
                        }else
                        {
                            view.setBackgroundColor(0);
                        }
                    }
            });
            mOkayButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        final Intent intent =
                                new Intent(SelectionActivity.this, SelectionActivity.class);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
            });
        }
//    private void contactSelection()
//        {
//            mOkayButton.setVisibility(View.VISIBLE);
//            mOkayButton.setText(R.string.finished_selection);
//            /*
//             secondaryButton.Text = "Edit"
//                        onClick calls intent to Contact Management(id)
//                if _groupId is empty
//                    primaryButton.Text = "Manage Group Associations"
//                        onClick calls GroupSelection and passes contactId
//
//               else
//                    if(contact in group)
//                        primaryButton.Text = "Remove From Group"
//                            onClick will remove from group
//                        highlight
//                    else
//                        primaryButton.Text = "Add To Group"
//                            onClick will add to Group
//
//             */
//        }
}
