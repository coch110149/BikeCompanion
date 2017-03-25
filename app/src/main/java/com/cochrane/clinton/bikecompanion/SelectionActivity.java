package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class SelectionActivity extends AppCompatActivity
{
    private final DatabaseHandler db = new DatabaseHandler(this);
    SelectionAdapter selectionAdapter;
    private ListView listView;
    private ArrayList<?> objects;
    private Button okayButton;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_selection);
            final String typeOfRequest = getIntent().getStringExtra("TypeOfRequest");
            final String contactId = getIntent().getStringExtra("ContactId");
            objects = db.getObjects(typeOfRequest);
            selectionAdapter = (contactId == null) ? new SelectionAdapter(this, objects) :
                               new SelectionAdapter(this, objects, contactId);
            listView = (ListView) findViewById(R.id.object_list_view);
            okayButton = (Button) findViewById(R.id.okay_button);
            listView.setAdapter(selectionAdapter);
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
            okayButton.setVisibility(View.GONE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view,
                                        final int position, final long id)
                    {
                        final Intent intent = new Intent();
                        final Bike bike = (Bike) objects.get(position);
                        intent.setData(Uri.parse(Integer.toString(bike.getId())));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
            });
        }


    private void groupSelection(String _contactId)
        {
            okayButton.setVisibility(View.VISIBLE);
            okayButton.setText(R.string.finished_selection);
            if(_contactId != null)
            {
                group_contactSelection(_contactId);
            }else
            {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(final AdapterView<?> parent, final View view,
                                            final int position, final long id)
                        {
                            final Group group = (Group) objects.get(position);
                            group.swapSelected();
                            if(group.isSelected())
                            {
                                view.setBackgroundColor(
                                        view.getResources().getColor(R.color.colorPrimary));
                            }else
                            {
                                view.setBackgroundColor(0);
                            }
                            db.updateGroup(group);
                        }
                });
                okayButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(final View v)
                        {
                            final Intent intent = new Intent();
                            //objects = (ArrayList<Group>) db.getAllGroups(true);
                            //Bundle bundle = new Bundle();
                            //bundle.putSerializable("activated groups", objects);
                            //intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                });
            }
        }


    private void group_contactSelection(final String _extraId)
        {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view,
                                        final int position, final long id)
                    {
                        final Group group = (Group) objects.get(position);
                        Contact contact = group.addRemoveContact(_extraId, getApplicationContext());
                        if(contact.in(group.getId(), getApplicationContext()))
                        {
                            view.setBackgroundColor(
                                    view.getResources().getColor(R.color.colorPrimary));
                        }else
                        {
                            view.setBackgroundColor(0);
                        }
                    }
            });
            okayButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        final Intent intent =
                                new Intent(SelectionActivity.this, SelectionActivity.class);
                        //objects = (ArrayList<Group>) db.getAllGroups(true);
                        //Bundle bundle = new Bundle();
                        //bundle.putSerializable("activated groups", objects);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
            });
        }


    private void contactSelection()
        {
            okayButton.setVisibility(View.VISIBLE);
            okayButton.setText(R.string.finished_selection);
            /*
             secondaryButton.Text = "Edit"
                        onClick calls intent to ContactManagment(id)
                if _groupId is empty
                    primaryButton.Text = "Manage Group Associations"
                        onClick calls GroupSelection and passes contactId

               else
                    if(contact in group)
                        primaryButton.Text = "Remove From Group"
                            onClick will remove from group
                        highlight
                    else
                        primaryButton.Text = "Add To Group"
                            onClick will add to Group

             */
        }
}
